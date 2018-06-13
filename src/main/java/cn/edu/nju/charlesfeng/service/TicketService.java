package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.exceptions.TicketsNotAdequateException;

import java.util.List;

public interface TicketService {

    /**
     * 根据节目ID获取节目的售票状态
     *
     * @param programID
     * @return
     */
    SaleType getProgramSaleType(ProgramID programID);

    /**
     * 根据节目ID获取节目的余票数量
     *
     * @param programID 节目ID
     * @return 数量
     */
    int getProgramRemainTicketNumber(ProgramID programID);

    /**
     * 立即购买锁票
     *
     * @param programID 节目ID
     * @param num       数量
     * @return 锁票后的票列表
     */
    List<Ticket> lock(ProgramID programID, int num, String seatType) throws TicketsNotAdequateException;

    /**
     * 选座购买锁票
     *
     * @param ticketIDS 需要锁票的ID列表
     * @return 锁票后的票列表
     */
    List<Ticket> lock(List<TicketID> ticketIDS);
}
