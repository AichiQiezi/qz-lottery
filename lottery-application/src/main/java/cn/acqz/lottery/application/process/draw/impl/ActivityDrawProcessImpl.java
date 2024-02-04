package cn.acqz.lottery.application.process.draw.impl;

import cn.acqz.lottery.application.process.draw.IActivityDrawProcess;
import cn.acqz.lottery.application.process.draw.req.DrawProcessReq;
import cn.acqz.lottery.application.process.draw.res.DrawProcessResult;
import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.activity.service.partake.IActivityPartake;
import cn.acqz.lottery.domain.support.ids.IIdGenerator;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    @Resource
    private KafkaProducer kafkaProducer;

    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        return null;
    }
}
