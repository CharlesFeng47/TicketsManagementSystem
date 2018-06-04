package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.AlipayDao;
import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.model.AlipayAccount;
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
    public AlipayAccount getAlipayEntity(String aliId) {
        return (AlipayAccount) baseDao.get(AlipayAccount.class, aliId);
    }

    @Override
    public boolean update(AlipayAccount alipayAccount) {
        return baseDao.update(alipayAccount);
    }
}
