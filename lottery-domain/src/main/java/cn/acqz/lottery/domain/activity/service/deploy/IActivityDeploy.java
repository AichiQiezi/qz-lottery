package cn.acqz.lottery.domain.activity.service.deploy;


import cn.acqz.lottery.domain.activity.model.aggregates.ActivityInfoLimitPageRich;
import cn.acqz.lottery.domain.activity.model.req.ActivityConfigReq;
import cn.acqz.lottery.domain.activity.model.req.ActivityInfoLimitPageReq;
import cn.acqz.lottery.domain.activity.model.vo.ActivityVO;

import java.util.List;

/**
 * @description: 部署活动配置接口
 */
public interface IActivityDeploy {

    /**
     * 创建活动信息
     *
     * @param req 活动配置信息
     */
    void createActivity(ActivityConfigReq req);

    /**
     * 修改活动信息
     *
     * @param req 活动配置信息
     */
    void updateActivity(ActivityConfigReq req);

    /**
     * 扫描待处理的活动列表，状态为：通过、活动中
     * <p>
     * 通过 -> 时间符合时 -> 活动中
     * 活动中 -> 时间到期时 -> 关闭
     *
     * @param id ID，无业务含义
     * @return 待处理的活动集合
     */
    List<ActivityVO> scanToDoActivityList(Long id);

    /**
     * 查询活动分页查询聚合对象
     *
     * @param req 请求参数；分页、活动
     * @return    查询结果
     */
    ActivityInfoLimitPageRich queryActivityInfoLimitPage(ActivityInfoLimitPageReq req);

}
