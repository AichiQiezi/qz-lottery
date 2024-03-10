package cn.acqz.lottery.domain.activity.service.deploy.impl;

import cn.acqz.lottery.domain.activity.model.aggregates.ActivityConfigRich;
import cn.acqz.lottery.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.acqz.lottery.domain.activity.model.req.ActivityConfigReq;
import cn.acqz.lottery.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.acqz.lottery.domain.activity.model.vo.ActivityVO;
import cn.acqz.lottery.domain.activity.model.vo.AwardVO;
import cn.acqz.lottery.domain.activity.model.vo.StrategyDetailVO;
import cn.acqz.lottery.domain.activity.model.vo.StrategyVO;
import cn.acqz.lottery.domain.activity.repository.IActivityRepository;
import cn.acqz.lottery.domain.activity.service.deploy.IActivityDeploy;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 部署活动配置服务
 */
@Service
public class ActivityDeployImpl implements IActivityDeploy {

    private Logger logger = LoggerFactory.getLogger(ActivityDeployImpl.class);

    @Resource
    private IActivityRepository activityRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createActivity(ActivityConfigReq req) {
        logger.info("创建活动配置开始，参数为 {}", JSON.toJSONString(req));
        ActivityConfigRich activityConfigRich = req.getActivityConfigRich();
        try {
            ActivityVO activity = activityConfigRich.getActivity();
            activityRepository.addActivity(activity);

            List<AwardVO> awardList = activityConfigRich.getAwardList();
            activityRepository.addAward(awardList);

            StrategyVO strategy = activityConfigRich.getStrategy();
            activityRepository.addStrategy(strategy);

            List<StrategyDetailVO> strategyDetailList = strategy.getStrategyDetailList();
            activityRepository.addStrategyDetailList(strategyDetailList);

        } catch (DuplicateKeyException e) {
            logger.error("创建活动失败，唯一索引冲突 activityId {} reqJson {}", req.getActivityId(), JSON.toJSONString(req), e);
            throw e;
        }
    }

    @Override
    public void updateActivity(ActivityConfigReq req) {
        //todo
    }

    @Override
    public List<ActivityVO> scanToDoActivityList(Long id) {
        return activityRepository.scanToDoActivityList(id);
    }

    @Override
    public ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req) {
        return activityRepository.queryActivityInfoLimitPage(req);
    }
}
