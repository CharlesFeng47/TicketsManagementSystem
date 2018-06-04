package cn.edu.nju.charlesfeng.filter;

/**
 * 前端使用的PieChart的data
 */
public class PieChartData {

    /**
     * 名称
     */
    private String name;

    /**
     * 数量
     */
    private int value;

    public PieChartData() {
    }

    PieChartData(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
