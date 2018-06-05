package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数据层对用户的服务,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface UserRepository extends JpaRepository<User, String> {
}
