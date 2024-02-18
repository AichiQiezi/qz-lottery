package cn.acqz.lottery.interfaces.test;

import cn.acqz.lottery.domain.activity.model.vo.StrategyVO;
import cn.acqz.lottery.domain.strategy.model.vo.DrawAwardVO;
import cn.acqz.lottery.infrastructure.po.Strategy;
import cn.acqz.lottery.rpc.activity.booth.dto.AwardDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 博客：https://acqz.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：acqz虫洞栈
 * Create by 小傅哥(fustack)
 */
public class ApiTest {

    /**
     * 抽奖策略测试
     * <p>
     * https://www.jugong.wang/random-portal/my/qa
     * https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software
     * Java 随机数生成器 Random & SecureRandom 原理分析 https://blog.csdn.net/hustspy1990/article/details/93364805
     * 使用 SecureRandom 产生随机数采坑记录 https://blog.csdn.net/weixin_41385912/article/details/103267277
     */
    @Test
    public void test_strategy() {
        SecureRandom random = new SecureRandom();
        int rate = random.nextInt(100);

        System.out.println("概率：" + rate);

        List<Map<String, String>> strategyList = new ArrayList<>();

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "一等奖：彩电");
            put("awardId", "10001");
            put("awardCount", "3");
            put("awardRate", "0.2");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "二等奖：冰箱");
            put("awardId", "10002");
            put("awardCount", "5");
            put("awardRate", "0.3");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "三等奖：洗衣机");
            put("awardId", "10003");
            put("awardCount", "10");
            put("awardRate", "0.5");
        }});

    }


    @Test
    public void test_idx() {

        Map<Integer, Integer> map = new HashMap<>();

        int HASH_INCREMENT = 0x61c88647;
        int hashCode = 0;
        for (int i = 1; i <= 100; i++) {
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int idx = hashCode & (128 - 1);

            map.merge(idx, 1, Integer::sum);

            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(i).hashCode() & (128 - 1)));
        }

        System.out.println(map);
    }

    @Test
    public void test_id() {
        String random = RandomStringUtils.randomNumeric(9);
        System.out.println(random);
    }

    @Test
    public void test_vo2dto() {

        DrawAwardVO drawAwardVO = new DrawAwardVO();

        AwardDTO awardDTO = new AwardDTO();
        awardDTO.setAwardId(drawAwardVO.getAwardId());
        awardDTO.setAwardType(drawAwardVO.getAwardType());
        awardDTO.setAwardName(drawAwardVO.getAwardName());
        awardDTO.setAwardContent(drawAwardVO.getAwardContent());
        awardDTO.setStrategyMode(drawAwardVO.getStrategyMode());
        awardDTO.setGrantType(drawAwardVO.getGrantType());
        awardDTO.setGrantDate(drawAwardVO.getGrantDate());


    }

    public void addStrategy(StrategyVO strategy) {

        User user = new User();

        Strategy req = new Strategy();

    }

    class User {
        private Long strategyId;
        private String strategyDesc;

        public Long getStrategyId() {
            return strategyId;
        }

        public void setStrategyId(Long strategyId) {
            this.strategyId = strategyId;
        }

        public String getStrategyDesc() {
            return strategyDesc;
        }

        public void setStrategyDesc(String strategyDesc) {
            this.strategyDesc = strategyDesc;
        }
    }

    public static void main(String[] args) {

//        Pattern pattern = Pattern.compile("(?i)SELECT\\s?(\\*).*?(?i)FROM\\s?(.*?);");
        Pattern pattern = Pattern.compile("(?=FROM)");
        Matcher matcher = pattern.matcher("SELECT * FROM java;");

        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }

    }

}



