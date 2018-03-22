package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.AlipayDao;
import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.entity.AlipayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlipayDaoImpl implements AlipayDao {

    private final BaseDao baseDao;

    @Autowired
    public AlipayDaoImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public AlipayEntity getAlipayEntity(String aliId) {
        return (AlipayEntity) baseDao.get(AlipayEntity.class, aliId);
    }

    @Override
    public boolean update(AlipayEntity alipayEntity) {
        return baseDao.update(alipayEntity);
    }
}
