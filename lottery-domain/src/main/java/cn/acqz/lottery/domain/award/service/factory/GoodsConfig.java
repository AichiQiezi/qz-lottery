package cn.acqz.lottery.domain.award.service.factory;

import cn.acqz.lottery.domain.award.service.goods.IDistributionGoods;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/1/31
 */
public class GoodsConfig {

    /** 奖品发放策略组 */
    protected static Map<String, IDistributionGoods> goodsMap = new HashMap<>();

}
