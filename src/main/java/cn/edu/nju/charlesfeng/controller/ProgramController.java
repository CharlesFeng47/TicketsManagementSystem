package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.dto.program.PreviewSearchResultDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramBriefDTO;
import cn.edu.nju.charlesfeng.dto.program.ProgramDetailDTO;
import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.service.TicketService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.ProgramType;
import cn.edu.nju.charlesfeng.util.enums.SaleType;
import cn.edu.nju.charlesfeng.util.helper.SowingHelper;
import cn.edu.nju.charlesfeng.util.helper.SystemHelper;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 有关计划／日程的前端控制器
 *
 * @author Dong
 */
@RestController
@RequestMapping("/program")
public class ProgramController {

    private static final Logger logger = Logger.getLogger(ProgramController.class);

    private final ProgramService programService;

    private final TicketService ticketService;

    private final UserService userService;

    @Autowired
    public ProgramController(ProgramService programService, TicketService ticketService, UserService userService) {
        this.programService = programService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    /**
     * @return 轮播图url获取(realId - > url)
     */
    @GetMapping("/getSowingMapUrl")
    public Map<String, String> getSowingMapUrl(@RequestParam("city") String city) {
        logger.debug("INTO /program/getSowingMapUrl?city=" + city);

        try {
            Map<String, String> result = new TreeMap<>();
            String domainName = SystemHelper.getDomainName();
            List<String> ids = SowingHelper.readAreaSowingID(city);
            for (String id : ids) {
                String[] preIds = id.split("-");
                ProgramID programID = new ProgramID();
                programID.setVenueID(Integer.parseInt(preIds[0]));
                programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(preIds[1])));
                String realID = programService.getSowingProgram(programID);
                if (realID == null) {
                    continue;
                }
                String url = domainName + "/SOWINGMAP/" + id + ".jpg";
                result.put(realID, url);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 首页的节目推荐
     */
    @GetMapping("/recommend")
    public Map<String, List<ProgramBriefDTO>> getRecommendPrograms(@RequestParam("city") String city) {
        logger.debug("INTO /program/recommend?city=" + city);
        //今天之后包括今天
        return programService.recommendPrograms(city, 5);
    }

    /**
     * @return 根据节目类型获取节目列表
     */
    @GetMapping("/getProgramsByType")
    public List<ProgramBriefDTO> getProgramsByType(@RequestParam("city") String city, @RequestParam("program_type") String programType) {
        logger.debug("INTO /program/getProgramsByType?city=" + city + "&program_type=" + programType);
        //今天之后包括今天
        return programService.getBriefPrograms(city, ProgramType.getEnum(programType));
    }

    /**
     * @return 根据节目ID获取节目详情
     */
    @GetMapping("/getProgramDetail")
    public ProgramDetailDTO getProgramDetail(@RequestParam("program_id") String programIDString) {
        logger.debug("INTO /program/getProgramDetail?program_id" + programIDString);

        String[] ids = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(ids[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(ids[1])));
        Program program = programService.getOneProgram(programID);

        SaleType saleType = ticketService.getProgramSaleType(programID);
        Set<LocalDateTime> fields = programService.getAllProgramField(programID.getVenueID(), program.getName());
        int number = ticketService.getProgramRemainTicketNumber(programID);
        //浏览量加1
        programService.addScanVolume(program.getProgramID());
        return new ProgramDetailDTO(program, saleType, fields, number);
    }

    /**
     * @return 根据节目ID判断是否喜欢该节目
     */
    @PostMapping("/isLikeProgram")
    public Boolean isLikeProgram(@RequestParam("program_id") String programIDString, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /program/isLikeProgram?program_id" + programIDString);

        String ids[] = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(ids[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(ids[1])));
        Program program = programService.getOneProgram(programID);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        return userService.isLike(user.getEmail(), program);
    }

    /**
     * @return 模糊搜索（支持空格）
     */
    @GetMapping("/search")
    public List<ProgramBriefDTO> search(@RequestParam("conditions") String conditions) {
        logger.debug("INTO /program/search?conditions=" + conditions);
        return programService.search(conditions);
    }

    /**
     * @return 预搜索
     */
    @GetMapping("/previewSearch")
    public List<PreviewSearchResultDTO> previewSearch(@RequestParam("conditions") String conditions) {
        logger.debug("INTO /program/previewSearch?conditions=" + conditions);
        return programService.previewSearch(conditions, 10);
    }
}
