package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.exceptions.venue.ProgramNotSettlableException;
import cn.edu.nju.charlesfeng.util.filter.program.PreviewSearchResult;
import cn.edu.nju.charlesfeng.util.filter.program.ProgramBrief;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProgramService {

    /**
     * 获取首页的每种类型的5个推荐节目
     *
     * @return Map<ProgramType                                                                                                                               ,                                                                                                                                                                                                                                                               List                                                                                                                               <                                                                                                                               Program>>
     */
    Map<String, List<Program>> recommendPrograms(LocalDateTime localDateTime, String city, int num);

    /**
     * 按类型获取节目
     *
     * @param city          制定城市
     * @param programType   节目类型
     * @param localDateTime 指定时间
     * @return 节目列表
     */
    List<ProgramBrief> getBriefPrograms(String city, ProgramType programType, LocalDateTime localDateTime);

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
     * 根据场馆ID，节目名获取所有场次
     *
     * @param venueID 场馆ID
     * @param name    节目名
     * @return 场次
     */
    Set<LocalDateTime> getAllProgramField(int venueID, String name);

    /**
     * 根据条件进行模糊搜索节目
     *
     * @param condition 条件
     * @return 节目简介列表
     */
    Set<Program> search(String condition);

    /**
     * 根据条件进行模糊预搜索节目
     *
     * @param condition 条件
     * @return 节目简介列表
     */
    List<PreviewSearchResult> previewSearch(String condition, int result_num);

    /**
     * 对节目的浏览量加1
     *
     * @param programID 节目ID
     */
    void addScanVolume(ProgramID programID);

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
