package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据层对用户的服务,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update user set portrait=:portrait where email=:userID", nativeQuery = true)
    void modifyUserPortrait(@Param("userID") String userID, @Param("portrait") String portrait);

    @Modifying
    @Transactional
    @Query(value = "update user set pwd=:passwd where email=:userID", nativeQuery = true)
    void modifyUserPassword(@Param("userID") String userID, @Param("passwd") String password);

    @Modifying
    @Transactional
    @Query(value = "update user set name=:name where email=:userID", nativeQuery = true)
    void modifyUserName(@Param("userID") String userID, @Param("name") String name);
}
