package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Par;
import cn.edu.nju.charlesfeng.model.id.ParID;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据层对票面的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface ParRepository extends JpaRepository<Par, ParID> {

    @Query(value = "select p.base_price from par as p where p.vid=:venueID and p.start_time=:start_time and p.seat_type=:type", nativeQuery = true)
    double findPrice(@Param("venueID") int venueID, @Param("start_time") LocalDateTime time, @Param("type") String type);

}
