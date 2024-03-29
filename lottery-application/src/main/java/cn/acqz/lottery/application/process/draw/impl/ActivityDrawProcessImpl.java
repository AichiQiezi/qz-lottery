package cn.acqz.lottery.application.process.draw.impl;

import cn.acqz.lottery.application.mq.producer.KafkaProducer;
import cn.acqz.lottery.application.process.draw.IActivityDrawProcess;
import cn.acqz.lottery.application.process.draw.req.DrawProcessReq;
import cn.acqz.lottery.application.process.draw.res.DrawProcessResult;
import cn.acqz.lottery.application.process.draw.res.RuleQuantificationCrowdResult;
import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.common.Result;
import cn.acqz.lottery.domain.activity.model.req.PartakeReq;
import cn.acqz.lottery.domain.activity.model.res.PartakeResult;
import cn.acqz.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import cn.acqz.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.acqz.lottery.domain.activity.model.vo.InvoiceVO;
import cn.acqz.lottery.domain.activity.model.vo.UserTakeActivityVO;
import cn.acqz.lottery.domain.activity.service.partake.IActivityPartake;
import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.model.res.EngineResult;
import cn.acqz.lottery.domain.rule.service.engine.EngineFilter;
import cn.acqz.lottery.domain.strategy.model.req.DrawReq;
import cn.acqz.lottery.domain.strategy.model.res.DrawResult;
import cn.acqz.lottery.domain.strategy.model.vo.DrawAwardVO;
import cn.acqz.lottery.domain.strategy.service.draw.IDrawExec;
import cn.acqz.lottery.domain.support.ids.IIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 活动抽奖流程编排接口的实现
 * @Author: qz
 * @Date: 2024/1/31
 */
@Service
public class ActivityDrawProcessImpl implements IActivityDrawProcess {
    private Logger logger = LoggerFactory.getLogger(ActivityDrawProcessImpl.class);

    @Resource
    private IActivityPartake activityPartake;
    @Resource
    private IDrawExec drawExec;
    @Resource(name = "ruleEngineHandle")
    private EngineFilter engineFilter;
    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;
    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        // 1. 领取活动
        PartakeReq partakeReq = new PartakeReq();
        partakeReq.setuId(req.getuId());
        partakeReq.setActivityId(req.getActivityId());
        partakeReq.setPartakeDate(new Date());
        PartakeResult partakeResult = activityPartake.doPartake(partakeReq);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())
                && !Constants.ResponseCode.NOT_CONSUMED_TAKE.getCode().equals(partakeResult.getCode())) {
            return new DrawProcessResult(partakeResult.getCode(), partakeResult.getInfo());
        }

        // 2. "首次"成功领取活动，发送 MQ 消息，扣减库存数量
        if (Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())) {
            ActivityPartakeRecordVO activityPartakeRecord = new ActivityPartakeRecordVO();
            activityPartakeRecord.setuId(req.getuId());
            activityPartakeRecord.setActivityId(req.getActivityId());
            activityPartakeRecord.setStockCount(partakeResult.getStockCount());
            activityPartakeRecord.setStockSurplusCount(partakeResult.getStockSurplusCount());
            // 发送 MQ 消息，异步更新库存
            kafkaProducer.sendLotteryActivityPartakeRecord(activityPartakeRecord);
        }

        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId();

        // 3. 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq(req.getuId(), strategyId));

        if (Constants.DrawState.FAIL.getCode().equals(drawResult.getDrawState())) {
            // 未中奖，修改抽奖单的状态为 已使用
            Result result = activityPartake.lockTackActivity(req.getuId(), req.getActivityId(), takeId);
            if (!Constants.ResponseCode.SUCCESS.getCode().equals(result.getCode())) {
                return new DrawProcessResult(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
            }
            return new DrawProcessResult(Constants.ResponseCode.LOSING_DRAW.getCode(), Constants.ResponseCode.LOSING_DRAW.getInfo());
        }

        // 4. 结果落库，修改抽奖单的状态为已使用，并保存用户的抽奖结果
        DrawAwardVO drawAwardVO = drawResult.getDrawAwardInfo();
        DrawOrderVO drawOrderVO = buildDrawOrderVO(req, strategyId, takeId, drawAwardVO);

        Result recordResult = activityPartake.recordDrawOrder(drawOrderVO);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(recordResult.getCode())) {
            return new DrawProcessResult(recordResult.getCode(), recordResult.getInfo());
        }

        // 5. 发送 MQ，触发发奖流程
        InvoiceVO invoiceVO = buildInvoiceVO(drawOrderVO);
        this.triggerPrize(invoiceVO, activityPartake);
        // 6. 返回结果
        return new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawAwardVO);
    }

    public void triggerPrize(InvoiceVO invoiceVO, IActivityPartake activityPartake) {
        ListenableFuture<SendResult<String, Object>> future = kafkaProducer.sendLotteryInvoice(invoiceVO);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                // 4.1 MQ 消息发送完成，更新数据库表 user_strategy_export.mq_state = 1
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.COMPLETE.getCode());
            }

            @Override
            public void onFailure(Throwable throwable) {
                // 4.2 MQ 消息发送失败，更新数据库表 user_strategy_export.mq_state = 2 【等待定时任务扫码补偿MQ消息】
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.FAIL.getCode());
            }

        });
    }

    /**
     * 构建发货单对象，这里地址信息就设置为 null 了
     *
     * @param drawOrderVO 奖品单
     * @return 发货单对象
     */
    private InvoiceVO buildInvoiceVO(DrawOrderVO drawOrderVO) {
        InvoiceVO invoiceVO = new InvoiceVO();
        invoiceVO.setuId(drawOrderVO.getuId());
        invoiceVO.setOrderId(drawOrderVO.getOrderId());
        invoiceVO.setAwardId(drawOrderVO.getAwardId());
        invoiceVO.setAwardType(drawOrderVO.getAwardType());
        invoiceVO.setAwardName(drawOrderVO.getAwardName());
        invoiceVO.setAwardContent(drawOrderVO.getAwardContent());
        invoiceVO.setShippingAddress(null);
        invoiceVO.setExtInfo(null);
        return invoiceVO;
    }

    /**
     * 构建奖品单
     *
     * @param req         抽奖请求
     * @param strategyId  策略 ID
     * @param takeId      活动领取 ID
     * @param drawAwardVO 中奖的奖品信息
     * @return 奖品单
     */
    private DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardVO drawAwardVO) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setuId(req.getuId());
        drawOrderVO.setTakeId(takeId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardVO.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardVO.getGrantType());
        drawOrderVO.setGrantDate(drawAwardVO.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardVO.getAwardId());
        drawOrderVO.setAwardType(drawAwardVO.getAwardType());
        drawOrderVO.setAwardName(drawAwardVO.getAwardName());
        drawOrderVO.setAwardContent(drawAwardVO.getAwardContent());
        return drawOrderVO;
    }

    @Override
    public RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req) {
        EngineResult engineResult = engineFilter.process(req);
        if (!engineResult.isSuccess()) {
            return new RuleQuantificationCrowdResult(Constants.ResponseCode.RULE_ERR.getCode(), Constants.ResponseCode.RULE_ERR.getInfo());
        }
        RuleQuantificationCrowdResult ruleQuantificationCrowdResult = new RuleQuantificationCrowdResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        ruleQuantificationCrowdResult.setActivityId(Long.valueOf(engineResult.getNodeValue()));
        return ruleQuantificationCrowdResult;
    }
}
