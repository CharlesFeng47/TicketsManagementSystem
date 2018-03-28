package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.OrderState;

/**
 * 一种订单类型的订单数量
 */
public class OrderNumOfOneState extends PieChartData {

    public OrderNumOfOneState(OrderState orderState, int num) {
        super(orderState.toString(), num);
    }
}
