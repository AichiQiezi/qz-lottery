package cn.acqz.lottery.rpc.activity.booth.res;

import cn.acqz.lottery.common.Result;
import cn.acqz.lottery.rpc.activity.booth.dto.AwardDTO;

import java.io.Serializable;

/**
 * @description: 抽奖结果
 */
public class DrawRes extends Result implements Serializable {

    private AwardDTO awardDTO;

    public DrawRes(String code, String info) {
        super(code, info);
    }

    public AwardDTO getAwardDTO() {
        return awardDTO;
    }

    public void setAwardDTO(AwardDTO awardDTO) {
        this.awardDTO = awardDTO;
    }

}
