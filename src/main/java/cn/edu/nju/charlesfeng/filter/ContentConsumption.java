//package cn.edu.nju.charlesfeng.filter;
//
//import cn.edu.nju.charlesfeng.model.Consumption;
//
///**
// * 前端展示的消费记录
// */
//public class ContentConsumption {
//
//    /**
//     * 消费记录类型
//     */
//    private String type;
//
//    /**
//     * 消费记录关联的会员
//     */
//    private String mid;
//
//    /**
//     * 消费记录关联的场馆名字
//     */
//    private String spotName;
//
//    /**
//     * 消费记录的金额
//     */
//    private double price;
//
//    public ContentConsumption(Consumption consumption) {
//        this.type = consumption.getType().toString();
//        this.mid = consumption.getMid();
//        this.spotName = consumption.getSpot().getSpotName();
//        this.price = consumption.getPrice();
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
//    public String getMid() {
//        return mid;
//    }
//
//    public void setMid(String mid) {
//        this.mid = mid;
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
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//}
