package cn.edu.nju.charlesfeng.model;

/**
 * 场馆中座位的一个实体类
 */
public class Seat {

    /**
     * 此座位所在的行
     */
    private int row;

    /**
     * 此座位所在的列
     */
    private int column;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
