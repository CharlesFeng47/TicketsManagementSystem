package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.id.OrderID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 数据层对订单的服务操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface OrderRepository extends JpaRepository<Order, OrderID> {
}
