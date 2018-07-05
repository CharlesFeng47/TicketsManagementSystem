package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据层对节目的操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface ProgramRepository extends JpaRepository<Program, ProgramID> {

    Program findByProgramID(ProgramID programID);

    /**
     * 获取轮播图的节目
     *
     * @param venueID   场馆ID
     * @param startTime 节目开始时间
     * @return 节目ID
     */
    @Query(value = "select p.vid, p.start_time from (select p1.vid,p1.name from program as p1 where p1.vid=:venueID and p1.start_time=:startTime) as t join program as p on t.vid=p.vid and t.name=p.name WHERE p.start_time>=NOW() order by p.start_time LIMIT 1 ", nativeQuery = true)
    Object[][] getSowingProgramID(@Param("venueID") int venueID, @Param("startTime") LocalDateTime startTime);

    List<Program> findByName(String name);

    @Query(value = "SELECT p1.vid, min(p1.start_time) as start_time from program as p1 join venue as v on p1.vid=v.vid WHERE p1.start_time>=NOW() and p1.type=:programType and v.city=:city GROUP BY p1.vid, p1.`name` order by start_time", nativeQuery = true)
    List<Object[]> getAvailableProgramIds(@Param("programType") int programType, @Param("city") String city);

    @Query(value = "select p.programID.startTime from Program p where p.programID.venueID=:venueID and p.name=:programName and p.programID.startTime>=:startTime")
    List<LocalDateTime> findField(@Param("venueID") int venueID, @Param("programName") String name, @Param("startTime")LocalDateTime startTime);

    @Query(value = "select p from Program p where p.programID.venueID=:venueID")
    List<Program> findByVenueID(@Param("venueID") int venueID);

    @Query(value = "select p from Program p where p.programType=:programType and p.programID.startTime>=:today and p.venue.address.city=:city")
    Page<Program> getAvailablePrograms(@Param("today") LocalDateTime today, @Param("programType") ProgramType programType, @Param("city") String city, Pageable pageable);

    @Query(value = "select p from Program p where p.programType=:programType and p.programID.startTime>=:today and p.venue.address.city=:city")
    List<Program> getAvailablePrograms(@Param("today") LocalDateTime today, @Param("programType") ProgramType programType, @Param("city") String city);

    @Query(value = "select t.vid, t.start_time from (select p.vid, min(p.start_time) as start_time, v.`name` as venueName, p.`name` as programName from program p join venue v on p.vid=v.vid WHERE p.start_time>=NOW() GROUP BY p.vid, p.`name`) as t WHERE CONCAT(t.venueName,t.programName) like :info", nativeQuery = true)
    List<Object[]> searchProgram(@Param("info") String info);

    @Query(value = "select t.vid, t.start_time, t.programName from (select p.vid, min(p.start_time) as start_time, v.`name` as venueName, p.`name` as programName from program p join venue v on p.vid=v.vid WHERE p.start_time>=NOW() GROUP BY p.vid, p.`name`) as t WHERE CONCAT(t.venueName,t.programName) like :info", nativeQuery = true)
    List<Object[]> previewSearchProgram(@Param("info") String info);

    @Modifying
    @Transactional
    @Query(value = "update program set scan = scan + 1 WHERE vid=:venueID and start_time=:startTime", nativeQuery = true)
    void addOneScanVolume(@Param("venueID") int venueID, @Param("startTime") LocalDateTime time);

    /**
     * test method
     */
    @Query(value = "select p from Program p where p.venue.address.city=:city and p.programID.startTime<:start_time")
    List<Program> getBeforeProrgam(@Param("city") String city, @Param("start_time") LocalDateTime localDateTime);
}
