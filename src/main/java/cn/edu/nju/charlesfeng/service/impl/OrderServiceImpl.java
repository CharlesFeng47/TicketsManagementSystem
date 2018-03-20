package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.OrderWay;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.InteriorWrongException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
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
    public Order subscribe(User curUser, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
                           String choseSeatsJson, OrderWay orderWay, boolean onSpotIsMember, String onSpotMemberId,
                           boolean didUseCoupon, Coupon usedCoupon, double totalPrice) throws UserNotExistException, InteriorWrongException {
        Order order = new Order();
        order.setSchedule(scheduleDao.getSchedule(scheduleId));
        order.setOrderWay(orderWay);
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

        Member buyer;
        if (orderWay == OrderWay.BUY_ON_MEMBER) {
            buyer = (Member) curUser;
            order.setMember(buyer);
            order = updateCouponOfBuyer(didUseCoupon, buyer, usedCoupon, order);
        } else if (orderWay == OrderWay.BUY_ON_SPOT) {
            // 现场购买可以是非会员
            if (onSpotIsMember) {
                buyer = (Member) userDao.getUser(onSpotMemberId, UserType.MEMBER);
                order.setMember(buyer);
                order = updateCouponOfBuyer(didUseCoupon, buyer, usedCoupon, order);
            }
        } else {
            throw new InteriorWrongException();
        }

        orderDao.saveOrder(order);
        return order;
    }

    @Override
    public List<Order> getMyOrders(String mid) {
        List<Order> result = new LinkedList<>();
        for (Order curOrder : orderDao.getAllOrders()) {
            Member curMember = curOrder.getMember();
            if (curMember != null && curMember.getId().equals(mid)) {
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

    private Order updateCouponOfBuyer(boolean didUseCoupon, Member buyer, Coupon usedCoupon, Order curOrder) {
        if (didUseCoupon) {
            List<Coupon> memberCoupons = buyer.getCoupons();

            // 找到需要移除的
            Coupon neededToRemove = null;
            for (Coupon coupon : memberCoupons) {
                if (coupon.getId() == usedCoupon.getId()) {
                    neededToRemove = coupon;
                    break;
                }
            }

            // 使用的优惠券一定可以被找到
            assert neededToRemove != null;
            curOrder.setUsedCoupon(neededToRemove);

            memberCoupons.remove(neededToRemove);
            userDao.updateUser(buyer, UserType.MEMBER);
        }
        return curOrder;
    }
}
