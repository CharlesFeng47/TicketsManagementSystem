package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.dto.program.PreviewSearchResultDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramBriefDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramDetailDTO;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.exceptions.venue.ProgramNotSettlableException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dong
 */
public interface ProgramService {

    /**
     * 获取首页的每种类型的5个推荐节目
     *
     * @param city 城市
     * @param num  数量
     * @return 推荐节目单
     */
    Map<String, List<ProgramBriefDTO>> recommendPrograms(String city, int num);

    /**
     * 按类型获取节目
     *
     * @param city        制定城市
     * @param programType 节目类型
     * @return 节目列表
     */
    List<ProgramBriefDTO> getBriefPrograms(String city, ProgramType programType);

    /**
     * @param programID 欲获取的日程Id
     * @return 日程详情实体
     */
    Program getOneProgram(ProgramID programID);

    /**
     * 获取轮播图的节目
     *
     * @param preProgramID 节目ID
     * @return Program
     */
    Program getSowingProgram(ProgramID preProgramID);

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
    List<ProgramBriefDTO> search(String condition);

    /**
     * 根据条件进行模糊预搜索节目
     *
     * @param condition 条件
     * @return 节目简介列表
     */
    List<PreviewSearchResultDTO> previewSearch(String condition, int result_num);

    /**
     * 对节目的浏览量加1
     *
     * @param programID 节目ID
     */
    void addScanVolume(ProgramID programID);
}
