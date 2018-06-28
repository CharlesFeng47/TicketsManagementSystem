package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Ticket;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.model.id.TicketID;
import cn.edu.nju.charlesfeng.repository.TicketRepository;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.exceptions.TicketsNotAdequateException;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * 根据节目ID获取节目的售票状态
     *
     * @param programID 节目ID
     * @return 售票状态
     */
    @Override
    public SaleType getProgramSaleType(ProgramID programID) {
        int result = ticketRepository.hasTickets(programID, false);
        if (result != 0) {
            return SaleType.TICKETING;
        } else {
            return SaleType.REPLACEMENTTICKETING;
        }
    }

    /**
     * 根据节目ID获取节目的余票数量
     *
     * @param programID 节目ID
     * @return 数量
     */
    @Override
    public int getProgramRemainTicketNumber(ProgramID programID) {
        return ticketRepository.hasTickets(programID, false);
    }

    /**
     * 立即购买锁票(后面可能要加锁)
     *
     * @param programID 节目ID
     * @param num       数量
     * @return 锁票后的票列表
     */
    @Override
    public List<Ticket> lock(ProgramID programID, int num, String seatType) throws TicketsNotAdequateException {
        List<Ticket> unlockTickets = ticketRepository.getUnLockTickets(programID, seatType, false);
        if (unlockTickets.size() < num) {
            throw new TicketsNotAdequateException();
        }
        List<Ticket> result = unlockTickets.subList(0, num);
        for (Ticket ticket : result) {
            ticket.setLock(true);
        }
        return ticketRepository.saveAll(result);
    }

    /**
     * 选座购买锁票
     *
     * @param ticketIDS 需要锁票的ID列表
     * @return 锁票后的票列表
     */
    @Override
//    @Transactional
    public List<Ticket> lock(List<TicketID> ticketIDS) {
        List<Ticket> tickets = ticketRepository.getUnLockTickets(ticketIDS);
        for (Ticket ticket : tickets) {
            ticket.setLock(true);
        }
        return ticketRepository.saveAll(tickets);
    }
}
