package cn.acqz.lottery.rpc.activity.booth;

import cn.acqz.lottery.rpc.activity.booth.req.DrawReq;
import cn.acqz.lottery.rpc.activity.booth.req.QuantificationDrawReq;
import cn.acqz.lottery.rpc.activity.booth.res.DrawRes;

/**
 * @description: 抽奖活动展台接口
 */
public interface ILotteryActivityBooth {

    /**
     * 指定活动抽奖
     * @param drawReq 请求参数
     * @return        抽奖结果
     */
    DrawRes doDraw(DrawReq drawReq);

    /**
     * 量化人群抽奖
     * @param quantificationDrawReq 请求参数
     * @return                      抽奖结果
     */
    DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq);

}
