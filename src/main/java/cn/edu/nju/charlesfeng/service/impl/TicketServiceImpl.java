package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.TicketRepository;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        int result = ticketRepository.hasTickets(programID);
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
        return ticketRepository.hasTickets(programID);
    }
}
