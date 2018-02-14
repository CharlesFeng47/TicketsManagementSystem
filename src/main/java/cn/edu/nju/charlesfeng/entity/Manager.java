package cn.edu.nju.charlesfeng.entity;

import cn.edu.nju.charlesfeng.model.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统中经理实体
 */
@Entity
@Table(name = "manager")
public class Manager extends User implements Serializable {

    public Manager() {
    }

    public Manager(String id, String pwd) {
        super(id, pwd);
    }
}
