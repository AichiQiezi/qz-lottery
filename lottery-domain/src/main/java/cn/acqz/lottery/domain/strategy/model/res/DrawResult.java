package cn.acqz.lottery.domain.strategy.model.res;

import cn.acqz.lottery.common.Constants;
import cn.acqz.lottery.domain.strategy.model.vo.DrawAwardVO;

/**
 * @Description: 抽奖结果
 * @Author: qz
 * @Date: 2024/2/5
 */
public class DrawResult {
    /**
     * 用户 ID
     */
    private String uId;
    /**
     * 策略 ID
     */
    private Long strategyId;
    /**
     * 中奖状态：0 未中奖、1 已中奖、2 兜底奖
     */
    private Integer drawState = Constants.DrawState.FAIL.getCode();
    /**
     * 中奖奖品信息
     */
    private DrawAwardVO drawAwardInfo;

    public DrawResult() {
    }

    public DrawResult(String uId, Long strategyId, Integer drawState) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
    }

    public DrawResult(String uId, Long strategyId, Integer drawState, DrawAwardVO drawAwardInfo) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
        this.drawAwardInfo = drawAwardInfo;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getDrawState() {
        return drawState;
    }

    public void setDrawState(Integer drawState) {
        this.drawState = drawState;
    }

    public DrawAwardVO getDrawAwardInfo() {
        return drawAwardInfo;
    }

    public void setDrawAwardInfo(DrawAwardVO drawAwardInfo) {
        this.drawAwardInfo = drawAwardInfo;
    }

}
