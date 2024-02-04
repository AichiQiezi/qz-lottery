package cn.acqz.lottery.domain.rule.service.engine.impl;

import cn.acqz.lottery.domain.rule.model.aggregates.TreeRuleRich;
import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.model.res.EngineResult;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.acqz.lottery.domain.rule.repository.IRuleRepository;
import cn.acqz.lottery.domain.rule.service.engine.EngineBase;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/2/4
 */
@Service
public class RuleEngineHandle extends EngineBase {
    @Resource
    private IRuleRepository ruleRepository;

    @Override
    public EngineResult process(DecisionMatterReq matter) {
        // 决策规则树
        TreeRuleRich treeRuleRich = ruleRepository.queryTreeRuleRich(matter.getTreeId());
        if (Objects.isNull(treeRuleRich)){
            throw new IllegalStateException("Tree Rule is null!");
        }

        // 决策节点
        TreeNodeVO treeNodeInfo = engineDecisionMaker(treeRuleRich, matter);

        // 决策结果
        return new EngineResult(matter.getUserId(), treeNodeInfo.getTreeId(), treeNodeInfo.getTreeNodeId(), treeNodeInfo.getNodeValue());
    }
}

