package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.Schedule;
import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentSpot;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.ScheduleService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 对用户信息访问的控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final UserService userService;

    private final ScheduleService scheduleService;

    @Autowired
    public UserController(UserService userService, ScheduleService scheduleService) {
        this.userService = userService;
        this.scheduleService = scheduleService;
    }

    /**
     * 根据Token获取当前用户
     */
    @PostMapping
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);
        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute(token);
        return new RequestReturnObject(RequestReturnObjectState.OK, curUser);
    }

    /**
     * @param scheduleId 发布的计划Id
     * @return 单个场馆的信息
     */
    @GetMapping("/spot")
    public RequestReturnObject getOneSpot(String scheduleId, HttpServletResponse response) {
        if (scheduleId == null || scheduleId.equals("")) {
            response.setStatus(404);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } else {
            logger.debug("INTO /user/spot?scheduleId=" + scheduleId);
            try {
                Schedule schedule = scheduleService.getOneSchedule(scheduleId);
                Spot resultSpot = (Spot) userService.getUser(schedule.getSpotId(), UserType.SPOT);
                return new RequestReturnObject(RequestReturnObjectState.OK, new ContentSpot(resultSpot));
            } catch (UserNotExistException e) {
                return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
            }
        }
    }

    /**
     * 场馆修改
     *
     * @return 该场馆对应的token
     */
    @PostMapping("spot_modify")
    public RequestReturnObject spotModify(@RequestParam("id") String spotId, @RequestParam("password") String pwd,
                                          @RequestParam("name") String spotName, @RequestParam("site") String site,
                                          @RequestParam("seatInfos") String seatInfosJson, @RequestParam("seatsMap") String seatsMapJson,
                                          @RequestParam("curSeatTypeCount") int curSeatTypeCount, HttpServletRequest request) {
        logger.debug("INTO /login/spot_sign_up");

        List<SeatInfo> seatInfos = JSON.parseArray(seatInfosJson, SeatInfo.class);

        try {
            boolean result = userService.modifySpot(spotId, pwd, spotName, site, seatInfos, seatsMapJson, curSeatTypeCount);
            if (result) {
                User curSpot = userService.getUser(spotId, UserType.SPOT);
                String token = "SPOT: " + curSpot.getId();
                HttpSession session = request.getSession();
                session.setAttribute(token, curSpot);
                return new RequestReturnObject(RequestReturnObjectState.OK, token);
            } else return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }
}
