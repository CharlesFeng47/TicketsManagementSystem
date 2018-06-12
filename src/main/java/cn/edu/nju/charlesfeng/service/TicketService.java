package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.SaleType;

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

}
