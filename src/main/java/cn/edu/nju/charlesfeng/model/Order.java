package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 订单实体
 */
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    /**
     * 订单ID（用户ID， 订单生成时间）
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private OrderID orderID;

    /**
     * 订单状态
     */
    @Column(name = "order_state")
    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    /**
     * 总价
     */
    @Column(name = "total_price")
    private double totalPrice;

    /**
     * 订单所属的节目实体(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "program_order",
            joinColumns = {@JoinColumn(name = "order_email",referencedColumnName = "email"),
                    @JoinColumn(name = "order_time",referencedColumnName = "time")},
            inverseJoinColumns = {@JoinColumn(name = "program_vid", referencedColumnName = "vid"),
                    @JoinColumn(name = "program_start_time", referencedColumnName = "start_time")
            })
    private Program program;

    /**
     * 订单下的所有票(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public OrderID getOrderID() {
        return orderID;
    }

    public void setOrderID(OrderID orderID) {
        this.orderID = orderID;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }
}
