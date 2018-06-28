package cn.edu.nju.charlesfeng.util.filter.order;

import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;

import java.io.Serializable;
import java.time.LocalDateTime;

public class OrderBrief implements Serializable {

    /**
     * 订单号
     */
    private String orderID;

    /**
     * 订单生成时间
     */
    private LocalDateTime orderTime;

    /**
     * 节目ID（和节目详情那边一样）
     */
    private String programID;

    /**
     * 节目的开始时间
     */
    private LocalDateTime programTime;

    /**
     * 节目的名称
     */
    private String programName;

    /**
     * 订单总价
     */
    private double totalPrice;

    /**
     * 订单票数
     */
    private int num;

    /**
     * 订单的类型（状态）
     */
    private String orderState;

    /**
     * 场馆的名称
     */
    private String venueName;

    /**
     * 节目海报的url
     */
    private String imagesUrl;

    public OrderBrief(Order order) {
        orderID = String.valueOf(TimeHelper.getLong(order.getOrderID().getTime()));
        orderTime = order.getOrderID().getTime();
        Program program = order.getProgram();
        programID = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programTime = program.getProgramID().getStartTime();
        programName = program.getName();
        totalPrice = order.getTotalPrice();
        orderState = order.getOrderState().toString();
        venueName = program.getVenue().getVenueName();
        num = order.getTickets().size();
        imagesUrl = SystemHelper.getDomainName() + program.getProgramType().name() + "/" + programID + ".jpg";
    }

    public String getOrderID() {
        return orderID;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getProgramID() {
        return programID;
    }

    public LocalDateTime getProgramTime() {
        return programTime;
    }

    public String getProgramName() {
        return programName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getNum() {
        return num;
    }

    public String getOrderState() {
        return orderState;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }
}
