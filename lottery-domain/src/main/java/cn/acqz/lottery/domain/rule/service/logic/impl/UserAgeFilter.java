package cn.acqz.lottery.domain.rule.service.logic.impl;

import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.service.logic.BaseLogic;

/**
 * @Description: 用户年龄过滤器
 * @Author: qz
 * @Date: 2024/2/4
 */
public class UserAgeFilter extends BaseLogic {
    @Override
    public String matterValue(DecisionMatterReq decisionMatterReq) {
        return decisionMatterReq.getValMap().get("age").toString();
    }
}
