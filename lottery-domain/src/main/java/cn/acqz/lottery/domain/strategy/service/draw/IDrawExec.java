package cn.acqz.lottery.domain.strategy.service.draw;

import cn.acqz.lottery.domain.strategy.model.req.DrawReq;
import cn.acqz.lottery.domain.strategy.model.res.DrawResult;

/**
 * @Description: 抽奖执行接口
 * @Author: qz
 * @Date: 2024/2/5
 */
public interface IDrawExec {
    /**
     * 抽奖方法
     * @param req 抽奖参数；用户ID、策略ID、防重ID
     * @return    中奖结果
     */
    DrawResult doDrawExec(DrawReq req);
}
