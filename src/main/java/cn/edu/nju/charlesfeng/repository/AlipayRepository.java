package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.AlipayAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数据层对支付宝订单的模拟操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface AlipayRepository extends JpaRepository<AlipayAccount, String> {
}
