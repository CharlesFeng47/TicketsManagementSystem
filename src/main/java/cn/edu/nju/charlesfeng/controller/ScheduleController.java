package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import com.alibaba.fastjson.JSON;
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

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
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
        Schedule resultSchedule = scheduleService.getOneSchedule(Integer.parseInt(id));
        System.out.println(JSON.toJSONString(resultSchedule));
        return new RequestReturnObject(RequestReturnObjectState.OK, resultSchedule);
    }
}
