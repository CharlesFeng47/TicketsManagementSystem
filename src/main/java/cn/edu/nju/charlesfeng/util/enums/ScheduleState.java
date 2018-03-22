package cn.edu.nju.charlesfeng.util.enums;

/**
 * 计划状态
 */
public enum ScheduleState {

    RELEASED("已发布"),
    COMPLETED("已结束"),
    SETTLED("已结算");


    private String val;

    ScheduleState(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return this.val;
    }

    public static ScheduleState getEnum(String val) {
        for (ScheduleState curType : ScheduleState.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

}
