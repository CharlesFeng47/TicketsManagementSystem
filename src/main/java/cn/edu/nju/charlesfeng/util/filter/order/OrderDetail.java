package cn.edu.nju.charlesfeng.util.filter.order;

import cn.edu.nju.charlesfeng.model.Address;
import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail implements Serializable {

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
     * 场馆的详细地址
     */
    private Address venueAddress;

    /**
     * 票的详细信息
     */
    private List<Ticket> tickets;

    public OrderDetail(Order order) {
        orderID = String.valueOf(TimeHelper.getLong(order.getOrderID().getTime()));
        orderTime = order.getOrderID().getTime();
        Program program = order.getProgram();
        programID = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programTime = program.getProgramID().getStartTime();
        programName = program.getName();
        totalPrice = order.getTotalPrice();
        orderState = order.getOrderState().toString();
        venueName = program.getVenue().getVenueName();
        venueAddress = program.getVenue().getAddress();
        num = order.getTickets().size();
        tickets = new ArrayList<>();
        for (Ticket ticket : order.getTickets()) {
            tickets.add(new Ticket(ticket));
        }
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

    public Address getVenueAddress() {
        return venueAddress;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}