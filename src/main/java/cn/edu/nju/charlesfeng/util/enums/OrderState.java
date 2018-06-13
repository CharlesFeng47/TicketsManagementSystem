package cn.edu.nju.charlesfeng.util.enums;

/**
 * 订单状态
 */
public enum OrderState {

    UNPAID("未支付"),
    PAYED("已支付"),
    ORDERED("已下单"),
    REFUND("已退款"),
    EXPIRED("已过期"),
    CANCELLED("逾期未付款自动取消"),
    DISPATCH_FAIL("配票失败"),
    CHECKED("已检票");

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
