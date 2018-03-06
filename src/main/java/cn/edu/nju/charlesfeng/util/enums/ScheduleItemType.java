package cn.edu.nju.charlesfeng.util.enums;

/**
 * 场馆可以举行的计划
 * 修改此处类型之后，前端展示也需要对应修改
 * （/src/views/schedule/index.vue:22,
 * /src/views/schedule/oneSchedule.vue:144,
 * /src/views/schedule/newSchedule/step1.vue:25）
 */
public enum ScheduleItemType {

    CONCERT("音乐会"),
    DANCE("舞蹈"),
    DRAMA("话剧"),
    SPORT("体育赛事");

    private String val;

    ScheduleItemType(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return this.val;
    }

    public static ScheduleItemType getEnum(String val) {
        for (ScheduleItemType curType : ScheduleItemType.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }
}
