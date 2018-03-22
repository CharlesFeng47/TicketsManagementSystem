package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.dao.ScheduleDao;
import cn.edu.nju.charlesfeng.dao.UserDao;
import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.entity.NotChoseSeats;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.enums.OrderWay;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
import com.alibaba.fastjson.JSON;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * OrderDaoImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>二月 20, 2018</pre>
 */
public class OrderDaoImplTest {

    private OrderDao orderDao;

    private UserDao userDao;

    private ScheduleDao scheduleDao;

    @Before
    public void before() throws Exception {
        orderDao = DaoTestHelper.orderDao;
        userDao = DaoTestHelper.userDao;
        scheduleDao = DaoTestHelper.scheduleDao;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getOrder(int id)
     */
    @Test
    public void testGetOrder() throws Exception {
        Order order = orderDao.getOrder(0);
    }

    /**
     * Method: saveOrder(Order order)
     */
    @Test
    public void testSaveOrder() throws Exception {
        Order order = getNewOrder();
        orderDao.saveOrder(order);
    }

    /**
     * Method: updateOrder(Order order)
     */
    @Test
    public void testUpdateOrder() throws Exception {
        Order order = orderDao.getOrder(1);
        order.setOrderTime(LocalDateTime.now());
        orderDao.updateOrder(order);
    }

    /**
     * Method: updateOrder(Order order)
     * 测试order中的schedule改变，级联更新
     */
    @Test
    public void testUpdateScheduleOfOrder() throws Exception {
        Order order = orderDao.getOrder(7);
        order.setOrderTime(LocalDateTime.now());
        Schedule schedule = order.getSchedule();
        schedule.setName("测试用日程名字4");

        Order order2 = getNewOrder();
        schedule.getOrders().add(order2);
        orderDao.saveOrder(order2);

        System.out.println(JSON.toJSONString(schedule));
        scheduleDao.updateSchedule(schedule);
    }

    private Order getNewOrder() throws UserNotExistException {
        Order order = new Order();
        order.setOrderType(OrderType.NOT_CHOOSE_SEATS);
        order.setOrderTime(LocalDateTime.now());
        order.setTotalPrice(100);
        order.setOrderState(OrderState.ORDERED);
        order.setOrderWay(OrderWay.BUY_ON_MEMBER);
        order.setCalProcess("");

        NotChoseSeats ncs = new NotChoseSeats();
        ncs.setNum(100);
        ncs.setPrice(200);
        ncs.setSeatName("一等座");

        ncs.setOrder(order);
        order.setNotChoseSeats(ncs);

        Member curMember = (Member) userDao.getUser("suzy", UserType.MEMBER);
        order.setMember(curMember);

        Schedule curSchedule = scheduleDao.getSchedule("2018322194122");
        order.setSchedule(curSchedule);
        return order;
    }

} 
