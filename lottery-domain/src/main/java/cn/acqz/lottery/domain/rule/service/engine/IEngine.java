package cn.acqz.lottery.domain.rule.service.engine;

import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.model.res.EngineResult;

/**
 * @Description: 规则过滤器引擎
 * @Author: qz
 * @Date: 2024/2/4
 */
public interface IEngine {

    /**
     * 规则过滤器接口
     *
     * @param matter      规则决策物料
     * @return            规则决策结果
     */
    EngineResult process(final DecisionMatterReq matter);

}

