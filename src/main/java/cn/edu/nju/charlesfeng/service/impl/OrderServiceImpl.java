package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final ScheduleDao scheduleDao;

    private final UserDao userDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ScheduleDao scheduleDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
    }

    @Override
    public Order subscribe(Member member, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
                           String choseSeatsJson, Coupon usedCoupon, double totalPrice) {
        Order order = new Order();
        order.setMember(member);
        order.setSchedule(scheduleDao.getSchedule(scheduleId));
        order.setOrderState(OrderState.ORDERED);
        order.setOrderType(orderType);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalPrice(totalPrice);

        if (orderType == OrderType.CHOOSE_SEATS) {
            order.setOrderedSeatsJson(choseSeatsJson);
        } else if (orderType == OrderType.NOT_CHOOSE_SEATS) {
            order.setNotChoseSeats(notChoseSeats);
            notChoseSeats.setOrder(order);
        }

        // 不是 Formatter 初始化的默认值，则需要处理优惠券信息
        if (usedCoupon.getId() != -1) {
            List<Coupon> memberCoupons = member.getCoupons();

            // 找到需要移除的
            Coupon neededToRemove = null;
            for (Coupon coupon : memberCoupons) {
                if (coupon.getId() == usedCoupon.getId()) {
                    neededToRemove = coupon;
                    break;
                }
            }

            if (neededToRemove != null) {
                memberCoupons.remove(neededToRemove);
                userDao.updateUser(member, UserType.MEMBER);
            }
        }
        orderDao.saveOrder(order);
        return order;
    }

    @Override
    public List<Order> getMyOrders(String mid) {
        List<Order> result = new LinkedList<>();
        for (Order curOrder : orderDao.getAllOrders()) {
            if (curOrder.getMember().getId().equals(mid)) {
                result.add(curOrder);
            }
        }
        return result;
    }

    @Override
    public Order checkOrderDetail(int oid) {
        return orderDao.getOrder(oid);
    }

    @Override
    public boolean unsubscribe(String oid) {
        return false;
    }
}
