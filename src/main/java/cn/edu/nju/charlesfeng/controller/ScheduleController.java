package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentSchedule;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    public RequestReturnObject getSchedules(@PathVariable int sid) {
        return null;
    }

    /**
     * @return 某一条日程的详情
     */
    @GetMapping("/{id}")
    public RequestReturnObject getOneSchedule(@PathVariable("id") String id, HttpServletResponse response) {
        logger.debug("INTO /schedule/" + id);
        Schedule resultSchedule = scheduleService.getOneSchedule(Integer.parseInt(id));
        try {
            Spot relativeSpot = (Spot) userService.getUser(resultSchedule.getSpotId(), UserType.SPOT);
            return new RequestReturnObject(RequestReturnObjectState.OK, new ContentSchedule(resultSchedule, relativeSpot));
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }
}
