package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    Order findByOrderID(OrderID orderID);

    /**
     * 获取指定用户的所有订单
     *
     * @param email 用户的邮箱（ID）
     * @return 所有订单实体
     */
    @Query(value = "select o from Order as o where o.orderID.email=:email")
    List<Order> getMyOrders(@Param("email") String email);

    /**
     * 获取指定用户的所有指定类型的订单
     *
     * @param email 用户的邮箱（ID）
     * @return 所有订单实体
     */
    @Query(value = "select o from Order as o where o.orderID.email=:email and o.orderState=:orderState")
    List<Order> getMyOrders(@Param("email") String email, @Param("orderState") OrderState orderState);

    /**
     * 跟据订单状态获取订单
     *
     * @param orderState 订单状态
     * @return 订单
     */
    @Query(value = "select o from Order as o where o.orderState=:orderState")
    List<Order> getOrderByState(@Param("orderState") OrderState orderState);

    /**
     * 根据orderID获取节目ID
     *
     * @param userID
     * @param orderTime
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "delete from order_ticket where order_email=:userID and order_time=:order_time", nativeQuery = true)
    void deleteRelationOfTicket(@Param("userID") String userID, @Param("order_time") LocalDateTime orderTime);
}
