package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.dao.*;
import cn.edu.nju.charlesfeng.entity.*;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.OrderSeatHelper;
import cn.edu.nju.charlesfeng.util.enums.*;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final ScheduleDao scheduleDao;

    private final UserDao userDao;

    private final AlipayDao alipayDao;

    private final ConsumptionDao consumptionDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ScheduleDao scheduleDao, UserDao userDao, AlipayDao alipayDao, ConsumptionDao consumptionDao) {
        this.orderDao = orderDao;
        this.scheduleDao = scheduleDao;
        this.userDao = userDao;
        this.alipayDao = alipayDao;
        this.consumptionDao = consumptionDao;
    }

    @Override
    public int subscribe(User curUser, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
                         String choseSeatsJson, OrderWay orderWay, boolean onSpotIsMember, String onSpotMemberId,
                         boolean didUseCoupon, Coupon usedCoupon, String calProcess, double totalPrice)
            throws UserNotExistException, InteriorWrongException {

        Schedule toOrderSchedule = scheduleDao.getSchedule(scheduleId);

        Order order = new Order();
        order.setSchedule(toOrderSchedule);
        order.setOrderWay(orderWay);
        order.setOrderState(OrderState.ORDERED);
        order.setOrderType(orderType);
        order.setOrderTime(LocalDateTime.now());
        order.setCalProcess(calProcess);
        order.setTotalPrice(totalPrice);

        if (orderType == OrderType.CHOOSE_SEATS) {
            order.setOrderedSeatsJson(choseSeatsJson);

            List<Seat> curbBookedSeats = JSON.parseArray(choseSeatsJson, Seat.class);
            List<String> curBookedIds = OrderSeatHelper.getSeatListIds(curbBookedSeats);

            // 对schedule进行处理，预定的座位更新
            List<String> alreadyBookedIds = JSON.parseArray(toOrderSchedule.getBookedSeatsIdJson(), String.class);
            alreadyBookedIds.addAll(curBookedIds);
            toOrderSchedule.setBookedSeatsIdJson(JSON.toJSONString(alreadyBookedIds));

            List<String> curRemainSeatMap = JSON.parseArray(toOrderSchedule.getRemainSeatsJson(), String.class);
            for (String id : curBookedIds) {
                String[] idParts = id.split("_");
                int rowIndex = Integer.parseInt(idParts[0]) - 1;
                int colIndex = Integer.parseInt(idParts[1]) - 1;

                curRemainSeatMap.set(rowIndex,
                        OrderSeatHelper.orderSpecificSeat(curRemainSeatMap.get(rowIndex), colIndex));
            }
            toOrderSchedule.setRemainSeatsJson(JSON.toJSONString(curRemainSeatMap));
            // 因为此时此order还未被存储，所以不受级联更新
            scheduleDao.updateSchedule(toOrderSchedule);

        } else if (orderType == OrderType.NOT_CHOOSE_SEATS) {
            // 对此非选座购票处理，找到此座位的表示字符
            Map<SeatInfo, Double> priceMap = JSON.parseObject(toOrderSchedule.getSeatInfoPricesJson(), new TypeReference<HashMap<SeatInfo, Double>>() {
            });
            for (SeatInfo tempSeatInfo : priceMap.keySet()) {
                if (tempSeatInfo.getSeatName().equals(notChoseSeats.getSeatName())) {
                    notChoseSeats.setSeatChar(tempSeatInfo.getSeatChar());
                    break;
                }
            }

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

        return orderDao.saveOrder(order);
    }

    @Override
    public boolean payOrder(Member member, int oid, String paymentId, String paymentPwd) throws AlipayWrongPwdException, AlipayBalanceNotAdequateException {
        AlipayEntity alipayEntity = alipayDao.getAlipayEntity(paymentId);
        if (alipayEntity != null && alipayEntity.getPwd().equals(paymentPwd)) {
            Order toPay = orderDao.getOrder(oid);
            final double payMoney = toPay.getTotalPrice();

            // 检查支付宝余额是否充足
            final double remainBalance = alipayEntity.getBalance() - payMoney;
            if (remainBalance < 0) {
                throw new AlipayBalanceNotAdequateException();
            }

            // 会员支付宝减少余额
            alipayEntity.setBalance(remainBalance);
            alipayDao.update(alipayEntity);
            // 场馆未结算部分余额增加，级联更新
            Schedule curSchedule = toPay.getSchedule();
            curSchedule.setBalance(curSchedule.getBalance() + payMoney);

            // 增加一条消费记录
            consumptionDao.saveConsumption(new Consumption
                    (ConsumptionType.SUBSCRIBE, toPay.getMember().getId(), toPay.getSchedule().getSpot(), toPay.getTotalPrice()));

            // 订单状态改变
            toPay.setOrderState(OrderState.PAYED);
            return orderDao.updateOrder(toPay);
        } else {
            throw new AlipayWrongPwdException();
        }
    }

    @Override
    public boolean checkTicket(int oid) throws TicketHasBeenCheckedException, TicketStateWrongException, OrderNotExistException {
        Order toCheck = orderDao.getOrder(oid);
        if (toCheck == null) throw new OrderNotExistException();

        final OrderState curState = toCheck.getOrderState();

        if (curState == OrderState.PAYED) {
            toCheck.setOrderState(OrderState.CHECKED);

            // 积分增加
            Member curMember = toCheck.getMember();
            final double addedCredit = toCheck.getTotalPrice();
            curMember.setCreditTotal(curMember.getCreditTotal() + addedCredit);
            curMember.setCreditRemain(curMember.getCreditRemain() + addedCredit);
            userDao.updateUser(curMember, UserType.MEMBER);
        } else if (curState == OrderState.CHECKED) throw new TicketHasBeenCheckedException();
        else throw new TicketStateWrongException();
        return orderDao.updateOrder(toCheck);
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
    public boolean unsubscribe(Member member, int oid, String paymentId) throws InteriorWrongException, OrderNotRefundableException, AlipayEntityNotExistException {
        Order toUnsubscribe = orderDao.getOrder(oid);
        if (!toUnsubscribe.getMember().getId().equals(member.getId())) throw new InteriorWrongException();

        final LocalDateTime now = LocalDateTime.now();
        // 只有已支付和配票失败的订单可以退票
        final OrderState curOrderState = toUnsubscribe.getOrderState();
        if (curOrderState == OrderState.PAYED || curOrderState == OrderState.DISPATCH_FAIL) {
            // 已支付的订单只能在开始前退票
            if (curOrderState == OrderState.PAYED && now.isAfter(toUnsubscribe.getSchedule().getStartDateTime()))
                throw new OrderNotRefundableException();
        } else {
            throw new OrderNotRefundableException();
        }

        // 可以退款
        toUnsubscribe.setOrderState(OrderState.REFUND);

        AlipayEntity buyerAE = alipayDao.getAlipayEntity(paymentId);
        if (buyerAE == null) throw new AlipayEntityNotExistException();

        // 会员款项增加
        final double unsubscribeMoney = toUnsubscribe.getTotalPrice() * getRefundPercent(now, toUnsubscribe.getSchedule().getStartDateTime());
        buyerAE.setBalance(buyerAE.getBalance() + unsubscribeMoney);
        alipayDao.update(buyerAE);
        // 场馆未结算部分余额减少，级联更新
        Schedule curSchedule = toUnsubscribe.getSchedule();
        curSchedule.setBalance(curSchedule.getBalance() - unsubscribeMoney);

        // 增加一条消费记录
        consumptionDao.saveConsumption(new Consumption
                (ConsumptionType.UNSUBSCRIBE, toUnsubscribe.getMember().getId(), toUnsubscribe.getSchedule().getSpot(), toUnsubscribe.getTotalPrice()));


        // 对schedule进行处理，预定的座位被释放
        toUnsubscribe = OrderSeatHelper.releaseSeatsInOrder(toUnsubscribe);
        return orderDao.updateOrder(toUnsubscribe);
    }

    /**
     * 根据参数决定是否要更新买家的优惠券，如果需要就更新
     */
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

    /**
     * 根据据订单中计划开始的时间时限确定退款的比例
     */
    private double getRefundPercent(LocalDateTime now, LocalDateTime scheduleTime) {
        // TODO 不同时限退款不同
        return 1;
    }
}
