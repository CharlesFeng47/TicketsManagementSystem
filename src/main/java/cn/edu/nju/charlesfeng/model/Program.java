package cn.edu.nju.charlesfeng.model;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

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
    @Column(name = "`name`", nullable = false)
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
    @Lob
    @Column(columnDefinition = "longblob")
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
    @Column(name = "`like`", columnDefinition = "int default 0", nullable = false)
    private int favoriteVolume;

    /**
     * 节目所属的场馆实体(N->1)
     */
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "vid", insertable = false, updatable = false)
    private Venue venue;

    /**
     * 节目下的所有票面(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.LAZY)
    private Set<Par> pars;

    /**
     * 节目下的所有票(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    /**
     * 节目下的所有的订单(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "program", fetch = FetchType.LAZY)
    private Set<Order> orders;

    /**
     * 喜欢该节目的用户(N->N)
     */
    @ManyToMany(mappedBy = "programs", fetch = FetchType.LAZY)
    private Set<User> users;

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

    public int getFavoriteVolume() {
        return favoriteVolume;
    }

    public void setFavoriteVolume(int like) {
        this.favoriteVolume = like;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Set<Par> getPars() {
        return pars;
    }

    public void setPars(Set<Par> pars) {
        this.pars = pars;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
