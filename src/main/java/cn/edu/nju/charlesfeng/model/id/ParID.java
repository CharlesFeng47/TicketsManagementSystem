package cn.edu.nju.charlesfeng.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParID implements Serializable {

    /**
     * 节目的ID
     */
    private ProgramID programID;

    /**
     * 当前类型票面的底价
     */
    @Column(name = "base_price", columnDefinition = "double default 0")
    private double basePrice;

    /**
     * 该种票面的提示
     */
    private String comments;

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParID parID = (ParID) o;
        return Double.compare(parID.basePrice, basePrice) == 0 &&
                Objects.equals(programID, parID.programID) &&
                Objects.equals(comments, parID.comments);
    }

    @Override
    public int hashCode() {

        return Objects.hash(programID, basePrice, comments);
    }
}
