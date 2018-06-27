package cn.edu.nju.charlesfeng.util.filter.program;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.id.ProgramID;

import java.io.Serializable;

public class ParDto implements Serializable {

    /**
     * 节目的ID
     */
    private ProgramID programID;

    /**
     * 当前类型票面的底价
     */
    private double basePrice;

    /**
     * 该种票面的提示
     */
    private String comments;

    /**
     * 当前票面使用的折扣
     */
    private double discount;

    /**
     * 该种票面对应的座位类型
     */
    private String seatType;

    public ParDto(Par par) {
        programID = par.getParID().getProgramID();
        basePrice = par.getParID().getBasePrice();
        comments = par.getParID().getComments();
        discount = par.getDiscount();
        seatType = par.getSeatType();
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getComments() {
        return comments;
    }

    public double getDiscount() {
        return discount;
    }

    public String getSeatType() {
        return seatType;
    }
}
