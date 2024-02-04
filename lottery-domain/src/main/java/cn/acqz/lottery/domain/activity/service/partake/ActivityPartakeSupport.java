package cn.acqz.lottery.domain.activity.service.partake;

import cn.acqz.lottery.domain.activity.model.req.PartakeReq;
import cn.acqz.lottery.domain.activity.model.vo.ActivityBillVO;
import cn.acqz.lottery.domain.activity.repository.IActivityRepository;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: qz
 * @Date: 2024/1/30
 */
public class ActivityPartakeSupport {

    @Resource
    private IActivityRepository activityRepository;

    /**
     * 查询活动账单信息【库存、状态、日期、个人参与次数】
     *
     * @param req 参与活动请求
     * @return 活动账单
     */
    protected ActivityBillVO queryActivityBill(PartakeReq req){
        return activityRepository.queryActivityBill(req);
    }
}
