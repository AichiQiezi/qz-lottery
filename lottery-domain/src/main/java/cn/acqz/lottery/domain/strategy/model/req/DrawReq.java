package cn.acqz.lottery.domain.strategy.model.req;

/**
 * @Description: 抽奖请求对象
 * @Author: qz
 * @Date: 2024/2/5
 */
public class DrawReq {
    /**
     * 用户 ID
     */
    private String uId;

    /**
     * 策略 ID
     */
    private Long strategyId;

    /**
     * 防重 ID
     */
    private String uuid;

    public DrawReq() {
    }

    public DrawReq(String uId, Long strategyId) {
        this.uId = uId;
        this.strategyId = strategyId;
    }

    public DrawReq(String uId, Long strategyId, String uuid) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
