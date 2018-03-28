package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.util.enums.OrderState;

/**
 * 一种订单类型的订单数量
 */
public class SingleOrderNumOfOneState {

    /**
     * 类型名称
     */
    private String name;

    /**
     * 数量
     */
    private int value;

    public SingleOrderNumOfOneState(OrderState orderState, int num) {
        this.name = orderState.toString();
        this.value = num;
    }

    public SingleOrderNumOfOneState() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
