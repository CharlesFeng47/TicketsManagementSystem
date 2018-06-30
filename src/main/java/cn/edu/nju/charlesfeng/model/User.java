package cn.edu.nju.charlesfeng.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 系统的用户实体
 */
@Entity
@Table(name = "user")
public class User implements Serializable, Comparable<User> {

    /**
     * 用户ID，邮箱
     */
    @Id
    @GenericGenerator(name = "myGenerator", strategy = "assigned")
    @GeneratedValue(generator = "myGenerator")
    private String email;

    /**
     * 用户登录密码
     */
    @Column(name = "pwd", nullable = false)
    private String password;

    /**
     * 用户名
     */
    @Column(nullable = false)
    @JSONField(name = "username")
    private String name;

    /**
     * 头像
     */
    @Lob
    @Column(columnDefinition = "longblob")
    private String portrait;

    /**
     * 是否激活
     */
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean activated = false;

    /**
     * 该用户收藏的节目（设为喜欢的节目）(N->N)
     */
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_program",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "email")},
            inverseJoinColumns = {@JoinColumn(name = "program_vid", referencedColumnName = "vid"),
                    @JoinColumn(name = "program_time", referencedColumnName = "start_time")})
    private Set<Program> programs;

    public User() {
    }

    public User(User u) {
        email = u.getEmail();
        password = u.getPassword();
        name = u.getName();
        portrait = u.getPortrait();
        activated = u.isActivated();
    }

    public User(String username, String password, String email) {
        this.name = username;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<Program> programs) {
        this.programs = programs;
    }

    @Override
    public int compareTo(User o) {
        return email.compareTo(o.getEmail());
    }
}
