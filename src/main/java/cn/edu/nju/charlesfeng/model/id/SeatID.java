package cn.edu.nju.charlesfeng.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SeatID implements Serializable {

    /**
     * 座位的列
     */
    @Column(length = 8)
    private int col;

    /**
     * 座位的行
     */
    @Column(length = 8)
    private int row;

    /**
     * 场馆ID
     */
    @Column(name = "vid", length = 7)
    private int venueID;

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getVenueID() {
        return venueID;
    }

    public void setVenueID(int venueID) {
        this.venueID = venueID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatID seatID = (SeatID) o;
        return col == seatID.col &&
                row == seatID.row &&
                venueID == seatID.venueID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(col, row, venueID);
    }
}
