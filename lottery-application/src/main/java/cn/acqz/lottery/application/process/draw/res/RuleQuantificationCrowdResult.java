package cn.acqz.lottery.application.process.draw.res;

import cn.acqz.lottery.common.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/1/31
 */
public class RuleQuantificationCrowdResult extends Result {
    /** 活动ID */
    private Long activityId;

    public RuleQuantificationCrowdResult(String code, String info) {
        super(code, info);
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
