package cn.acqz.lottery.application.process.draw;

import cn.acqz.lottery.application.process.draw.req.DrawProcessReq;
import cn.acqz.lottery.application.process.draw.res.DrawProcessResult;
import cn.acqz.lottery.application.process.draw.res.RuleQuantificationCrowdResult;

/**
 * @Description: 活动抽奖流程编排接口
 * @Author: qz
 * @Date: 2024/1/31
 */
public interface IActivityDrawProcess {
    /**
     * 执行抽奖流程
     * @param req 抽奖请求
     * @return    抽奖结果
     */
    DrawProcessResult doDrawProcess(DrawProcessReq req);

    /**
     * 规则量化人群，返回可参与的活动ID
     * @param req   规则请求
     * @return      量化结果，用户可以参与的活动ID
     */
//    RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req);

}

