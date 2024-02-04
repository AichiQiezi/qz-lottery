package cn.acqz.lottery.domain.award.repository;


/**
 * @Description: 奖品表仓储服务接口
 * @Author: qz
 * @Date: 2024/1/29
 */
public interface IOrderRepository {

    /**
     * 更新奖品发放状态
     *
     * @param uId               用户ID
     * @param orderId           订单ID
     * @param awardId           奖品ID
     * @param grantState        奖品状态
     */
    void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState);

}
