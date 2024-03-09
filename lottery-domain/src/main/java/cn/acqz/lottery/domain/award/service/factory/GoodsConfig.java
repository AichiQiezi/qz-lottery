package cn.acqz.lottery.domain.award.service.factory;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.award.service.goods.IDistributionGoods;
import cn.acqz.lottery.domain.award.service.goods.impl.CouponGoods;
import cn.acqz.lottery.domain.award.service.goods.impl.DescGoods;
import cn.acqz.lottery.domain.award.service.goods.impl.PhysicalGoods;
import cn.acqz.lottery.domain.award.service.goods.impl.RedeemCodeGoods;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/1/31
 */
public class GoodsConfig {

    /** 奖品发放策略组 */
    protected static Map<Integer, IDistributionGoods> goodsMap = new HashMap<>();

    @Resource
    private DescGoods descGoods;

    @Resource
    private RedeemCodeGoods redeemCodeGoods;

    @Resource
    private CouponGoods couponGoods;

    @Resource
    private PhysicalGoods physicalGoods;

    @PostConstruct
    public void init(){
        goodsMap.put(Constants.AwardType.DESC.getCode(),descGoods);
        goodsMap.put(Constants.AwardType.RedeemCodeGoods.getCode(),redeemCodeGoods);
        goodsMap.put(Constants.AwardType.CouponGoods.getCode(),couponGoods);
        goodsMap.put(Constants.AwardType.PhysicalGoods.getCode(),physicalGoods);
    }

}
