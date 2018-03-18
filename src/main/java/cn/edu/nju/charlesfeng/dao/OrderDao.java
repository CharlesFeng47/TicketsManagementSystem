package cn.edu.nju.charlesfeng.dao;

import cn.edu.nju.charlesfeng.entity.Order;

import java.util.List;

/**
 * 数据层对订单的服务操作
 */
public interface OrderDao {

    /**
     * @param id 要查看的订单ID
     * @return 该订单的实体对象
     */
    Order getOrder(int id);

    /**
     * @return 所有的订单实体
     */
    List<Order> getAllOrders();

    /**
     * @param order 欲保存的订单实体
     * @return 成功保存后的此实体对象主键
     */
    int saveOrder(Order order);

    /**
     * @param order 欲更新的订单实体
     * @return 更新结果，成功则true
     */
    boolean updateOrder(Order order);
}
