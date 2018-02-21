package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public Order checkOrderDetail(int oid) {
        return orderDao.getOrder(oid);
    }

    @Override
    public Order subscribe() {
        return null;
    }

    @Override
    public boolean unsubscribe(String oid) {
        return false;
    }
}
