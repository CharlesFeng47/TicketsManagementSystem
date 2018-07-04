package cn.edu.nju.charlesfeng.util.enums;

/**
 * 场馆可以举行的计划
 * 修改此处类型之后，前端展示也需要对应修改
 * （/src/views/schedule/index.vue:22,
 * /src/views/schedule/oneSchedule.vue:144,
 * /src/views/schedule/newSchedule/step1.vue:25）
 *
 * @author Dong
 */
public enum ProgramType {

    /**
     * 演唱会
     */
    VOCALCONCERT("演唱会"),

    /**
     * 音乐会
     */
    CONCERT("音乐会"),

    /**
     * 话剧歌剧
     */
    DRAMA("话剧歌剧"),

    /**
     * 体育赛事
     */
    SPORT("体育赛事"),

    /**
     * 舞蹈芭蕾
     */
    DANCE("舞蹈芭蕾"),

    /**
     * 儿童亲自
     */
    PARENTCHILD("儿童亲子"),

    /**
     * 曲艺杂谈
     */
    QUYITALK("曲艺杂谈"),

    /**
     * 展览休闲
     */
    EXHIBITION("展览休闲"),

    /**
     * 首页
     */
    ALL("首页");

    private String val;

    ProgramType(String val) {
        this.val = val;
    }

    public static ProgramType getEnum(String val) {
        for (ProgramType curType : ProgramType.values()) {
            if (curType.val.equals(val)) {
                return curType;
            }
        }
        return null;
    }

    public static int getIndex(ProgramType programType) {
        ProgramType[] programTypes = ProgramType.values();
        for (int i = 0; i < programTypes.length; i++) {
            if (programTypes[i].equals(programType)) {
                return i;
            }
        }
        return -1;
    }

    public static ProgramType get(int index) {
        ProgramType[] programTypes = ProgramType.values();
        return programTypes[index];
    }

    @Override
    public String toString() {
        return this.val;
    }
}
