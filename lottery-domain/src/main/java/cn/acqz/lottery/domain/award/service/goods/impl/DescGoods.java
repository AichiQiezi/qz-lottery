package cn.acqz.lottery.domain.award.service.goods.impl;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.award.model.req.GoodsReq;
import cn.acqz.lottery.domain.award.model.res.DistributionRes;
import cn.acqz.lottery.domain.award.service.goods.DistributionBase;
import cn.acqz.lottery.domain.award.service.goods.IDistributionGoods;
import org.springframework.stereotype.Component;

/**
 * @Description: 描述类商品，以文字形式展示给用户
 * @Author: qz
 * @Date: 2024/1/31
 */
@Component
public class DescGoods extends DistributionBase implements IDistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        super.updateUserAwardState(req.getuId(), req.getOrderId(), req.getAwardId(), Constants.GrantState.COMPLETE.getCode());

        return new DistributionRes(req.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

}
