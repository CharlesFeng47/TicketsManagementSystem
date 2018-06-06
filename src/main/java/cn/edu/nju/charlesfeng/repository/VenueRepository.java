package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 数据层对场馆的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface VenueRepository extends JpaRepository<Venue, Integer> {
}
