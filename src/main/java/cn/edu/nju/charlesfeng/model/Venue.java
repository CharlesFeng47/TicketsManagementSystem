package cn.edu.nju.charlesfeng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 场馆实体
 */
@Entity
@Table(name = "venue")
public class Venue implements Serializable {

    /**
     * 场馆的ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vid")
    private int venueID;

    /**
     * 场馆名
     */
    @Column(name = "name", nullable = false)
    private String venueName;

    /**
     * 该场馆的支付宝账户ID
     */
    private String alipayId;

    /**
     * 场馆的地址
     */
    private Address address;

    /**
     * 场馆下的所有节目(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venue", fetch = FetchType.EAGER)
    private Set<Program> programs;

    /**
     * 场馆下的所有座位(1->N)
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "venue", fetch = FetchType.EAGER)
    private Set<Seat> seats;

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(String alipayId) {
        this.alipayId = alipayId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }
}
