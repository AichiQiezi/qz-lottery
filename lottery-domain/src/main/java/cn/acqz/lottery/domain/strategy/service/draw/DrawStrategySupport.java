package cn.acqz.lottery.domain.strategy.service.draw;

import cn.acqz.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.acqz.lottery.domain.strategy.model.vo.AwardBriefVO;
import cn.acqz.lottery.domain.strategy.repository.IStrategyRepository;
import javax.annotation.Resource;

/**
 * @Description: 抽奖策略数据支撑，一些通用的数据服务
 * @Author: qz
 * @Date: 2024/2/5
 */
public class DrawStrategySupport extends DrawConfig {

    @Resource
    protected IStrategyRepository strategyRepository;

    /**
     * 查询策略配置信息
     *
     * @param strategyId 策略ID
     * @return 策略配置信息
     */
    protected StrategyRich queryStrategyRich(Long strategyId) {
        return strategyRepository.queryStrategyRich(strategyId);
    }

    /**
     * 查询奖品详情信息
     *
     * @param awardId 奖品ID
     * @return 中奖详情
     */
    protected AwardBriefVO queryAwardInfoByAwardId(String awardId) {
        return strategyRepository.queryAwardInfo(awardId);
    }

}
