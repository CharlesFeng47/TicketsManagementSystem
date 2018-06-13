package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据层对节目的操作,该类的操作为基本实现，可以不用自己实现(据之前的方法得知，可能会改)
 */
public interface ProgramRepository extends JpaRepository<Program, ProgramID> {

    Program findByProgramID(ProgramID programID);

    List<Program> findByName(String name);

    @Query(value = "select p.programID.startTime from Program p where p.programID.venueID=:venueID and p.name=:programName")
    List<LocalDateTime> findField(@Param("venueID") int venueID, @Param("programName") String name);

    @Query(value = "select p from Program p where p.programID.venueID=:venueID")
    List<Program> findByVenueID(@Param("venueID") int venueID);

    @Query(value = "select p from Program p where p.programType=:programType and p.programID.startTime>=:today and p.venue.address.city=:city")
    Page<Program> getAvailablePrograms(@Param("today") LocalDateTime today, @Param("programType") ProgramType programType, @Param("city") String city, Pageable pageable);

    @Query(value = "select p from Program p where p.programType=:programType and p.programID.startTime>=:today and p.venue.address.city=:city")
    List<Program> getAvailablePrograms(@Param("today") LocalDateTime today, @Param("programType") ProgramType programType, @Param("city") String city);

    @Query(value = "select p from Program p where concat(p.name, p.venue.venueName) like:info")
    List<Program> searchProgram(@Param("info") String info);

    @Query(value = "select p.programID, p.name from Program p where concat(p.name, p.venue.venueName) like:info")
    List<Object[]> previewSearchProgram(@Param("info") String info);
}
