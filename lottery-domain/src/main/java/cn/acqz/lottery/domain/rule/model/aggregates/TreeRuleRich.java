package cn.acqz.lottery.domain.rule.model.aggregates;

import cn.acqz.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.acqz.lottery.domain.rule.model.vo.TreeRootVO;

import java.util.Map;

/**
 * @Description: 规则树聚合
 * @Author: qz
 * @Date: 2024/2/4
 */
public class TreeRuleRich {
    /** 🌲根信息 */
    private TreeRootVO treeRoot;
    /** 树节点 ID --> 子节点 */
    private Map<Long, TreeNodeVO> treeNodeMap;

    public TreeRootVO getTreeRoot() {
        return treeRoot;
    }

    public void setTreeRoot(TreeRootVO treeRoot) {
        this.treeRoot = treeRoot;
    }

    public Map<Long, TreeNodeVO> getTreeNodeMap() {
        return treeNodeMap;
    }

    public void setTreeNodeMap(Map<Long, TreeNodeVO> treeNodeMap) {
        this.treeNodeMap = treeNodeMap;
    }
}
