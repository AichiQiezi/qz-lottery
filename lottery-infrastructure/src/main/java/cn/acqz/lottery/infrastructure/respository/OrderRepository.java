package cn.acqz.lottery.infrastructure.respository;

import cn.acqz.lottery.domain.award.repository.IOrderRepository;
import cn.acqz.lottery.infrastructure.dao.IUserStrategyExportDao;
import cn.acqz.lottery.infrastructure.po.UserStrategyExport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Description: 奖品表仓储服务接口实现
 * @Author: qz
 * @Date: 2024/2/5
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IUserStrategyExportDao userStrategyExportDao;

    @Override
    public void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId(uId);
        userStrategyExport.setOrderId(orderId);
        userStrategyExport.setAwardId(awardId);
        userStrategyExport.setGrantState(grantState);
        userStrategyExportDao.updateUserAwardState(userStrategyExport);
    }
}
