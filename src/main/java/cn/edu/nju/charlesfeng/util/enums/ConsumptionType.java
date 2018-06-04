package cn.edu.nju.charlesfeng.util.enums;

/**
 * 消费记录的类型
 */
public enum ConsumptionType {

    SUBSCRIBE("预订"),
    UNSUBSCRIBE("退订");

    private String val;

    ConsumptionType(String val) {
        this.val = val;
    }

    public static ConsumptionType getEnum(String val) {
        for (ConsumptionType curType : ConsumptionType.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
