package cn.edu.nju.charlesfeng.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class ProgramID implements Serializable {

    /**
     * 场馆ID
     */
    @Column(name = "vid")
    private int venueID;

    /**
     * 节目开始时间
     */
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramID programID = (ProgramID) o;
        return venueID == programID.venueID &&
                Objects.equals(startTime, programID.startTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(venueID, startTime);
    }
}
