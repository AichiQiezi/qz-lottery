package cn.acqz.lottery.domain.rule.service.engine;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.rule.model.aggregates.TreeRuleRich;
import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.model.res.EngineResult;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.acqz.lottery.domain.rule.model.vo.TreeRootVO;
import cn.acqz.lottery.domain.rule.service.logic.LogicFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/2/4
 */
public class EngineBase extends EngineConfig implements IEngine{
    private Logger logger = LoggerFactory.getLogger(EngineBase.class);

    @Override
    public EngineResult process(DecisionMatterReq matter) {
        throw new IllegalStateException("未实现规则引擎服务");
    }

    protected TreeNodeVO engineDecisionMaker(TreeRuleRich treeRuleRich, DecisionMatterReq matter) {
        TreeRootVO treeRoot = treeRuleRich.getTreeRoot();
        Map<Long, TreeNodeVO> treeNodeMap = treeRuleRich.getTreeNodeMap();

        TreeNodeVO treeNodeVO = treeNodeMap.get(treeRoot.getTreeRootNodeId());
        while (Constants.NodeType.STEM.equals(treeNodeVO.getNodeType())){
            String ruleKey = treeNodeVO.getRuleKey();
            LogicFilter logicFilter = logicFilterMap.get(ruleKey);
            String matterValue = logicFilter.matterValue(matter);
            Long nextNodeId = logicFilter.filter(matterValue, treeNodeVO.getTreeNodeLineInfoList());
            treeNodeVO = treeNodeMap.get(nextNodeId);
            logger.info("决策树引擎=>{} userId：{} treeId：{} treeNode：{} ruleKey：{} matterValue：{}", treeRoot.getTreeName(), matter.getUserId(), matter.getTreeId(), treeNodeVO.getTreeNodeId(), ruleKey, matterValue);
        }

        return treeNodeVO;
    }
}
