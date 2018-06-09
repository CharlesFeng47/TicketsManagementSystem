package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.exceptions.ProgramNotSettlableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProgramService {

    /**
     * 获取首页的每种类型的5个推荐节目
     *
     * @return Map<ProgramType   ,       List   <   Program>>
     */
    Map<ProgramType, List<Program>> recommendPrograms(LocalDateTime localDateTime, String city, int num);

    /**
     * @return 所有日程
     */
    List<Program> getAllPrograms();

    /**
     * @return 用户可见的所有日程（即不含已过期的日程计划）
     */
    List<Program> getAllAvailablePrograms();

    /**
     * @param venueID 欲获取的相关场馆的场馆Id
     * @return 相关场馆的所有日程
     */
    List<Program> getProgramsOfOneVenue(int venueID);

    /**
     * @param programID 欲获取的日程Id
     * @return 日程详情实体
     */
    Program getOneProgram(ProgramID programID);

    /**
     * @param program 欲发布的活动日程描述
     * @return 是否成功发布，成功则为该实体对象
     */
    Program publishProgram(Program program);

    /**
     * @param program 欲修改的活动日程描述
     * @return 是否成功修改，成功则true
     */
    boolean modifyProgram(Program program);

    /**
     * @param programID 要结算的计划ID
     * @return 结算结果，成功则true
     */
    boolean settleOneProgram(ProgramID programID) throws ProgramNotSettlableException;
}
