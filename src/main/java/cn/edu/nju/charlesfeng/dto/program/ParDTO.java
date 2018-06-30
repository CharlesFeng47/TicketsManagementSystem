package cn.edu.nju.charlesfeng.dto.program;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.id.ProgramID;

import java.io.Serializable;

/**
 * @author Dong
 */
public class ParDTO implements Serializable, Comparable<ParDTO> {

    /**
     * 节目的ID
     */
    private ProgramID programID;

    /**
     * 当前类型票面的底价
     */
    private Double basePrice;

    /**
     * 该种票面的提示
     */
    private String comments;

    /**
     * 当前票面使用的折扣
     */
    private Double discount;

    /**
     * 该种票面对应的座位类型
     */
    private String seatType;

    public ParDTO(Par par) {
        programID = par.getParID().getProgramID();
        basePrice = par.getParID().getBasePrice();
        comments = par.getParID().getComments();
        discount = par.getDiscount();
        seatType = par.getSeatType();
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public String getComments() {
        return comments;
    }

    public Double getDiscount() {
        return discount;
    }

    public String getSeatType() {
        return seatType;
    }

    @Override
    public int compareTo(ParDTO o) {
        if (basePrice.equals(o.getBasePrice())) {
            return 0;
        }

        if (basePrice > o.getBasePrice()) {
            return 1;
        }
        return -1;
    }
}
