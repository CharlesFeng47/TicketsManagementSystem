package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.util.enums.ConsumptionType;

import javax.persistence.*;

/**
 * 消费记录
 */
@Entity
@Table(name = "consumption")
public class Consumption {

    /**
     * 消费记录标志符ID，hibernate自己自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 消费记录类型
     */
    @Column(name = "type", nullable = false)
    private ConsumptionType type;

    /**
     * 消费记录关联的会员
     */
    @Column(name = "mid", nullable = false)
    private String mid;

    /**
     * 消费记录关联的场馆
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "sid")
    private Spot spot;

    /**
     * 消费记录的金额
     */
    @Column(name = "price", nullable = false)
    private double price;

    public Consumption() {
    }

    public Consumption(ConsumptionType type, String mid, Spot spot, double price) {
        this.type = type;
        this.mid = mid;
        this.spot = spot;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConsumptionType getType() {
        return type;
    }

    public void setType(ConsumptionType type) {
        this.type = type;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
