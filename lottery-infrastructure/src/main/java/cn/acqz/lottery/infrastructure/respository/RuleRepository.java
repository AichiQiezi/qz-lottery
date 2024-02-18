package cn.acqz.lottery.infrastructure.respository;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.rule.model.aggregates.TreeRuleRich;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeLineVO;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.acqz.lottery.domain.rule.model.vo.TreeRootVO;
import cn.acqz.lottery.domain.rule.repository.IRuleRepository;
import cn.acqz.lottery.infrastructure.dao.RuleTreeDao;
import cn.acqz.lottery.infrastructure.dao.RuleTreeNodeDao;
import cn.acqz.lottery.infrastructure.dao.RuleTreeNodeLineDao;
import cn.acqz.lottery.infrastructure.po.RuleTree;
import cn.acqz.lottery.infrastructure.po.RuleTreeNode;
import cn.acqz.lottery.infrastructure.po.RuleTreeNodeLine;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 规则信息仓储服务接口实现
 * @Author: qz
 * @Date: 2024/2/4
 */
@Repository
public class RuleRepository implements IRuleRepository {

    @Resource
    private RuleTreeDao ruleTreeDao;
    @Resource
    private RuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private RuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public TreeRuleRich queryTreeRuleRich(Long treeId) {
        // 规则树
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        TreeRootVO treeRootVO = new TreeRootVO();
        treeRootVO.setTreeId(treeId);
        treeRootVO.setTreeRootNodeId(ruleTree.getTreeRootNodeId());
        treeRootVO.setTreeName(ruleTree.getTreeName());

        // 构建规则树模型
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeDao.queryRuleTreeNodeList(treeId);
        Map<Long, TreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodeList) {
            List<TreeNodeLineVO> treeNodeLineInfoList = new ArrayList<>();
            // 只有‘茎’才需要查询节点链路
            if (Constants.NodeType.STEM.equals(ruleTreeNode.getNodeType())) {
                RuleTreeNodeLine ruleTreeNodeLine = new RuleTreeNodeLine();
                ruleTreeNodeLine.setTreeId(ruleTreeNode.getTreeId());
                ruleTreeNodeLine.setNodeIdFrom(ruleTreeNode.getId());
                List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineList(ruleTreeNodeLine);
                for (RuleTreeNodeLine treeNodeLine : ruleTreeNodeLineList) {
                    TreeNodeLineVO treeNodeLineVO = new TreeNodeLineVO();
                    treeNodeLineVO.setNodeIdFrom(treeNodeLine.getNodeIdFrom());
                    treeNodeLineVO.setNodeIdTo(treeNodeLine.getNodeIdTo());
                    treeNodeLineVO.setRuleLimitType(treeNodeLine.getRuleLimitType());
                    treeNodeLineVO.setRuleLimitValue(treeNodeLine.getRuleLimitValue());
                    treeNodeLineInfoList.add(treeNodeLineVO);
                }
            }

            TreeNodeVO treeNodeVO = new TreeNodeVO();
            treeNodeVO.setTreeId(ruleTreeNode.getTreeId());
            treeNodeVO.setTreeNodeId(ruleTreeNode.getId());
            treeNodeVO.setNodeType(ruleTreeNode.getNodeType());
            treeNodeVO.setNodeValue(ruleTreeNode.getNodeValue());
            treeNodeVO.setRuleKey(ruleTreeNode.getRuleKey());
            treeNodeVO.setRuleDesc(ruleTreeNode.getRuleDesc());
            treeNodeVO.setTreeNodeLineInfoList(treeNodeLineInfoList);
            treeNodeMap.put(ruleTreeNode.getId(), treeNodeVO);
        }

        TreeRuleRich treeRuleRich = new TreeRuleRich();
        treeRuleRich.setTreeRoot(treeRootVO);
        treeRuleRich.setTreeNodeMap(treeNodeMap);
        return treeRuleRich;
    }
}
