package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.entity.Coupon;
import cn.edu.nju.charlesfeng.entity.Order;
import cn.edu.nju.charlesfeng.entity.Spot;

/**
 * 前端展示的订单
 */
public class ContentOrder {

    /**
     * 订单标志符ID
     */
    private int id;

    /**
     * 订单下达的计划
     */
    private ContentSchedule schedule;

    /**
     * 订单下达方式
     */
    private String orderWay;

    /**
     * 订单状态
     */
    private String orderState;

    /**
     * 订单的类型
     */
    private String orderType;

    /**
     * 订单的下达时间
     */
    private String orderTime;

    /**
     * 订单总价
     */
    private double totalPrice;

    /**
     * 订单使用的优惠券
     */
    private Coupon usedCoupon;

    /**
     * 订单类型为NOT_CHOOSE_SEATS时，选择的座位情况
     */
    private ContentNotChoseSeats notChoseSeats;

    /**
     * 此订单中包含的座位具体情况 json 串【因为其信息本身就比较冗余，所以以 json 串形式存储，不再单独映射为表】
     * 若订单类型为NOT_CHOOSE_SEATS，此项可暂为空
     */
    private String orderedSeatsJson;

    public ContentOrder(Order order, Spot relativeSpot) {
        this.id = order.getId();
        this.schedule = new ContentSchedule(order.getSchedule(), relativeSpot);
        this.orderWay = order.getOrderWay().toString();
        this.orderState = order.getOrderState().toString();
        this.orderType = order.getOrderType().toString();
        this.orderTime = order.getOrderTime().toString().replace('T', ' ');
        this.totalPrice = order.getTotalPrice();
        this.usedCoupon = order.getUsedCoupon();
        this.notChoseSeats = new ContentNotChoseSeats(order.getNotChoseSeats());
        this.orderedSeatsJson = order.getOrderedSeatsJson();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContentSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(ContentSchedule schedule) {
        this.schedule = schedule;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Coupon getUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(Coupon usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ContentNotChoseSeats getNotChoseSeats() {
        return notChoseSeats;
    }

    public void setNotChoseSeats(ContentNotChoseSeats notChoseSeats) {
        this.notChoseSeats = notChoseSeats;
    }

    public String getOrderedSeatsJson() {
        return orderedSeatsJson;
    }

    public void setOrderedSeatsJson(String orderedSeatsJson) {
        this.orderedSeatsJson = orderedSeatsJson;
    }
}
