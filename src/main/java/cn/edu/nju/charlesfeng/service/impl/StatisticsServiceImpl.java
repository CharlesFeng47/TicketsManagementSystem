package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.ConsumptionDao;
import cn.edu.nju.charlesfeng.entity.Consumption;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final ConsumptionDao consumptionDao;

    @Autowired
    public StatisticsServiceImpl(ConsumptionDao consumptionDao) {
        this.consumptionDao = consumptionDao;
    }

    @Override
    public List<Consumption> checkConsumption(User user) {
        List<Consumption> result = new LinkedList<>();

        if (user instanceof Member) {
            final String memberId = user.getId();
            for (Consumption cur : consumptionDao.getAllConsumption()) {
                if (cur.getMid().equals(memberId)) result.add(cur);
            }
        } else if ((user instanceof Spot)) {
            final String spotId = user.getId();
            for (Consumption cur : consumptionDao.getAllConsumption()) {
                if (cur.getSpot().getId().equals(spotId)) result.add(cur);
            }
        }
        return result;
    }
}
