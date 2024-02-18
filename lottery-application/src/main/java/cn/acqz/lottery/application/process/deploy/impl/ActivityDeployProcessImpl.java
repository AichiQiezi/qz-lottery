package cn.acqz.lottery.application.process.deploy.impl;

import cn.acqz.lottery.application.process.deploy.IActivityDeployProcess;
import cn.acqz.lottery.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.acqz.lottery.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.acqz.lottery.domain.activity.service.deploy.IActivityDeploy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 活动部署接口；查询列表、创建活动、修改活动、删除活动(一般实际场景为逻辑删除)，如果活动的部署需要做一些逻辑校验，那么可以在这一层做编排
 * @Author: qz
 * @Date: 2024/2/6
 */
@Service
public class ActivityDeployProcessImpl implements IActivityDeployProcess {

    @Resource
    private IActivityDeploy activityDeploy;
    @Override
    public ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req) {
        return activityDeploy.queryActivityInfoLimitPage(req);
    }
}
