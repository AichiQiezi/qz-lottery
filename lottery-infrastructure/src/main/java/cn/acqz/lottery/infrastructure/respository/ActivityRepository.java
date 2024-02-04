package cn.acqz.lottery.infrastructure.respository;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.acqz.lottery.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.acqz.lottery.domain.activity.model.req.PartakeReq;
import cn.acqz.lottery.domain.activity.model.res.StockResult;
import cn.acqz.lottery.domain.activity.model.vo.*;
import cn.acqz.lottery.domain.activity.repository.IActivityRepository;
import cn.acqz.lottery.infrastructure.dao.*;
import cn.acqz.lottery.infrastructure.po.*;
import cn.acqz.lottery.infrastructure.util.RedisUtil;
import cn.hutool.core.bean.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 活动仓储服务的实现
 * @Author: qz
 * @Date: 2024/1/29
 */
@Repository
public class ActivityRepository implements IActivityRepository {

    private Logger logger = LoggerFactory.getLogger(ActivityRepository.class);

    @Resource
    private IActivityDao activityDao;
    @Resource
    private IAwardDao awardDao;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyDetailDao strategyDetailDao;
    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;
    @Resource
    private RedisUtil redisUtil;

    @Override
    public void addActivity(ActivityVO activityVO) {
        Activity activity = BeanUtil.copyProperties(activityVO, Activity.class);
        activityDao.insert(activity);
    }

    @Override
    public void addAward(List<AwardVO> awardList) {
        List<Award> req = new ArrayList<>();
        for (AwardVO awardVO : awardList) {
            Award award = new Award();
            award.setAwardId(awardVO.getAwardId());
            award.setAwardType(awardVO.getAwardType());
            award.setAwardName(awardVO.getAwardName());
            award.setAwardContent(awardVO.getAwardContent());
            req.add(award);
        }
        awardDao.insertList(req);
    }

    @Override
    public void addStrategy(StrategyVO strategy) {
        Strategy req = new Strategy();
        req.setStrategyId(strategy.getStrategyId());
        req.setStrategyDesc(strategy.getStrategyDesc());
        req.setStrategyMode(strategy.getStrategyMode());
        req.setGrantType(strategy.getGrantType());
        req.setGrantDate(strategy.getGrantDate());
        req.setExtInfo(strategy.getExtInfo());

        strategyDao.insert(req);
    }

    @Override
    public void addStrategyDetailList(List<StrategyDetailVO> strategyDetailList) {
        List<StrategyDetail> req = new ArrayList<>();
        for (StrategyDetailVO strategyDetailVO : strategyDetailList) {
            StrategyDetail strategyDetail = new StrategyDetail();
            strategyDetail.setStrategyId(strategyDetailVO.getStrategyId());
            strategyDetail.setAwardId(strategyDetailVO.getAwardId());
            strategyDetail.setAwardName(strategyDetailVO.getAwardName());
            strategyDetail.setAwardCount(strategyDetailVO.getAwardCount());
            strategyDetail.setAwardSurplusCount(strategyDetailVO.getAwardSurplusCount());
            strategyDetail.setAwardRate(strategyDetailVO.getAwardRate());
            req.add(strategyDetail);
        }
        strategyDetailDao.insertList(req);
    }

    @Override
    public boolean alterStatus(Long activityId, Enum<Constants.ActivityState> beforeState, Enum<Constants.ActivityState> afterState) {
        AlterStateVO alterStateVO = new AlterStateVO(activityId, ((Constants.ActivityState) beforeState).getCode(), ((Constants.ActivityState) afterState).getCode());
        int count = activityDao.alterState(alterStateVO);
        return 1 == count;
    }

