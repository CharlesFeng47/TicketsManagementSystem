package cn.edu.nju.charlesfeng.task;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.SeatId;
import cn.edu.nju.charlesfeng.util.OrderSeatHelper;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.exceptions.NoSuitableSeatException;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 订单相关的操作
 */
@Component
public class OrderTask {

    private final static Logger logger = Logger.getLogger(OrderTask.class);

    private final OrderDao orderDao;

    private final ScheduleDao scheduleDao;

    @Autowired
    public OrderTask(OrderDao orderDao, ScheduleDao scheduleDao) {
        this.orderDao = orderDao;
        this.scheduleDao = scheduleDao;
    }

    /**
     * 15分钟内未成功支付的订单自动取消
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void unpayedOrderAutoCancel() {
        logger.info("UnpayedOrderAutoCancel Task 开始工作");

        // 订单自动取消的时长
        long autoCancelledDurationSeconds = 60 * 3;

        final LocalDateTime curTime = LocalDateTime.now();
        List<Order> orders = orderDao.getAllOrders();
        for (Order curOrder : orders) {
            if (curOrder.getOrderState() == OrderState.ORDERED) {
                long differ = ChronoUnit.SECONDS.between(curOrder.getOrderTime(), curTime);
                if (differ > autoCancelledDurationSeconds) {
                    curOrder.setOrderState(OrderState.CANCELLED);

                    // 对schedule进行处理，预定的座位被释放
                    if (curOrder.getOrderedSeatsJson() != null) {
                        // 需要释放座位
                        List<Seat> curOrderedSeats = JSON.parseArray(curOrder.getOrderedSeatsJson(), Seat.class);
                        List<String> curOrderedSeatIds = OrderSeatHelper.getSeatListIds(curOrderedSeats);

                        Schedule toUpdateSchedule = curOrder.getSchedule();
                        List<String> alreadyBookedIds = JSON.parseArray(toUpdateSchedule.getBookedSeatsIdJson(), String.class);
                        alreadyBookedIds.removeAll(curOrderedSeatIds);
                        toUpdateSchedule.setBookedSeatsIdJson(JSON.toJSONString(alreadyBookedIds));

                        List<String> curRemainSeatMap = JSON.parseArray(toUpdateSchedule.getRemainSeatsJson(), String.class);
                        for (String id : curOrderedSeatIds) {
                            String[] idParts = id.split("_");
                            int rowIndex = Integer.parseInt(idParts[0]) - 1;
                            int colIndex = Integer.parseInt(idParts[1]) - 1;

                            curRemainSeatMap.set(rowIndex,
                                    OrderSeatHelper.releaseSpecificSeat(curRemainSeatMap.get(rowIndex), colIndex));
                        }
                        toUpdateSchedule.setRemainSeatsJson(JSON.toJSONString(curRemainSeatMap));
                    }

                    // 更新订单，级联更新座位
                    orderDao.updateOrder(curOrder);
                }
            }
        }
    }

    /**
     * 未检票的订单自动过期
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void unpcheckedOrderAutoExpire() {
        logger.info("unpcheckedOrderAutoExpire Task 开始工作");

        final LocalDateTime curTime = LocalDateTime.now();
        List<Order> orders = orderDao.getAllOrders();
        for (Order curOrder : orders) {
            if (curOrder.getOrderState() == OrderState.PAYED) {
                if (curTime.isAfter(curOrder.getSchedule().getStartDateTime())) {
                    curOrder.setOrderState(OrderState.EXPIRED);
                    orderDao.updateOrder(curOrder);
                }
            }
        }
    }

    /**
     * 每5分钟，对已支付但没有配票的订单进行配票
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void distributeTicket() {
        logger.info("DistributeTicket Task 开始工作");
        LocalDateTime start = LocalDateTime.now();

        // 订单开始自动配票的时限
        long autoDistributedTicketDays = 14;

        // 对订单按计划分类
        Map<Schedule, List<Order>> neededDispatchScheduleMap = new HashMap<>();
        for (Order curOrder : orderDao.getAllOrders()) {
            final Schedule curSchedule = curOrder.getSchedule();
            // 时限之内的才进行配票
            if (ChronoUnit.DAYS.between(start, curSchedule.getStartDateTime()) < autoDistributedTicketDays) {
                List<Order> curOrdersOfSchedule = neededDispatchScheduleMap.get(curSchedule);
                if (curOrdersOfSchedule == null) curOrdersOfSchedule = new LinkedList<>();

                curOrdersOfSchedule.add(curOrder);
                neededDispatchScheduleMap.put(curSchedule, curOrdersOfSchedule);
            }
        }

        // 对计划配票
        for (Map.Entry<Schedule, List<Order>> entry : neededDispatchScheduleMap.entrySet()) {
            Schedule toDispatchSchedule = entry.getKey();
            // 剩余座位图
            List<String> curRemainSeatMap = JSON.parseArray(toDispatchSchedule.getRemainSeatsJson(), String.class);
            // 已经预定的座位ID
            List<String> alreadyBookedIds = JSON.parseArray(toDispatchSchedule.getBookedSeatsIdJson(), String.class);

            for (Order curOrder : entry.getValue()) {
                if (curOrder.getOrderState() == OrderState.PAYED && curOrder.getOrderType() == OrderType.NOT_CHOOSE_SEATS
                        && curOrder.getOrderedSeatsJson() == null) {
                    NotChoseSeats ncs = curOrder.getNotChoseSeats();
                    try {
                        List<SeatId> dispatchedSeats = getDispatchedSeats(curRemainSeatMap, ncs.getSeatChar(), ncs.getNum());

                        // 对分配到的座位预定
                        List<Seat> curOrderedSeats = new LinkedList<>();
                        for (SeatId curDispatchedSeatId : dispatchedSeats) {
                            final int rowIndex = curDispatchedSeatId.getRowIndex();
                            final int colIndex = curDispatchedSeatId.getColIndex();
                            curRemainSeatMap.set(rowIndex,
                                    OrderSeatHelper.orderSpecificSeat(curRemainSeatMap.get(rowIndex), colIndex));

                            // 订单中已预订到的座位信息
                            curOrderedSeats.add(new Seat(curDispatchedSeatId, ncs));

                            // 计划中已预订到的座位ID
                            alreadyBookedIds.add(curDispatchedSeatId.toString());
                        }

                        curOrder.setOrderedSeatsJson(JSON.toJSONString(curOrderedSeats));
                    } catch (NoSuitableSeatException e) {
                        curOrder.setOrderState(OrderState.DISPATCH_FAIL);
                    }
                }
            }

            // 此计划配票完毕，级联更新所有订单
            toDispatchSchedule.setRemainSeatsJson(JSON.toJSONString(curRemainSeatMap));
            toDispatchSchedule.setBookedSeatsIdJson(JSON.toJSONString(alreadyBookedIds));
            scheduleDao.updateSchedule(toDispatchSchedule);
        }

        // 对计划
        LocalDateTime end = LocalDateTime.now();
        System.out.println("DistributeTicket Task 耗时：" + ChronoUnit.SECONDS.between(start, end));
    }

    /**
     * 根据不选座的情况得到指定数目指定类型的座位
     */
    private List<SeatId> getDispatchedSeats(List<String> seatMap, char bookedSeatChar, int bookedSeatNum) throws NoSuitableSeatException {
        List<SeatId> result = new LinkedList<>();

        final int rowSize = seatMap.size();
        final int colSize = seatMap.get(0).length();
        for (int i = 0; i < rowSize; i++) {
            final String curRow = seatMap.get(i);
            for (int j = 0; j < colSize; j++) {
                if (curRow.charAt(j) == bookedSeatChar) {
                    result.add(new SeatId(i, j));
                    bookedSeatNum--;

                    // 找完了就返回
                    if (bookedSeatNum == 0) return result;
                }
            }
        }
        throw new NoSuitableSeatException();
    }

}
