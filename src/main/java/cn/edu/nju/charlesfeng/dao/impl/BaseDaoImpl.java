package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.BaseDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class BaseDaoImpl implements BaseDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public BaseDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Object get(Class c, Object id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Object result = session.find(c, id);
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public List getAllList(Class c) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        String hql = "from " + c.getName();
        List result = session.createQuery(hql).getResultList();
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public Serializable save(Object bean) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Serializable result = session.save(bean);
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public boolean update(Object bean) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.merge(bean);
        tx.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(Class c, Object id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Object obj = session.get(c, (Serializable) id);
        session.delete(obj);
        tx.commit();
        session.close();
        return true;
    }
}
