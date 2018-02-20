package cn.edu.nju.charlesfeng.dao.impl;

import cn.edu.nju.charlesfeng.dao.OrderDao;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import cn.edu.nju.charlesfeng.util.testUtil.DaoTestHelper;
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

    private OrderDao dao;

    @Before
    public void before() throws Exception {
        dao = DaoTestHelper.orderDao;
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getOrder(int id)
     */
    @Test
    public void testGetOrder() throws Exception {
        Order order = dao.getOrder(0);
    }

    /**
     * Method: saveOrder(Order order)
     */
    @Test
    public void testSaveOrder() throws Exception {
        Order order = new Order();
        order.setOrderTime(LocalDateTime.now());
        order.setOrderType(OrderType.NOT_CHOOSE_SEATS);
        order.setTotalPrice(100);

        dao.saveOrder(order);
    }

    /**
     * Method: updateOrder(Order order)
     */
    @Test
    public void testUpdateOrder() throws Exception {
        Order order = dao.getOrder(0);
        order.setOrderTime(LocalDateTime.now());
        dao.updateOrder(order);
    }


} 
