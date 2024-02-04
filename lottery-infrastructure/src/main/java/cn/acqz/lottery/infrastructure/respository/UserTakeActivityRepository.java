package cn.acqz.lottery.infrastructure.respository;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import cn.acqz.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.acqz.lottery.domain.activity.model.vo.InvoiceVO;
import cn.acqz.lottery.domain.activity.model.vo.UserTakeActivityVO;
import cn.acqz.lottery.domain.activity.repository.IUserTakeActivityRepository;
import cn.acqz.lottery.infrastructure.dao.IUserTakeActivityCountDao;
import cn.acqz.lottery.infrastructure.dao.IUserTakeActivityDao;
import cn.acqz.lottery.infrastructure.po.UserTakeActivity;
import cn.acqz.lottery.infrastructure.po.UserTakeActivityCount;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description: 用户参与活动仓储
 * @Author: qz
 * @Date: 2024/1/30
 */
@Repository
public class UserTakeActivityRepository implements IUserTakeActivityRepository {

    @Resource
    private IUserTakeActivityDao userTakeActivityDao;
    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;

    @Override
    public int subtractionLeftCount(Long activityId, String activityName, Integer takeCount, Integer userTakeLeftCount, String uId) {
        if (null == userTakeLeftCount){
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setuId(uId);
            userTakeActivityCount.setActivityId(activityId);
            userTakeActivityCount.setTotalCount(takeCount);
            userTakeActivityCount.setLeftCount(takeCount - 1);
            userTakeActivityCountDao.insert(userTakeActivityCount);
            return 1;
        }else {
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setuId(uId);
            userTakeActivityCount.setActivityId(activityId);
            return userTakeActivityCountDao.updateLeftCount(userTakeActivityCount);
        }
    }

    @Override
    public void takeActivity(Long activityId, String activityName, Long strategyId, Integer takeCount, Integer userTakeLeftCount, String uId, Date takeDate, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setuId(uId);
        userTakeActivity.setTakeId(takeId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setActivityName(activityName);
        userTakeActivity.setTakeDate(takeDate);
        if (null == userTakeLeftCount) {
            userTakeActivity.setTakeCount(1);
        } else {
            userTakeActivity.setTakeCount(takeCount - userTakeLeftCount + 1);
        }
        userTakeActivity.setStrategyId(strategyId);
        userTakeActivity.setState(Constants.TaskState.NO_USED.getCode());
        String uuid = uId + "_" + activityId + "_" + userTakeActivity.getTakeCount();
        userTakeActivity.setUuid(uuid);

        userTakeActivityDao.insert(userTakeActivity);
    }

    @Override
    public int lockTackActivity(String uId, Long activityId, Long takeId) {
        return 0;
    }

    @Override
    public void saveUserStrategyExport(DrawOrderVO drawOrder) {

    }

    @Override
    public UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId) {
        return null;
    }

    @Override
    public void updateInvoiceMqState(String uId, Long orderId, Integer mqState) {

    }

    @Override
    public List<InvoiceVO> scanInvoiceMqState() {
        return null;
    }

    @Override
    public void updateActivityStock(ActivityPartakeRecordVO activityPartakeRecordVO) {

    }
}
