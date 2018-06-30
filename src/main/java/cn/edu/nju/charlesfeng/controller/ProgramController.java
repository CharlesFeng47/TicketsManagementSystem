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
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 有关计划／日程的前端控制器
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
     * @return 首页的节目推荐
     */
    @GetMapping("/recommend")
    public Map<String, List<ProgramBriefDTO>> getRecommendPrograms(@RequestParam("city") String city) {
        logger.debug("INTO /program/recommend?city=" + city);
        //今天之后包括今天
        Map<String, List<Program>> map = programService.recommendPrograms(LocalDateTime.now(), city, 5);
        Map<String, List<ProgramBriefDTO>> result = new HashMap<>();
        for (String key : map.keySet()) {
            List<Program> programs = map.get(key);
            List<ProgramBriefDTO> programBriefDTOS = new ArrayList<>();
            for (Program program : programs) {
                programBriefDTOS.add(new ProgramBriefDTO(program));
            }
            result.put(key, programBriefDTOS);
        }
        return result;
    }

    /**
     * @return 根据节目类型获取节目列表
     */
    @GetMapping("/getProgramsByType")
    public List<ProgramBriefDTO> getProgramsByType(@RequestParam("city") String city, @RequestParam("program_type") String programType) {
        logger.debug("INTO /program/getProgramsByType?city=" + city + "&program_type=" + programType);
        //今天之后包括今天
        return programService.getBriefPrograms(city, ProgramType.getEnum(programType), LocalDateTime.now());
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
        return new ProgramDetailDTO(program, saleType, fields, number, false);
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
        Set<Program> programs = programService.search(conditions);
        List<ProgramBriefDTO> result = new ArrayList<>();
        for (Program program : programs) {
            SaleType saleType = ticketService.getProgramSaleType(program.getProgramID());
            result.add(new ProgramBriefDTO(program, saleType));
        }
        return result;
    }

    /**
     * @return 预搜索
     */
    @GetMapping("/previewSearch")
    public List<PreviewSearchResultDTO> previewSearch(@RequestParam("conditions") String conditions) {
        logger.debug("INTO /program/previewSearch?conditions=" + conditions);
        return programService.previewSearch(conditions, 10);
    }

//    /**
//     * 保存单条计划
//     */
//    @PostMapping("save")
//    public RequestReturnObject saveOneSchedule(@RequestParam("token") String token, @RequestParam("name") String name,
//                                               @RequestParam("dateStr") String dateString, @RequestParam("timeStr") String timeString,
//                                               @RequestParam("type") ProgramType scheduleItemType, @RequestParam("description") String description,
//                                               @RequestParam("nameListStr") String nameListJson, @RequestParam("priceListStr") String priceListJson,
//                                               HttpServletRequest request) throws SpotSeatDisorderException {
//        logger.debug("INTO /schedule/save");
//        HttpSession session = request.getSession();
////        Spot curSpot = (Spot) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Spot;
//        Spot curSpot = (Spot) o;
//
//        // 时间相关的处理
//        LocalDateTime dateTime = convertDateTime(dateString, timeString);
//        // 价格对应表的处理
//        String seatInfoPricesJson = convertPriceMapJson(nameListJson, priceListJson, curSpot);
//
//        Schedule result = scheduleService.publishSchedule(name, curSpot, dateTime, scheduleItemType, seatInfoPricesJson, description);
//        return new RequestReturnObject(RequestReturnObjectState.OK, result);
//    }
//
//    /**
//     * 保存单条计划
//     */
//    @PostMapping("modify")
//    public RequestReturnObject modifyOneSchedule(@RequestParam("token") String token, @RequestParam("scheduleId") String scheduleId,
//                                                 @RequestParam("name") String name, @RequestParam("dateStr") String dateString,
//                                                 @RequestParam("timeStr") String timeString, @RequestParam("type") ProgramType scheduleItemType,
//                                                 @RequestParam("description") String description, @RequestParam("nameListStr") String nameListJson,
//                                                 @RequestParam("priceListStr") String priceListJson, HttpServletRequest request) throws SpotSeatDisorderException {
//        logger.debug("INTO /schedule/modify");
//        HttpSession session = request.getSession();
////        Spot curSpot = (Spot) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Spot;
//        Spot curSpot = (Spot) o;
//
//        // 时间相关的处理
//        LocalDateTime dateTime = convertDateTime(dateString, timeString);
//        // 价格对应表的处理
//        String seatInfoPricesJson = convertPriceMapJson(nameListJson, priceListJson, curSpot);
//
//        boolean result = scheduleService.modifySchedule(scheduleId, name, curSpot, dateTime, scheduleItemType, seatInfoPricesJson, description);
//        if (result) return new RequestReturnObject(RequestReturnObjectState.OK);
//        else return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//    }
//
//    /**
//     * @return 获取日程对应的简介
//     */
//    private List<ContentScheduleBrief> getBrief(List<Schedule> schedules) {
//        List<ContentScheduleBrief> result = new LinkedList<>();
//        for (Schedule schedule : schedules) {
//            result.add(new ContentScheduleBrief(schedule));
//        }
//        return result;
//    }
//
//    /**
//     * 将前端发回的日期时间String转换为LocalDateTime
//     */
//    private LocalDateTime convertDateTime(String dateString, String timeString) {
//        String[] dateParts = dateString.split("-");
//        String[] timeParts = timeString.split(":");
//        LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
//        LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
//        return LocalDateTime.of(date, time);
//    }
//
//    /**
//     * 将前端发回的座位名称、价格形成map映射，并以json串的形式返回
//     */
//    private String convertPriceMapJson(String nameListJson, String priceListJson, Spot curSpot) throws SpotSeatDisorderException {
//        List<String> nameList = JSON.parseArray(nameListJson, String.class);
//        List<Double> priceList = JSON.parseArray(priceListJson, Double.class);
//        Map<SeatInfo, Double> priceMap = new LinkedHashMap<>();
//
//        // 按顺序发过去，按顺序接受，理应是顺序一样的，以防万一出错顺序不对，抛出异常
//        List<SeatInfo> seatInfos = curSpot.getSeatInfos();
//        for (int i = 0; i < seatInfos.size(); i++) {
//            SeatInfo curSeatInfo = seatInfos.get(i);
//            if (!curSeatInfo.getSeatName().equals(nameList.get(i))) throw new SpotSeatDisorderException();
//            else {
//                priceMap.put(curSeatInfo, priceList.get(i));
//            }
//        }
//        return JSON.toJSONString(priceMap);
//    }
}
