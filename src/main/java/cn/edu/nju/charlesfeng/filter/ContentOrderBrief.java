package cn.edu.nju.charlesfeng.filter;

import cn.edu.nju.charlesfeng.model.Order;

/**
 * 前端展示的订单简介
 */
public class ContentOrderBrief {

    /**
     * 订单标志符ID
     */
    private int id;

    /**
     * 此活动的名字
     */
    private String name;

    /**
     * 此活动开始的时间
     */
    private String startDateTime;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 订单总价
     */
    private double totalPrice;

    public ContentOrderBrief(Order order) {
        this.id = order.getId();
        this.name = order.getSchedule().getName();
        this.startDateTime = order.getSchedule().getStartDateTime().toString().replace('T', ' ');
        this.orderState = order.getOrderState().toString();
        this.totalPrice = order.getTotalPrice();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
