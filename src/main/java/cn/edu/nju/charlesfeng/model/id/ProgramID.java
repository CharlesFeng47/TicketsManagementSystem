package cn.edu.nju.charlesfeng.model.id;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Dong
 */
@Embeddable
public class ProgramID implements Serializable, Comparable<ProgramID> {

    /**
     * 场馆ID
     */
    @Column(name = "vid")
    private int venueID;

    /**
     * 节目开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    public ProgramID() {
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProgramID programID = (ProgramID) o;
        return venueID == programID.venueID &&
                Objects.equals(startTime, programID.startTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(venueID, startTime);
    }

    @Override
    public int compareTo(ProgramID o) {
        //两个ID相同，则返回节目相同
        if (venueID == o.getVenueID() && startTime.isEqual(o.getStartTime())) {
            return 0;
        }

        // 我的场馆ID小于指定场馆ID，则返回我的大
        if (venueID < o.getVenueID()) {
            return 1;
        }

        //场馆ID相同，节目开始时间先于指定节目开始时间，返回大
        if (venueID == o.getVenueID() && startTime.isBefore(o.getStartTime())) {
            return 1;
        }

        return -1;
    }
}
