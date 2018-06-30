package cn.edu.nju.charlesfeng.dto.order;

import cn.edu.nju.charlesfeng.model.Address;
import cn.edu.nju.charlesfeng.model.Order;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Dong
 */
public class OrderDTO implements Serializable {

    /**
     * 订单号
     */
    private String orderID;

    /**
     * 订单生成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    /**
     * 节目ID（和节目详情那边一样）
     */
    private String programID;

    /**
     * 节目的开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime programTime;

    /**
     * 节目的名称
     */
    private String programName;

    /**
     * 订单总价
     */
    private Double totalPrice;

    /**
     * 订单票数
     */
    private Integer num;

    /**
     * 订单的类型（状态）
     */
    private OrderState orderState;

    /**
     * 场馆的名称
     */
    private String venueName;

    /**
     * 场馆的详细地址
     */
    private Address venueAddress;

    /**
     * 节目海报的url
     */
    private String imagesUrl;

    /**
     * 详细的座位信息
     */
    private Map<String, Double> ticketInfo;

    public OrderDTO(Order order) {
        orderID = String.valueOf(TimeHelper.getLong(order.getOrderID().getTime()));
        orderTime = order.getOrderID().getTime();
        Program program = order.getProgram();
        programID = String.valueOf(program.getProgramID().getVenueID()) + "-" + String.valueOf(TimeHelper.getLong(program.getProgramID().getStartTime()));
        programTime = program.getProgramID().getStartTime();
        programName = program.getName();
        totalPrice = order.getTotalPrice();
        orderState = order.getOrderState();
        venueName = program.getVenue().getVenueName();
        venueAddress = program.getVenue().getAddress();
        num = order.getTickets().size();
        imagesUrl = SystemHelper.getDomainName() + program.getProgramType().name() + "/" + programID + ".jpg";
        ticketInfo = new TreeMap<>();
        for (Ticket ticket : order.getTickets()) {
            String seatInfo = ticket.getSeatType() + String.valueOf(ticket.getTicketID().getRow()) + "排" + String.valueOf(ticket.getTicketID().getCol()) + "座";
            ticketInfo.put(seatInfo, ticket.getPrice());
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Integer getNum() {
        return num;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public String getVenueName() {
        return venueName;
    }

    public Address getVenueAddress() {
        return venueAddress;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public Map<String, Double> getTicketInfo() {
        return ticketInfo;
    }
}
