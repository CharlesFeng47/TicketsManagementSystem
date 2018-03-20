package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentSchedule;
import cn.edu.nju.charlesfeng.model.ContentScheduleBrief;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.ScheduleItemType;
import cn.edu.nju.charlesfeng.util.exceptions.SpotSeatDisorderException;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 有关计划／日程的前端控制器
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private static final Logger logger = Logger.getLogger(ScheduleController.class);

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * @return 所有日程／某一场馆的所有日程的简介
     */
    @GetMapping("all")
    public RequestReturnObject getSchedules(String spotId) {
        List<Schedule> allSchedules;
        if (spotId == null || spotId.equals("")) {
            logger.debug("INTO /schedule/all");
            allSchedules = scheduleService.getAllSchedules();
        } else {
            logger.debug("INTO /schedule/all?spotId=" + spotId);
            allSchedules = scheduleService.getSchedulesOfOneSpot(spotId);
        }
        return new RequestReturnObject(RequestReturnObjectState.OK, getBrief(allSchedules));
    }

    /**
     * @return 某一条日程的详情
     */
    @GetMapping("/{id}")
    public RequestReturnObject getOneSchedule(@PathVariable("id") String id) {
        logger.debug("INTO /schedule/" + id);
        Schedule resultSchedule = scheduleService.getOneSchedule(id);
        return new RequestReturnObject(RequestReturnObjectState.OK, new ContentSchedule(resultSchedule));
    }

    /**
     * 删除单条计划
     */
    @PostMapping("delete")
    public RequestReturnObject deleteOneSchedule(@RequestParam("scheduleId") String scheduleId) {
        logger.debug("INTO /schedule/delete: " + scheduleId);
        if (scheduleService.deleteOneSchedule(scheduleId)) {
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } else {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }

    /**
     * 保存单条计划
     */
    @PostMapping("save")
    public RequestReturnObject saveOneSchedule(@RequestParam("token") String token, @RequestParam("name") String name,
                                               @RequestParam("dateStr") String dateString, @RequestParam("timeStr") String timeString,
                                               @RequestParam("type") ScheduleItemType scheduleItemType, @RequestParam("description") String description,
                                               @RequestParam("nameListStr") String nameListJson, @RequestParam("priceListStr") String priceListJson,
                                               HttpServletRequest request) throws SpotSeatDisorderException {
        logger.debug("INTO /schedule/save");
        HttpSession session = request.getSession();
        Spot curSpot = (Spot) session.getAttribute(token);

        // 时间相关的处理
        LocalDateTime dateTime = convertDateTime(dateString, timeString);
        // 价格对应表的处理
        String seatInfoPricesJson = convertPriceMapJson(nameListJson, priceListJson, curSpot);

        Schedule result = scheduleService.publishSchedule(name, curSpot, dateTime, scheduleItemType, seatInfoPricesJson, description);
        return new RequestReturnObject(RequestReturnObjectState.OK, result);
    }

    /**
     * 保存单条计划
     */
    @PostMapping("modify")
    public RequestReturnObject modifyOneSchedule(@RequestParam("token") String token, @RequestParam("scheduleId") String scheduleId,
                                                 @RequestParam("name") String name, @RequestParam("dateStr") String dateString,
                                                 @RequestParam("timeStr") String timeString, @RequestParam("type") ScheduleItemType scheduleItemType,
                                                 @RequestParam("description") String description, @RequestParam("nameListStr") String nameListJson,
                                                 @RequestParam("priceListStr") String priceListJson, HttpServletRequest request) throws SpotSeatDisorderException {
        logger.debug("INTO /schedule/modify");
        HttpSession session = request.getSession();
        Spot curSpot = (Spot) session.getAttribute(token);

        // 时间相关的处理
        LocalDateTime dateTime = convertDateTime(dateString, timeString);
        // 价格对应表的处理
        String seatInfoPricesJson = convertPriceMapJson(nameListJson, priceListJson, curSpot);

        boolean result = scheduleService.modifySchedule(scheduleId, name, curSpot, dateTime, scheduleItemType, seatInfoPricesJson, description);
        if (result) return new RequestReturnObject(RequestReturnObjectState.OK);
        else return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
    }

    /**
     * @return 获取日程对应的简介
     */
    private List<ContentScheduleBrief> getBrief(List<Schedule> schedules) {
        List<ContentScheduleBrief> result = new LinkedList<>();
        for (Schedule schedule : schedules) {
            result.add(new ContentScheduleBrief(schedule));
        }
        return result;
    }

    /**
     * 将前端发回的日期时间String转换为LocalDateTime
     */
    private LocalDateTime convertDateTime(String dateString, String timeString) {
        String[] dateParts = dateString.split("-");
        String[] timeParts = timeString.split(":");
        LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
        LocalTime time = LocalTime.of(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]), Integer.parseInt(timeParts[2]));
        return LocalDateTime.of(date, time);
    }

    /**
     * 将前端发回的座位名称、价格形成map映射，并以json串的形式返回
     */
    private String convertPriceMapJson(String nameListJson, String priceListJson, Spot curSpot) throws SpotSeatDisorderException {
        List<String> nameList = JSON.parseArray(nameListJson, String.class);
        List<Double> priceList = JSON.parseArray(priceListJson, Double.class);
        Map<SeatInfo, Double> priceMap = new LinkedHashMap<>();

        // 按顺序发过去，按顺序接受，理应是顺序一样的，以防万一出错顺序不对，抛出异常
        List<SeatInfo> seatInfos = curSpot.getSeatInfos();
        for (int i = 0; i < seatInfos.size(); i++) {
            SeatInfo curSeatInfo = seatInfos.get(i);
            if (!curSeatInfo.getSeatName().equals(nameList.get(i))) throw new SpotSeatDisorderException();
            else {
                priceMap.put(curSeatInfo, priceList.get(i));
            }
        }
        return JSON.toJSONString(priceMap);
    }
}
