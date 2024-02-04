package cn.acqz.lottery.domain.award.service.goods.impl;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.award.model.req.GoodsReq;
import cn.acqz.lottery.domain.award.model.res.DistributionRes;
import cn.acqz.lottery.domain.award.service.goods.DistributionBase;
import cn.acqz.lottery.domain.award.service.goods.IDistributionGoods;
import org.springframework.stereotype.Component;

/**
 * @Description: 优惠券商品
 * @Author: qz
 * @Date: 2024/1/31
 */
@Component
public class CouponGoods extends DistributionBase implements IDistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        // 模拟调用优惠券发放接口
        logger.info("模拟调用优惠券发放接口 uId：{} awardContent：{}", req.getuId(), req.getAwardContent());

        // 更新用户领奖结果
        super.updateUserAwardState(req.getuId(), req.getOrderId(), req.getAwardId(), Constants.GrantState.COMPLETE.getCode());

        return new DistributionRes(req.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

}