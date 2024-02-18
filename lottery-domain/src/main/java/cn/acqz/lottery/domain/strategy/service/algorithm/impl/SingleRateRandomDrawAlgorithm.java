package cn.acqz.lottery.domain.strategy.service.algorithm.impl;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.strategy.annotation.Strategy;
import cn.acqz.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 单体概率：单项随机概率抽奖，抽到一个已经排掉的奖品则未中奖
 * @Author: qz
 * @Date: 2024/2/5
 */
@Component("singleRateRandomDrawAlgorithm")
@Strategy(strategyMode = Constants.StrategyMode.SINGLE)
public class SingleRateRandomDrawAlgorithm extends BaseAlgorithm {

    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        String[] rateTuple = super.rateTupleMap.get(strategyId);
        if (null == rateTuple) {
            throw new IllegalStateException("存放奖品概率的散列数组不存在！");
        }
        // 随机索引
        int randomVal = this.generateSecureRandomIntCode(100);
        int idx = super.hashIdx(randomVal);

        // 获取奖品 ID
        String awardId = rateTuple[idx];

        // 校验是否命中被排除的奖品的列表中，是则返回 null
        if (excludeAwardIds.contains(awardId)) {
            return null;
        }
        return awardId;
    }
}
