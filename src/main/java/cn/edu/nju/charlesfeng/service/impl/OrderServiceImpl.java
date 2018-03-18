package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.model.ChoseSeat;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private ScheduleDao scheduleDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ScheduleDao scheduleDao) {
        this.orderDao = orderDao;
        this.scheduleDao = scheduleDao;
    }

    @Override
    public Order checkOrderDetail(int oid) {
        return orderDao.getOrder(oid);
    }

    @Override
    public Order subscribe(Member member, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats, String choseSeatsJson) {
        Order order = new Order();
        order.setMember(member);
        order.setSchedule(scheduleDao.getSchedule(scheduleId));
        order.setOrderState(OrderState.ORDERED);
        order.setOrderType(OrderType.CHOOSE_SEATS);
        order.setOrderTime(LocalDateTime.now());

        if (orderType == OrderType.CHOOSE_SEATS) {
            double totalPrice = 0;
            List<ChoseSeat> choseSeats = JSON.parseArray(choseSeatsJson, ChoseSeat.class);
            for (ChoseSeat cs : choseSeats) {
                totalPrice += cs.getPrice();
            }
            order.setTotalPrice(totalPrice);

            order.setOrderedSeatsJson(choseSeatsJson);
        } else if (orderType == OrderType.NOT_CHOOSE_SEATS) {
            order.setTotalPrice(notChoseSeats.getPrice());

            order.setNotChoseSeats(notChoseSeats);
            notChoseSeats.setOrder(order);
        }
        orderDao.saveOrder(order);
        return order;
    }

    @Override
    public boolean unsubscribe(String oid) {
        return false;
    }
}
