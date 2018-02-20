package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order getOrder(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Order result = session.find(Order.class, id);
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public int saveOrder(Order order) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        int oid = (int) session.save(order);
        tx.commit();
        session.close();
        return oid;
    }

    @Override
    public boolean updateOrder(Order order) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(order);
        tx.commit();
        session.close();
        return true;
    }
}
