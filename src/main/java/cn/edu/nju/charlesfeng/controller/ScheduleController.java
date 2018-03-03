package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentSchedule;
import cn.edu.nju.charlesfeng.model.ContentScheduleBrief;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    private final UserService userService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, UserService userService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    /**
     * @return 所有日程的简介
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
        try {
            return new RequestReturnObject(RequestReturnObjectState.OK, getBrief(allSchedules));
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }

    /**
     * @return 某一条日程的详情
     */
    @GetMapping("/{id}")
    public RequestReturnObject getOneSchedule(@PathVariable("id") String id, HttpServletResponse response) {
        logger.debug("INTO /schedule/" + id);
        Schedule resultSchedule = scheduleService.getOneSchedule(id);
        try {
            Spot relativeSpot = (Spot) userService.getUser(resultSchedule.getSpotId(), UserType.SPOT);
            return new RequestReturnObject(RequestReturnObjectState.OK, new ContentSchedule(resultSchedule, relativeSpot));
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
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
     * @return 获取日程对应的简介
     */
    private List<ContentScheduleBrief> getBrief(List<Schedule> schedules) throws UserNotExistException {
        List<ContentScheduleBrief> result = new LinkedList<>();
        Map<String, Spot> spotMap = userService.getAllSpotIdMap();
        for (Schedule schedule : schedules) {
            Spot relativeSpot = spotMap.get(schedule.getSpotId());
            assert relativeSpot != null;
            result.add(new ContentScheduleBrief(schedule, relativeSpot));
        }
        return result;
    }
}
