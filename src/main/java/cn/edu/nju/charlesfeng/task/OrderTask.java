package cn.edu.nju.charlesfeng.task;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
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
    @Scheduled(cron = "0 1 * * * ?")
    public void unpayedOrderAutoCancel() {
        logger.info("UnpayedOrderAutoCancel Task 开始工作");

        // 订单自动取消的时长
        long autoCancelledDurationSeconds = 60 * 15;

        final LocalDateTime curTime = LocalDateTime.now();
        List<Order> orders = orderDao.getAllOrders();
        for (Order curOrder : orders) {
            if (curOrder.getOrderState() == OrderState.ORDERED) {
                long differ = ChronoUnit.SECONDS.between(curOrder.getOrderTime(), curTime);
                if (differ > autoCancelledDurationSeconds) {
                    curOrder.setOrderState(OrderState.CANCELLED);
                    orderDao.updateOrder(curOrder);
                }
            }
        }
    }

    /**
     * 每5分钟，对已支付但没有配票的订单进行配票
     */
    @Scheduled(cron = "0 5 * * * ?")
    public void distributeTicket() {
        logger.info("DistributeTicket Task 开始工作");

        List<Order> orders = orderDao.getAllOrders();
        for (Order curOrder : orders) {
            if (curOrder.getOrderState() == OrderState.PAYED && curOrder.getOrderType() == OrderType.NOT_CHOOSE_SEATS) {
                // TODO 配票
            }
        }
    }
}
