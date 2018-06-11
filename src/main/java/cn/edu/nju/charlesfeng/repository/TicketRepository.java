package cn.edu.nju.charlesfeng.repository;

import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 数据层对票的操作，之前没有相关类的数据层操作，后期根据Service实现
 */
public interface TicketRepository extends JpaRepository<Ticket, TicketID> {

    @Query("select count(t.ticketID) from Ticket t where t.ticketID.programID=:programID")
    int hasTickets(@Param("programID") ProgramID programID);
}
