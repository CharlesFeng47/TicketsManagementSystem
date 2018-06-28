package cn.edu.nju.charlesfeng.task;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.service.OrderService;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 对节目和订单的操作
 */
@Component
public class ScheduleTask {

    private final static Logger logger = Logger.getLogger(ScheduleTask.class);

    private final OrderService orderService;

    @Autowired
    public ScheduleTask(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 每分钟检查一次，未支付订单到达15分钟取消订单
     */
    //@Scheduled(fixedRate = 60000)
    public void ScheduleAutoComplete() {
        logger.info("ScheduleAutoComplete Task 开始工作");
        List<Order> orders = orderService.getOrderByState(OrderState.UNPAID);
        for (Order order : orders) {
            if (TimeHelper.getDurationMinute(order.getOrderID().getTime(), LocalDateTime.now()) >= 15) {
                orderService.cancelOrderBySchedule(order);
                logger.info("已取消" + order.getOrderID().getTime().toString() + "--" + order.getOrderID().getEmail() + "订单");
            }
        }
    }
}
