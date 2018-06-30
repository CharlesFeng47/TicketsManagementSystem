package cn.edu.nju.charlesfeng.model.id;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class OrderID implements Serializable {

    /**
     * 用户的ID
     */
    @Column
    private String email;

    /**
     * 订单生成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime time;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderID orderID = (OrderID) o;
        return Objects.equals(email, orderID.email) &&
                Objects.equals(time, orderID.time);
    }

    @Override
    public int hashCode() {

        return Objects.hash(email, time);
    }
}
