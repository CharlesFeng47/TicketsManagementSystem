package cn.edu.nju.charlesfeng.dao;

import cn.edu.nju.charlesfeng.model.AlipayAccount;

/**
 * 数据层对支付宝订单的模拟操作
 */
public interface AlipayDao {

    /**
     * @param aliId 要查看的支付宝账户ID
     * @return 该账户实体
     */
    AlipayAccount getAlipayEntity(String aliId);

    /**
     * @param alipayAccount 欲更新的支付宝实体
     * @return 更新结果，成功则true
     */
    boolean update(AlipayAccount alipayAccount);
}
