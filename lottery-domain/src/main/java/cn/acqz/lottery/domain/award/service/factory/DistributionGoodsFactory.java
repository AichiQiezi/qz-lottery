package cn.acqz.lottery.domain.award.service.factory;
import cn.acqz.lottery.domain.award.service.goods.IDistributionGoods;
import org.springframework.stereotype.Service;

/**
 * @Description: 配送商品简单工厂，提供获取发送服务
 * @Author: qz
 * @Date: 2024/1/31
 */
@Service
public class DistributionGoodsFactory extends GoodsConfig {

    public IDistributionGoods getDistributionGoodsService(Integer awardType){
        return goodsMap.get(awardType);
    }

}
