package cn.acqz.lottery.infrastructure.respository;

import cn.acqz.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.acqz.lottery.domain.strategy.model.vo.AwardBriefVO;
import cn.acqz.lottery.domain.strategy.model.vo.StrategyBriefVO;
import cn.acqz.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
import cn.acqz.lottery.domain.strategy.repository.IStrategyRepository;
import cn.acqz.lottery.infrastructure.dao.IAwardDao;
import cn.acqz.lottery.infrastructure.dao.IStrategyDao;
import cn.acqz.lottery.infrastructure.dao.IStrategyDetailDao;
import cn.acqz.lottery.infrastructure.po.Award;
import cn.acqz.lottery.infrastructure.po.Strategy;
import cn.acqz.lottery.infrastructure.po.StrategyDetail;
import cn.acqz.lottery.infrastructure.util.RedisUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 策略表仓储服务
 * @Author: qz
 * @Date: 2024/2/5
 */
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyDetailDao strategyDetailDao;

    @Resource
    private IAwardDao awardDao;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 策略配置可以提前初始化到，内存里。
     * 可以构建多级缓存，redis -> nginx -> jvm
     *
     * @param strategyId 策略ID
     * @return StrategyRich
     */
    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {
        String value = (String) redisUtil.get(String.valueOf(strategyId));
        if (StrUtil.isNotEmpty(value)) {
            return JSONUtil.toBean(value, StrategyRich.class);
        }
        synchronized (this) {
            value = (String) redisUtil.get(String.valueOf(strategyId));
            if (StrUtil.isNotEmpty(value)) {
                return JSONUtil.toBean(value, StrategyRich.class);
            }
            Strategy strategy = strategyDao.queryStrategy(strategyId);
            StrategyBriefVO strategyBriefVO = new StrategyBriefVO();
            strategyBriefVO.setStrategyId(strategy.getStrategyId());
            strategyBriefVO.setStrategyDesc(strategy.getStrategyDesc());
            strategyBriefVO.setStrategyMode(strategy.getStrategyMode());
            strategyBriefVO.setGrantType(strategy.getGrantType());
            strategyBriefVO.setGrantDate(strategy.getGrantDate());
            strategyBriefVO.setExtInfo(strategy.getExtInfo());
            StrategyRich strategyRich = new StrategyRich();

            List<StrategyDetail> strategyDetailList = strategyDetailDao.queryStrategyDetailList(strategyId);
            List<StrategyDetailBriefVO> strategyDetailBriefVOList = this.getStrategyDetailBriefVOS(strategyDetailList);
            strategyRich.setStrategy(strategyBriefVO);
            strategyRich.setStrategyDetailList(strategyDetailBriefVOList);
            redisUtil.set(String.valueOf(strategyId), JSONUtil.toJsonPrettyStr(strategyRich));
            return strategyRich;
        }
    }

    private static List<StrategyDetailBriefVO> getStrategyDetailBriefVOS(List<StrategyDetail> strategyDetailList) {
        List<StrategyDetailBriefVO> strategyDetailBriefVOList = new ArrayList<>(strategyDetailList.size());
        for (StrategyDetail strategyDetail : strategyDetailList) {
            StrategyDetailBriefVO strategyDetailBriefVO = new StrategyDetailBriefVO();
            strategyDetailBriefVO.setStrategyId(strategyDetail.getStrategyId());
            strategyDetailBriefVO.setAwardId(strategyDetail.getAwardId());
            strategyDetailBriefVO.setAwardName(strategyDetail.getAwardName());
            strategyDetailBriefVO.setAwardCount(strategyDetail.getAwardCount());
            strategyDetailBriefVO.setAwardSurplusCount(strategyDetail.getAwardSurplusCount());
            strategyDetailBriefVO.setAwardRate(strategyDetail.getAwardRate());
            strategyDetailBriefVOList.add(strategyDetailBriefVO);
        }
        return strategyDetailBriefVOList;
    }

    @Override
    public AwardBriefVO queryAwardInfo(String awardId) {
        Award award = awardDao.queryAwardInfo(awardId);

        AwardBriefVO awardBriefVO = new AwardBriefVO();
        awardBriefVO.setAwardId(award.getAwardId());
        awardBriefVO.setAwardType(award.getAwardType());
        awardBriefVO.setAwardName(award.getAwardName());
        awardBriefVO.setAwardContent(award.getAwardContent());

        return awardBriefVO;
    }

    @Override
    public List<String> queryNoStockStrategyAwardList(Long strategyId) {
        return strategyDetailDao.queryNoStockStrategyAwardList(strategyId);
    }

    @Override
    public boolean deductStock(Long strategyId, String awardId) {
        StrategyDetail req = new StrategyDetail();
        req.setStrategyId(strategyId);
        req.setAwardId(awardId);
        int count = strategyDetailDao.deductStock(req);
        return count == 1;
    }

}
