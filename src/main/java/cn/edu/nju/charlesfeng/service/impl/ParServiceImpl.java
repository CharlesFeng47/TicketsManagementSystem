package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.ParRepository;
import cn.edu.nju.charlesfeng.service.ParService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParServiceImpl implements ParService {

    private final ParRepository parRepository;

    @Autowired
    public ParServiceImpl(ParRepository parRepository) {
        this.parRepository = parRepository;
    }

    /**
     * 根据节目ID和座位类型获取该类型的票价
     *
     * @param programID 节目ID
     * @param seatType  座位类型
     * @return 票价
     */
    @Override
    public double getSeatPrice(ProgramID programID, String seatType) {
        return parRepository.findPrice(programID, seatType);
    }
}
