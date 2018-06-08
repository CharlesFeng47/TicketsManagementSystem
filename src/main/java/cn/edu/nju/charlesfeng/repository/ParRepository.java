package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.id.ParID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据层对票面的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface ParRepository extends JpaRepository<Par, ParID> {
}
