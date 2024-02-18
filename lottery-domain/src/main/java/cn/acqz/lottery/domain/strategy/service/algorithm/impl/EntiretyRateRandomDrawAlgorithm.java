package cn.acqz.lottery.domain.strategy.service.algorithm.impl;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.strategy.annotation.Strategy;
import cn.acqz.lottery.domain.strategy.model.vo.AwardRateVO;
import cn.acqz.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 总体概率：必中奖策略抽奖，排掉已经中奖的概率，重新计算中奖范围
 * @Author: qz
 * @Date: 2024/2/5
 */
@Component("entiretyRateRandomDrawAlgorithm")
@Strategy(strategyMode = Constants.StrategyMode.ENTIRETY)
public class EntiretyRateRandomDrawAlgorithm extends BaseAlgorithm {

    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        // 区间分母
        BigDecimal differenceDenominator = BigDecimal.ZERO;

        // 总体概率，需要提前过滤掉排除的奖品 ID
        List<AwardRateVO> differenceAwardRateList = new ArrayList<>();
        List<AwardRateVO> awardRateVOList = awardRateInfoMap.get(strategyId);
        for (AwardRateVO awardRateInfo : awardRateVOList) {
            String awardId = awardRateInfo.getAwardId();
            if (excludeAwardIds.contains(awardId)) {
                continue;
            }
            differenceAwardRateList.add(awardRateInfo);
            differenceDenominator = differenceDenominator.add(awardRateInfo.getAwardRate());
        }

        // 前置校验：奖品列表为 0，直接返回 null
        if (CollectionUtils.isEmpty(awardRateVOList)) {
            return null;
        }
        // 前置校验：奖品列表为 1，直接返回奖品 ID
        if (awardRateVOList.size() == 1) {
            return awardRateVOList.get(0).getAwardId();
        }

        //随机获取概率值
        int randomVal = this.generateSecureRandomIntCode(100);

        // 循环获取奖品
        String awardId = null;
        int cursorVal = 0;
        for (AwardRateVO awardRateVO : differenceAwardRateList) {
            int rateVal = awardRateVO.getAwardRate().divide(differenceDenominator, 2, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).intValue();
            if (randomVal <= (cursorVal + randomVal)){
                awardId = awardRateVO.getAwardId();
                break;
            }
            cursorVal += rateVal;
        }

        return awardId;
    }
}
