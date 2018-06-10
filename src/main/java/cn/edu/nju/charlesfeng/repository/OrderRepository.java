package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据层对订单的服务操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface OrderRepository extends JpaRepository<Order, OrderID> {

    /**
     * 根据ID获取订单实体
     *
     * @param orderID 订单ID
     * @return 订单实体
     */
    Order findByOrOrderID(OrderID orderID);

    /**
     * 获取指定用户的所有订单
     *
     * @param email 用户的邮箱（ID）
     * @return 所有订单实体
     */
    @Query(value = "select o from Order as o where o.orderID.email=:email")
    List<Order> getMyOrders(@Param("email") String email);

}
