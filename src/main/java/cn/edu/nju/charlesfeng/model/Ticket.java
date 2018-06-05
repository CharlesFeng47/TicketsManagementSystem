package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.TicketID;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 票的实体，用于解除场馆座位，票面，订单之间的耦合
 */
@Entity
@Table(name = "ticket")
public class Ticket implements Serializable {

    /**
     * 票的ID（节目ID， 座位的列， 座位的行）
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private TicketID ticketID;

    /**
     * 票数的价
     */
    @Column(columnDefinition = "double default 0", nullable = false)
    private double price;

    /**
     * 这张票是否被锁定
     */
    @Column(columnDefinition = "bit default 0", nullable = false)
    private boolean isLock;

    /**
     * 票所属的节目(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "vid", referencedColumnName = "vid", insertable = false, updatable = false),
            @JoinColumn(name = "start_time", referencedColumnName = "start_time", insertable = false, updatable = false),
    })
    private Program program;

    /**
     * 票所属的订单(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "order_ticket",
    joinColumns = {@JoinColumn(name = "ticket_vid",referencedColumnName = "vid"),
            @JoinColumn(name = "ticket_start_time",referencedColumnName = "start_time"),
            @JoinColumn(name = "ticket_row", referencedColumnName = "row"),
            @JoinColumn(name = "ticket_col", referencedColumnName = "col")},
    inverseJoinColumns = {@JoinColumn(name = "order_email", referencedColumnName = "email"),
            @JoinColumn(name = "order_time", referencedColumnName = "time")
    })
    private Order order;

    public TicketID getTicketID() {
        return ticketID;
    }

    public void setTicketID(TicketID ticketID) {
        this.ticketID = ticketID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
