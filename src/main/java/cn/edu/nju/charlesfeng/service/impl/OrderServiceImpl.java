package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.AlipayAccount;
import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.AlipayRepository;
import cn.edu.nju.charlesfeng.repository.OrderRepository;
import cn.edu.nju.charlesfeng.repository.TicketRepository;
import cn.edu.nju.charlesfeng.repository.UserRepository;
import cn.edu.nju.charlesfeng.service.AlipayService;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotCancelException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotPaymentException;
import cn.edu.nju.charlesfeng.util.exceptions.order.OrderNotRefundableException;
import cn.edu.nju.charlesfeng.util.exceptions.pay.AlipayBalanceNotAdequateException;
import cn.edu.nju.charlesfeng.util.exceptions.member.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.member.WrongPwdException;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    private final AlipayRepository alipayRepository;

    private final AlipayService alipayService;

    private final ProgramService programService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, AlipayRepository alipayRepository, TicketRepository ticketRepository, AlipayService alipayService, ProgramService programService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.alipayRepository = alipayRepository;
        this.ticketRepository = ticketRepository;
        this.alipayService = alipayService;
        this.programService = programService;
    }

    /**
     * @param orderID 要查看的订单ID
     * @return 订单详情
     */
    @Override
    public Order checkOrderDetail(OrderID orderID) {
        return orderRepository.findByOrderID(orderID);
    }

    /**
     * 生成订单
     *
     * @param order 订单实体
     * @return 保存结果
     */
    @Override
    public boolean generateOrder(Order order) {
        orderRepository.save(order);
        return true;
    }

    /**
     * 取消订单(未支付->已取消)
     *
     * @param orderID 订单ID
     * @return 是否成功取消
     */
    @Override
    public boolean cancelOrder(OrderID orderID) throws OrderNotCancelException {
        Order order = orderRepository.findByOrderID(orderID);
        if (!order.getOrderState().equals(OrderState.UNPAID)) {
            throw new OrderNotCancelException(ExceptionCode.ORDER_NOT_CANCEL);
        }

        order.setOrderState(OrderState.CANCELLED);
        Set<Ticket> tickets = new HashSet<>();
        for (Ticket ticket : order.getTickets()) {
            ticket.setOrder(null); //取消订单和票的关联
            ticket.setLock(false);
            tickets.add(ticket);
        }
        order.getTickets().clear();
        order.setTickets(null); //取消订单和票的关联
        ticketRepository.saveAll(tickets);
        orderRepository.save(order);
        orderRepository.deleteRelationOfTicket(orderID.getEmail(), orderID.getTime());//删除关联集合
        return true;
    }

    /**
     * 取消订单(系统调度取消)
     *
     * @param order 订单
     * @return 是否成功取消
     */
    @Override
    public boolean cancelOrderBySchedule(Order order) {
        order.setOrderState(OrderState.CANCELLED);
        Set<Ticket> tickets = new HashSet<>();
        for (Ticket ticket : order.getTickets()) {
            ticket.setOrder(null); //取消订单和票的关联
            ticket.setLock(false);
            tickets.add(ticket);
        }
        order.getTickets().clear();
        order.setTickets(null); //取消订单和票的关联
        ticketRepository.saveAll(tickets);
        orderRepository.save(order);
        orderRepository.deleteRelationOfTicket(order.getOrderID().getEmail(), order.getOrderID().getTime());//删除关联集合
        return true;
    }

    /**
     * 订单支付(未支付->已支付)
     *
     * @param orderID 订单ID
     * @return 是否成功支付
     */
    @Override
    public boolean payOrder(OrderID orderID) throws OrderNotPaymentException, UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException {
        Order order = orderRepository.findByOrderID(orderID);
        if (!order.getOrderState().equals(OrderState.UNPAID)) {
            throw new OrderNotPaymentException(ExceptionCode.ORDER_NOT_PAYMENT);
        }

        AlipayAccount from = alipayService.getUserAccount(orderID.getEmail());
        AlipayAccount to = SystemHelper.getSystemAccount();
        alipayService.transferAccounts(from.getId(), from.getPwd(), Objects.requireNonNull(to).getId(), order.getTotalPrice());
        order.setOrderState(OrderState.PAYED);
        orderRepository.save(order);
        return true;
    }

    /**
     * 订单退订(已支付->已退款)
     *
     * @param orderID 订单ID
     * @return 是否成功退订
     */
    @Override
    public boolean unsubscribe(OrderID orderID) throws OrderNotRefundableException, UserNotExistException, WrongPwdException, AlipayBalanceNotAdequateException {
        Order order = orderRepository.findByOrderID(orderID);
        ProgramID programID = order.getProgramID();
        LocalDateTime programTime = programID.getStartTime().minusMinutes(15);
        //在不是未支付状态下或者节目开始前15分钟，均不可退票
        if (!order.getOrderState().equals(OrderState.PAYED) || programTime.isBefore(LocalDateTime.now())) {
            throw new OrderNotRefundableException(ExceptionCode.ORDER_NOT_REFUNDABLE);
        }

        //退款操作
        AlipayAccount to = alipayService.getUserAccount(orderID.getEmail());
        AlipayAccount from = SystemHelper.getSystemAccount();
        alipayService.transferAccounts(Objects.requireNonNull(from).getId(), from.getPwd(), to.getId(), order.getTotalPrice());

        order.setOrderState(OrderState.REFUND);
        Set<Ticket> tickets = new HashSet<>();
        for (Ticket ticket : order.getTickets()) {
            ticket.setOrder(null); //取消订单和票的关联
            ticket.setLock(false);
            tickets.add(ticket);
        }
        order.getTickets().clear();
        order.setTickets(null); //取消订单和票的关联
        orderRepository.save(order);
        ticketRepository.saveAll(tickets);
        orderRepository.deleteRelationOfTicket(orderID.getEmail(), orderID.getTime());//删除关联集合
        return true;
    }

    /**
     * @param uid 查看订单的用户
     * @return 查看某一用户的全部订单
     */
    @Override
    public List<Order> getMyOrders(String uid) {
        return orderRepository.getMyOrders(uid);
    }

    /**
     * @param uid        查看指定类型订单的用户
     * @param orderState 订单状态或订单类型
     * @return 查看某一用户的全部订单
     */
    @Override
    public List<Order> getMyOrders(String uid, OrderState orderState) {
        return orderRepository.getMyOrders(uid, orderState);
    }

    /**
     * 跟据订单状态获取订单
     *
     * @param orderState 订单状态
     * @return 订单
     */
    @Override
    public List<Order> getOrderByState(OrderState orderState) {
        return orderRepository.getOrderByState(orderState);
    }

