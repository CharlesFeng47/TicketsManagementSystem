package cn.edu.nju.charlesfeng.util.filter;

/**
 * 每一种等级的会员数量
 */
public class NumOfOneMemberLevel extends PieChartData {

    public NumOfOneMemberLevel(int level, int value) {
        super("等级" + level, value);
    }
}
