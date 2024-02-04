package cn.acqz.lottery.domain.rule.repository;

import cn.acqz.lottery.domain.rule.model.aggregates.TreeRuleRich;

/**
 * @Description: 规则信息仓储服务接口
 * @Author: qz
 * @Date: 2024/2/4
 */
public interface IRuleRepository {
    /**
     * 查询规则决策树配置
     */
    TreeRuleRich queryTreeRuleRich(Long treeId);
}
