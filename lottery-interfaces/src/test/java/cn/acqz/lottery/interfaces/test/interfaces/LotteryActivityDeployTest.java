package cn.acqz.lottery.interfaces.test.interfaces;

import cn.acqz.lottery.rpc.activity.deploy.ILotteryActivityDeploy;
import cn.acqz.lottery.rpc.activity.deploy.req.ActivityPageReq;
import cn.acqz.lottery.rpc.activity.deploy.res.ActivityRes;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @description: 活动部署测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryActivityDeployTest {

    private Logger logger = LoggerFactory.getLogger(LotteryActivityDeployTest.class);

    @Resource
    private ILotteryActivityDeploy lotteryActivityDeploy;

    @Test
    public void test_queryActivityListByPageForErp() {
        ActivityPageReq req = new ActivityPageReq(1, 10);
        req.setErpId("xiaofuge");

        ActivityRes res = lotteryActivityDeploy.queryActivityListByPageForErp(req);

        logger.info("请求参数：{}", JSON.toJSONString(req));
        logger.info("测试结果：{}", JSON.toJSONString(res));
    }

}
