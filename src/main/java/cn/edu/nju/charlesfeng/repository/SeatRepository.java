package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Seat;
import cn.edu.nju.charlesfeng.model.id.SeatID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据层对场馆座位的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface SeatRepository extends JpaRepository<Seat, SeatID> {

    @Query(value = "select distinct s.type from seat as s where s.vid=:venueID", nativeQuery = true)
    List<String> getType(@Param("venueID") int venueID);
}