//    /**
//     * 根据据订单中计划开始的时间时限确定退款的比例
//     */
//    private double getRefundPercent(LocalDateTime now, LocalDateTime scheduleTime) {
//        // TODO 不同时限退款不同
//        return 1;
//    }


//    @Override
//    public int subscribe(User curUser, String scheduleId, OrderType orderType, NotChoseSeats notChoseSeats,
//                         String choseSeatsJson, OrderWay orderWay, boolean onSpotIsMember, String onSpotMemberId,
//                         boolean didUseCoupon, Coupon usedCoupon, String calProcess, double totalPrice)
//            throws UserNotExistException, InteriorWrongException {
//
//        Schedule toOrderSchedule = scheduleDao.getSchedule(scheduleId);
//
//        Order order = new Order();
//        order.setSchedule(toOrderSchedule);
//        order.setOrderWay(orderWay);
//        order.setOrderState(OrderState.ORDERED);
//        order.setOrderType(orderType);
//        order.setOrderTime(LocalDateTime.now());
//        order.setCalProcess(calProcess);
//        order.setTotalPrice(totalPrice);
//
//        if (orderType == OrderType.CHOOSE_SEATS) {
//            order.setOrderedSeatsJson(choseSeatsJson);
//
//            List<Seat> curbBookedSeats = JSON.parseArray(choseSeatsJson, Seat.class);
//            List<String> curBookedIds = OrderSeatHelper.getSeatListIds(curbBookedSeats);
//
//            // 对schedule进行处理，预定的座位更新
//            List<String> alreadyBookedIds = JSON.parseArray(toOrderSchedule.getBookedSeatsIdJson(), String.class);
//            alreadyBookedIds.addAll(curBookedIds);
//            toOrderSchedule.setBookedSeatsIdJson(JSON.toJSONString(alreadyBookedIds));
//
//            List<String> curRemainSeatMap = JSON.parseArray(toOrderSchedule.getRemainSeatsJson(), String.class);
//            for (String id : curBookedIds) {
//                String[] idParts = id.split("_");
//                int rowIndex = Integer.parseInt(idParts[0]) - 1;
//                int colIndex = Integer.parseInt(idParts[1]) - 1;
//
//                curRemainSeatMap.set(rowIndex,
//                        OrderSeatHelper.orderSpecificSeat(curRemainSeatMap.get(rowIndex), colIndex));
//            }
//            toOrderSchedule.setRemainSeatsJson(JSON.toJSONString(curRemainSeatMap));
//            // 因为此时此order还未被存储，所以不受级联更新
//            scheduleDao.updateSchedule(toOrderSchedule);
//
//        } else if (orderType == OrderType.NOT_CHOOSE_SEATS) {
//            // 对此非选座购票处理，找到此座位的表示字符
//            Map<SeatInfo, Double> priceMap = JSON.parseObject(toOrderSchedule.getSeatInfoPricesJson(), new TypeReference<HashMap<SeatInfo, Double>>() {
//            });
//            for (SeatInfo tempSeatInfo : priceMap.keySet()) {
//                if (tempSeatInfo.getSeatName().equals(notChoseSeats.getSeatName())) {
//                    notChoseSeats.setSeatChar(tempSeatInfo.getSeatChar());
//                    break;
//                }
//            }
//
//            order.setNotChoseSeats(notChoseSeats);
//            notChoseSeats.setOrder(order);
//        }
//
//        Member buyer;
//        if (orderWay == OrderWay.BUY_ON_MEMBER) {
//            buyer = (Member) curUser;
//            order.setMember(buyer);
//            order = updateCouponOfBuyer(didUseCoupon, buyer, usedCoupon, order);
//        } else if (orderWay == OrderWay.BUY_ON_SPOT) {
//            // 现场购买可以是非会员
//            if (onSpotIsMember) {
//                buyer = (Member) userDao.getUser(onSpotMemberId, UserType.MEMBER);
//                order.setMember(buyer);
//                order = updateCouponOfBuyer(didUseCoupon, buyer, usedCoupon, order);
//            }
//
//            // 现场购买不需要网上付款，直接已支付
//            order.setOrderState(OrderState.PAYED);
//        } else {
//            throw new InteriorWrongException();
//        }
//
//        return orderDao.saveOrder(order);
//    }
}
