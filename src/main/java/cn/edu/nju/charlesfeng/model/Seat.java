package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.SeatID;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 场馆的座位实体
 */
@Entity
@Table(name = "seat")
public class Seat implements Serializable {

    /**
     * 座位的ID（行，列，场馆ID）
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private SeatID seatID;

    /**
     * 当前座位的类型
     */
    private String type;

    /**
     * 座位属于的场馆(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "vid", insertable=false)
    private Venue venue;

    public SeatID getSeatID() {
        return seatID;
    }

    public void setSeatID(SeatID seatID) {
        this.seatID = seatID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
