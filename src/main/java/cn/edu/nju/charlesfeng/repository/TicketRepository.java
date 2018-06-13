package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据层对票的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface TicketRepository extends JpaRepository<Ticket, TicketID> {

    @Query(value = "select count(t) from Ticket t where t.ticketID.programID=:programID and t.isLock=:state")
    int hasTickets(@Param("programID") ProgramID programID, @Param("state") boolean isLock);

    @Query(value = "select t from Ticket t where t.ticketID.programID=:programID and t.seatType=:seatType and t.isLock=:state")
    List<Ticket> getUnLockTickets(@Param("programID") ProgramID programID, @Param("seatType") String seatType, @Param("state") boolean isLock);

    @Query(value = "select t from Ticket t where t.ticketID in :ticketIDs")
    List<Ticket> getUnLockTickets(@Param("ticketIDs") List<TicketID> ticketIDS);
}
