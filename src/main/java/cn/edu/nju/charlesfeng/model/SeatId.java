package cn.edu.nju.charlesfeng.model;

/**
 * 座位的标志符
 */
public class SeatId {

    /**
     * 座位图中的行（从 0 开始）
     */
    private int rowIndex;

    /**
     * 座位图中的列（从 0 开始）
     */
    private int colIndex;

    public SeatId(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    @Override
    public String toString() {
        return (rowIndex + 1) + "_" + (colIndex + 1);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
}
