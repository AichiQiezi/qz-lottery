package cn.acqz.lottery.domain.rule.service.logic;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/2/4
 */
public abstract class BaseLogic implements LogicFilter {

    @Override
    public Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineVOList) {
        for (TreeNodeLineVO treeNodeLineVO : treeNodeLineVOList) {
            if (decisionLogic(matterValue, treeNodeLineVO)) {
                return treeNodeLineVO.getNodeIdTo();
            }
        }
        throw new IllegalArgumentException("决策树路径匹配出现错误");
    }

    private boolean decisionLogic(String matterValue, TreeNodeLineVO treeNodeLineVO) {
        switch (treeNodeLineVO.getRuleLimitType()) {
            case Constants.RuleLimitType.EQUAL:
                return matterValue.equals(treeNodeLineVO.getRuleLimitValue());
            case Constants.RuleLimitType.GT:
                return Double.parseDouble(matterValue) > Double.parseDouble(treeNodeLineVO.getRuleLimitValue());
            case Constants.RuleLimitType.LT:
                return Double.parseDouble(matterValue) < Double.parseDouble(treeNodeLineVO.getRuleLimitValue());
            case Constants.RuleLimitType.GE:
                return Double.parseDouble(matterValue) >= Double.parseDouble(treeNodeLineVO.getRuleLimitValue());
            case Constants.RuleLimitType.LE:
                return Double.parseDouble(matterValue) <= Double.parseDouble(treeNodeLineVO.getRuleLimitValue());
            default:
                return false;
        }
    }
}
