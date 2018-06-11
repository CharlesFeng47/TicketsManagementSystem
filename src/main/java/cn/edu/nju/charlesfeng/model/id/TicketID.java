package cn.edu.nju.charlesfeng.model.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TicketID implements Serializable {

    /**
     * 节目ID（场馆ID， 节目时间）
     */
    private ProgramID programID;

    /**
     * 座位的列
     */
    @Column(length = 8)
    private int col;

    /**
     * 座位的行
     */
    @Column(name = "`row`", length = 8)
    private int row;

    public ProgramID getProgramID() {
        return programID;
    }

    public void setProgramID(ProgramID programID) {
        this.programID = programID;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketID ticketID = (TicketID) o;
        return col == ticketID.col &&
                row == ticketID.row &&
                Objects.equals(programID, ticketID.programID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(programID, col, row);
    }
}
