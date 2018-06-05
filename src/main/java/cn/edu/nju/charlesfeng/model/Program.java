package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 节目实体（也可以理解是计划）
 */
@Entity
@Table(name = "program")
public class Program implements Serializable {

    /**
     * 节目ID（节目开始时间， 场馆ID）
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private ProgramID programID;

    /**
     * 节目名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 节目类型
     */
    @Column(name = "type", nullable = false)
    @Enumerated
    private ProgramType programType;

    /**
     * 节目海报
     */
    private String poster;

    /**
     * 节目介绍
     */
    private String description;

    /**
     * 节目浏览量
     */
    @Column(name = "scan", columnDefinition = "int default 0", nullable = false)
    private int scanVolume;

    /**
     * 节目喜爱量
     */
    @Column(name = "like", columnDefinition = "int default 0", nullable = false)
    private int likeVolume;

    /**
     * 节目所属的场馆实体(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "vid", insertable=false, updatable=false)
    private Venue venue;

    /**
     * 节目下的所有票面(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.EAGER)
    private List<Par> pars;

    /**
     * 节目下的所有票(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    /**
     * 节目下的所有的订单(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.EAGER)
    private List<Order> orders;

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScanVolume() {
        return scanVolume;
    }

    public void setScanVolume(int scan) {
        this.scanVolume = scan;
    }

    public int getLikeVolume() {
        return likeVolume;
    }

    public void setLikeVolume(int like) {
        this.likeVolume = like;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Par> getPars() {
        return pars;
    }

    public void setPars(List<Par> pars) {
        this.pars = pars;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
