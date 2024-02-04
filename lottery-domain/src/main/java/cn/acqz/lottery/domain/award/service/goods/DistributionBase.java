package cn.acqz.lottery.domain.award.service.goods;

import cn.acqz.lottery.domain.award.repository.IOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @Description: 商品发送基础共用类
 * @Author: qz
 * @Date: 2024/1/31
 */
public class DistributionBase {

    protected Logger logger = LoggerFactory.getLogger(DistributionBase.class);

    @Resource
    private IOrderRepository awardRepository;

    protected void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        logger.info("修改商品发送状态");
        awardRepository.updateUserAwardState(uId, orderId, awardId, grantState);
    }

}