    @Override
    public ActivityBillVO queryActivityBill(PartakeReq req) {
        Activity activity = activityDao.queryActivityById(req.getActivityId());
        if (null == activity){
            throw new RuntimeException("查询的活动不存在！");
        }

        Integer usedStockCountObj = (Integer) redisUtil.get(Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT(req.getActivityId()));

        // 查询领取次数
        UserTakeActivityCount userTakeActivityCountReq = new UserTakeActivityCount();
        userTakeActivityCountReq.setuId(req.getuId());
        userTakeActivityCountReq.setActivityId(req.getActivityId());
        UserTakeActivityCount userTakeActivityCount = userTakeActivityCountDao.queryUserTakeActivityCount(userTakeActivityCountReq);

        // 封装结果信息
        ActivityBillVO activityBillVO = new ActivityBillVO();
        activityBillVO.setuId(req.getuId());
        activityBillVO.setActivityId(req.getActivityId());
        activityBillVO.setActivityName(activity.getActivityName());
        activityBillVO.setBeginDateTime(activity.getBeginDateTime());
        activityBillVO.setEndDateTime(activity.getEndDateTime());
        activityBillVO.setTakeCount(activity.getTakeCount());
        activityBillVO.setStockCount(activity.getStockCount());
        activityBillVO.setStockSurplusCount(null == usedStockCountObj ? activity.getStockSurplusCount() : activity.getStockCount() - Integer.parseInt(String.valueOf(usedStockCountObj)));
        activityBillVO.setStrategyId(activity.getStrategyId());
        activityBillVO.setState(activity.getState());
        activityBillVO.setUserTakeLeftCount(null == userTakeActivityCount ? null : userTakeActivityCount.getLeftCount());

        return activityBillVO;
    }

    @Override
    public int subtractionActivityStock(Long activityId) {
        return activityDao.subtractionActivityStock(activityId);
    }

    @Override
    public List<ActivityVO> scanToDoActivityList(Long id) {
        List<Activity> activityList = activityDao.scanToDoActivityList(id);
        List<ActivityVO> activityVOList = new ArrayList<>(activityList.size());
        for (Activity activity : activityList) {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setActivityId(activity.getActivityId());
            activityVO.setActivityName(activity.getActivityName());
            activityVO.setBeginDateTime(activity.getBeginDateTime());
            activityVOList.add(activityVO);
        }
        return activityVOList;
    }

    @Override
    public StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount, Date endDateTime) {
        //获取库存的 key
        String stockKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT(activityId);
        //扣减库存
        Integer seckillResult = redisUtil.seckill(stockKey, stockCount);
        if (seckillResult == 0){
            return new StockResult(Constants.ResponseCode.OUT_OF_STOCK.getCode(), Constants.ResponseCode.OUT_OF_STOCK.getInfo());
        }
        // 以活动库存占用编号，生成对应加锁Key，细化锁的颗粒度
        String stockTokenKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT_TOKEN(activityId, seckillResult);
        // 使用 Redis.setNx 加一个分布式锁；以活动结束时间，设定锁的有效时间。个人占用的锁，不需要被释放。
        long milliseconds = endDateTime.getTime() - System.currentTimeMillis();
        boolean lockToken = redisUtil.setNx(stockTokenKey, milliseconds);
        if (!lockToken) {
            logger.info("抽奖活动{}用户秒杀{}扣减库存，分布式锁失败：{}", activityId, uId, stockTokenKey);
            return new StockResult(Constants.ResponseCode.ERR_TOKEN.getCode(), Constants.ResponseCode.ERR_TOKEN.getInfo());
        }
        return new StockResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), stockTokenKey, stockCount - seckillResult);
    }

    @Override
    public void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code) {
        if (null == tokenKey){
            return;
        }
        redisUtil.del(tokenKey);
    }

    @Override
    public ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req) {
        Long count = activityDao.queryActivityInfoCount(req);
        List<Activity> activityList = activityDao.queryActivityInfoList(req);
        List<ActivityVO> activityVOList = activityList.stream().map(activity -> BeanUtil.copyProperties(activity, ActivityVO.class)).collect(Collectors.toList());
        ActivityInfoLimitPageRich activityInfoLimitPageRich = new ActivityInfoLimitPageRich();
        activityInfoLimitPageRich.setCount(count);
        activityInfoLimitPageRich.setActivityVOList(activityVOList);
        return activityInfoLimitPageRich;
    }
}
