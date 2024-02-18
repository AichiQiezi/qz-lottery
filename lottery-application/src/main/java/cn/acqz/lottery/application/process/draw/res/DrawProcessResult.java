package cn.acqz.lottery.application.process.draw.res;

import cn.acqz.lottery.common.Result;
import cn.acqz.lottery.domain.strategy.model.vo.DrawAwardVO;

/**
 * @Description: 活动抽奖结果
 * @Author: qz
 * @Date: 2024/1/31
 */
public class DrawProcessResult extends Result {
    private DrawAwardVO drawAwardVO;

    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardVO drawAwardVO) {
        super(code, info);
        this.drawAwardVO = drawAwardVO;
    }

    public DrawAwardVO getDrawAwardVO() {
        return drawAwardVO;
    }

    public void setDrawAwardVO(DrawAwardVO drawAwardVO) {
        this.drawAwardVO = drawAwardVO;
    }

}