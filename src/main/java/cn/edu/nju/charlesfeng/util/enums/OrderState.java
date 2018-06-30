package cn.edu.nju.charlesfeng.util.enums;

/**
 * 订单状态
 *
 * @author Dong
 */
public enum OrderState {

    /**
     * 未支付
     */
    UNPAID("未支付"),
    /**
     * 已支付
     */
    PAYED("已支付"),
    /**
     * 已退款
     */
    REFUND("已退款"),
    /**
     * 已取消
     */
    CANCELLED("已取消"),
    /**
     * 全部
     */
    ALL("全部");

    private String val;

    OrderState(String val) {
        this.val = val;
    }

    public static OrderState getEnum(String val) {
        for (OrderState curType : OrderState.values()) {
            if (curType.val.equals(val)) {
                return curType;
            }
        }
        return null;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }


    @Override
    public String toString() {
        return val;
    }
}
