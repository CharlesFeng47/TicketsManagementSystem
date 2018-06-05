package cn.edu.nju.charlesfeng.util.enums;

/**
 * 场馆可以举行的计划
 * 修改此处类型之后，前端展示也需要对应修改
 * （/src/views/schedule/index.vue:22,
 * /src/views/schedule/oneSchedule.vue:144,
 * /src/views/schedule/newSchedule/step1.vue:25）
 */
public enum ProgramType {

    VOCALCONCERT("演唱会"),
    DRAMA("话剧歌剧"),
    SPORT("体育赛事"),
    CONCERT("音乐会"),
    PARENTCHILD("儿童亲子"),
    DANCE("舞蹈芭蕾"),
    EXHIBITION("展览休闲"),
    QUYITALK("曲艺杂谈");

    private String val;

    ProgramType(String val) {
        this.val = val;
    }

    public static ProgramType getEnum(String val) {
        for (ProgramType curType : ProgramType.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
