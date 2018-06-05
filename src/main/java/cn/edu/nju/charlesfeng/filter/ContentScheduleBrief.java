//package cn.edu.nju.charlesfeng.filter;
//
//import cn.edu.nju.charlesfeng.model.Schedule;
//
///**
// * 前端展示的日程简介
// */
//public class ContentScheduleBrief {
//
//    /**
//     * 此活动的标志符ID
//     */
//    private String id;
//
//    /**
//     * 此活动的名字
//     */
//    private String name;
//
//    /**
//     * 此活动的举行地点ID
//     */
//    private String spotName;
//
//    /**
//     * 此活动开始的时间
//     */
//    private String startDateTime;
//
//    /**
//     * 此活动的类型
//     */
//    private String type;
//
//    /**
//     * 计划的状态
//     */
//    private String state;
//
//    /**
//     * 订单下达之后支付款项，暂存到计划中，待结算
//     */
//    private double balance;
//
//    public ContentScheduleBrief(Schedule schedule) {
//        this.id = schedule.getId();
//        this.name = schedule.getName();
//        this.spotName = schedule.getSpot().getSpotName();
//        this.startDateTime = schedule.getStartDateTime().toString().replace('T', ' ');
//        this.type = schedule.getType().toString();
//        this.state = schedule.getState().toString();
//        this.balance = schedule.getBalance();
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSpotName() {
//        return spotName;
//    }
//
//    public void setSpotName(String spotName) {
//        this.spotName = spotName;
//    }
//
//    public String getStartDateTime() {
//        return startDateTime;
//    }
//
//    public void setStartDateTime(String startDateTime) {
//        this.startDateTime = startDateTime;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = balance;
//    }
//}
