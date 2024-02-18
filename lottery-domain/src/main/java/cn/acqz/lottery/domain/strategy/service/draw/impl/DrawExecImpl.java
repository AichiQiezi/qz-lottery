package cn.acqz.lottery.domain.strategy.service.draw.impl;

import cn.acqz.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.acqz.lottery.domain.strategy.service.draw.AbstractDrawBase;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 抽奖过程方法实现
 * @Author: qz
 * @Date: 2024/2/5
 */
@Service("drawExec")
public class DrawExecImpl extends AbstractDrawBase {
    private Logger logger = LoggerFactory.getLogger(DrawExecImpl.class);

    @Override
    protected String drawAlgorithm(Long strategyId, IDrawAlgorithm drawAlgorithm, List<String> excludeAwardIds) {
        // 执行抽奖
        String awardId = drawAlgorithm.randomDraw(strategyId, excludeAwardIds);

        // 判断抽奖结果
        if (null == awardId){
            return null;
        }

        // 扣减库存
        boolean isSuccess = strategyRepository.deductStock(strategyId, awardId);

        return isSuccess ? awardId : null;
    }

    @Override
    protected List<String> queryExcludeAwardIds(Long strategyId) {
        List<String> awardList = strategyRepository.queryNoStockStrategyAwardList(strategyId);
        logger.info("执行抽奖策略 strategyId：{}，无库存排除奖品列表ID集合 awardList：{}", strategyId, JSON.toJSONString(awardList));
        return awardList;
    }
}
