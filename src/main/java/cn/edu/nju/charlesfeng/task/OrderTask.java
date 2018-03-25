package cn.edu.nju.charlesfeng.task;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.util.OrderSeatHelper;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 订单相关的操作
 */
@Component
public class OrderTask {

    private final static Logger logger = Logger.getLogger(OrderTask.class);

    private final OrderDao orderDao;

    @Autowired
    public OrderTask(OrderDao orderDao) {
        this.orderDao = orderDao;
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

                            curRemainSeatMap.set(rowIndex, lowercaseSpecificChar(curRemainSeatMap.get(rowIndex), colIndex));
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

        List<Order> orders = orderDao.getAllOrders();
        for (Order curOrder : orders) {
            if (curOrder.getOrderState() == OrderState.PAYED && curOrder.getOrderType() == OrderType.NOT_CHOOSE_SEATS
                    && curOrder.getOrderedSeatsJson() == null) {
                // TODO 配票
            }
        }
    }

    /**
     * 替换指定位置的座位为小写
     */
    private String lowercaseSpecificChar(String str, int pos) {
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, pos));
        sb.append((char) (str.charAt(pos) + 0x20));
        if (pos < str.length() - 1) sb.append(str.substring(pos + 1));
        return sb.toString();
    }

}
