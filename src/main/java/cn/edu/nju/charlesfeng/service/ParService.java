package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.id.ProgramID;

public interface ParService {

    /**
     * 根据节目ID和座位类型获取该类型的票价
     *
     * @param programID 节目ID
     * @param seatType  座位类型
     * @return 票价
     */
    double getSeatPrice(ProgramID programID, String seatType);

}
