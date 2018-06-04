package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.BaseDao;
import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final BaseDao baseDao;

    @Autowired
    public OrderDaoImpl(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public Order getOrder(int id) {
        return (Order) baseDao.get(Order.class, id);
    }

    @Override
    public List<Order> getAllOrders() {
        return baseDao.getAllList(Order.class);
    }

    @Override
    public int saveOrder(Order order) {
        return (int) baseDao.save(order);
    }

    @Override
    public boolean updateOrder(Order order) {
        return baseDao.update(order);
    }
}
