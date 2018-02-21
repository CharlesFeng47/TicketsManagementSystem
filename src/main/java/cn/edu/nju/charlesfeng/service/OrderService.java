package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Order;

/**
 * 系统中订单的服务
 */
public interface OrderService {

    /**
     * @param oid 要查看的订单ID
     * @return 订单详情
     */
    Order checkOrderDetail(int oid);

    /**
     * TODO 参数未定
     *
     * @return 预定结果，成果则返回订单实体
     */
    Order subscribe();

    /**
     * @param oid 要退订的订单ID
     * @return 退订结果，成功则true
     */
    boolean unsubscribe(String oid);
}
