package cn.edu.nju.charlesfeng.util.enums;

/**
 * 节目的售票状态
 */
public enum SaleType {

    TICKETING("售票中"),
    REPLACEMENTTICKETING("补票中");

    private String val;

    SaleType(String val) {
        this.val = val;
    }

    public static SaleType getEnum(String val) {
        for (SaleType curType : SaleType.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.val;
    }
}
