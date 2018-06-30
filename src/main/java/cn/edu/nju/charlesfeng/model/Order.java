package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.OrderID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.OrderState;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 订单实体
 * @author Dong
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
     * 节目ID
     */
    private ProgramID programID;

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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "vid", referencedColumnName = "vid", insertable = false, updatable = false),
            @JoinColumn(name = "start_time", referencedColumnName = "start_time", insertable = false, updatable = false),
    })
    private Program program;

    /**
     * 订单下的所有票(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    public Order() {
    }

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

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }
}
