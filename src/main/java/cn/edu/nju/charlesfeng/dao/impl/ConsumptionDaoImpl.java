package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.dao.ConsumptionDao;
import cn.edu.nju.charlesfeng.entity.Consumption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConsumptionDaoImpl implements ConsumptionDao {

    private final BaseDao baseDao;

    @Autowired
    public ConsumptionDaoImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Consumption getConsumption(int id) {
        return (Consumption) baseDao.get(Consumption.class, id);
    }

    @Override
    public List<Consumption> getAllConsumption() {
        return baseDao.getAllList(Consumption.class);
    }

    @Override
    public int saveConsumption(Consumption consumption) {
        return (int) baseDao.save(consumption);
    }
}
