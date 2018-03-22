package cn.edu.nju.charlesfeng.util.enums;

/**
 * 订单状态
 */
public enum OrderState {

    ORDERED("已下单"),
    CANCELLED("已取消"),
    PAYED("已支付"),
    CHECKED("已检票"),
    EXPIRED("已过期"),
    REFUND("已退款");

    private String val;

    OrderState(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return this.val;
    }

    public static OrderState getEnum(String val) {
        for (OrderState curType : OrderState.values()) {
            if (curType.val.equals(val)) return curType;
        }
        return null;
    }

}
