package cn.acqz.lottery.domain.rule.service.engine;

import cn.acqz.lottery.domain.rule.service.logic.LogicFilter;
import cn.acqz.lottery.domain.rule.service.logic.impl.UserAgeFilter;
import cn.acqz.lottery.domain.rule.service.logic.impl.UserGenderFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 规则配置
 * @Author: qz
 * @Date: 2024/2/4
 */
public class EngineConfig {

    protected static Map<String, LogicFilter> logicFilterMap = new ConcurrentHashMap<>();
    @Resource
    private UserAgeFilter userAgeFilter;
    @Resource
    private UserGenderFilter userGenderFilter;

    @PostConstruct
    public void init() {
        logicFilterMap.put("userAge", userAgeFilter);
        logicFilterMap.put("userGender", userGenderFilter);
    }
}
