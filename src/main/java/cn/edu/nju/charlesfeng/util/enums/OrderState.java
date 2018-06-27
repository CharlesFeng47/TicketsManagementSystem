package cn.edu.nju.charlesfeng.util.enums;

/**
 * 订单状态
 */
public enum OrderState {

    UNPAID("未支付"),
    PAYED("已支付"),
    REFUND("已退款"),
    CANCELLED("已取消"),
    ALL("全部");

    private String val;

    OrderState(String val) {
        this.val = val;
    }

    public static OrderState getEnum(String val) {
        for (OrderState curType : OrderState.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.val;
    }

}
