package cn.acqz.lottery.domain.rule.service.logic;

import cn.acqz.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * @Description: 逻辑过滤器接口
 * @Author: qz
 * @Date: 2024/2/4
 */
public interface LogicFilter {
    /**
     *
     * @param decisionMatterReq 决策物料
     * @return 决策值
     */
    String matterValue(DecisionMatterReq decisionMatterReq);

    /**
     * 逻辑决策器
     * @param matterValue 决策值
     * @param treeNodeLineVOList 规则树线信息
     * @return 下一个节点值
     */
    Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineVOList);
}
