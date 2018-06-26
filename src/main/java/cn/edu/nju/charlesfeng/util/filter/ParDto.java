package cn.edu.nju.charlesfeng.util.filter;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.id.ParID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;

import java.io.Serializable;

public class ParDto implements Serializable, Comparable<ParDto> {

    /**
     * 票面ID（节目ID， 票面底价）
     */
    private ParID parID;

    /**
     * 当前票面使用的折扣
     */
    private double discount;

    /**
     * 该种票面对应的座位类型
     */
    private String seatType;

    public ParDto(Par par) {
        parID = par.getParID();
        discount = par.getDiscount();
        seatType = par.getSeatType();
    }

    public ParID getParID() {
        return parID;
    }

    public double getDiscount() {
        return discount;
    }

    public String getSeatType() {
        return seatType;
    }

    @Override
    public int compareTo(ParDto o) {
        //两个ID相同，则返回节目相同
        if (parID.equals(o.parID)) {
            return 0;
        }

        ProgramID programID = o.getParID().getProgramID();
        // 我的场馆ID小于指定场馆ID，则返回我的大
        if (parID.getProgramID().getVenueID() < programID.getVenueID()) {
            return 1;
        }

        //场馆ID相同，节目开始时间先于指定节目开始时间，返回大
        if (parID.getProgramID().getVenueID() == programID.getVenueID() && parID.getProgramID().getStartTime().isBefore(programID.getStartTime())) {
            return 1;
        }

        //场馆ID相同，节目开始时间先于指定节目开始时间，价格大于指定价格，返回大
        if (parID.getProgramID().getVenueID() == programID.getVenueID() && parID.getProgramID().getStartTime().isBefore(programID.getStartTime())
                && parID.getBasePrice() > o.getParID().getBasePrice()) {
            return 1;
        }

        return -1;
    }
}
