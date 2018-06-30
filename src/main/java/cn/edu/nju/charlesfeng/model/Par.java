package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.ParID;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 指定节目下的票面
 * @author Dong
 */
@Entity
public class Par implements Serializable {

    /**
     * 票面ID（节目ID， 票面底价）
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private ParID parID;

    /**
     * 当前票面使用的折扣
     */
    @Column(name = "discount", columnDefinition = "double default 1")
    private double discount;

    /**
     * 该种票面对应的座位类型
     */
    @Column(name = "seat_type",nullable = false)
    private String seatType;

    /**
     * 该票面属于的节目实体(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vid", referencedColumnName = "vid", insertable = false, updatable = false),
            @JoinColumn(name = "start_time", referencedColumnName = "start_time", insertable = false, updatable = false),
    })
    private Program program;

    public ParID getParID() {
        return parID;
    }

    public void setParID(ParID parID) {
        this.parID = parID;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
