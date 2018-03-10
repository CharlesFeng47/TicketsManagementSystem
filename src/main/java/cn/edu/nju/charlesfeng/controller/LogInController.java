package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.entity.SeatInfo;
import cn.edu.nju.charlesfeng.entity.Spot;
import cn.edu.nju.charlesfeng.model.ContentUser;
import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserHasBeenSignUpException;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 控制登录页面
 */
@RestController
@RequestMapping("/login")
public class LogInController {

    private static final Logger logger = Logger.getLogger(LogInController.class);

    private final LogInService logInService;

    @Autowired
    public LogInController(LogInService logInService) {
        this.logInService = logInService;
    }

    /**
     * 用户登录
     * // TODO Token的算法
     *
     * @return 该用户对应的token
     */
    @PostMapping
    public RequestReturnObject login(@RequestParam("username") String uid, @RequestParam("password") String pwd,
                                     @RequestParam("userType") UserType userType, HttpServletRequest request) {
        logger.debug("INTO /login");
        try {
            User curUser = logInService.logIn(uid, pwd, userType);

            String token = userType.toString() + ": " + curUser.getId();
            HttpSession session = request.getSession();
            session.setAttribute(token, curUser);

            return new RequestReturnObject(RequestReturnObjectState.OK, token);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        } catch (WrongPwdException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_PWD_WRONG);
        }
    }

    /**
     * 用户注册
     *
     * @return 该用户对应的token
     */
    @PostMapping("member_sign_up")
    public RequestReturnObject memberSignUp(@RequestParam("username") String uid, @RequestParam("password") String pwd,
                                            @RequestParam("email") String email, HttpServletRequest request) {
        logger.debug("INTO /login/member_sign_up");
        try {
            User curUser = logInService.registerMember(uid, pwd, email);

            String token = "MEMBER: " + curUser.getId();
            HttpSession session = request.getSession();
            session.setAttribute(token, curUser);
            return new RequestReturnObject(RequestReturnObjectState.OK, token);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        } catch (UserHasBeenSignUpException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_HAS_BEEN_SIGN_UP);
        }
    }

    /**
     * 场馆注册
     *
     * @return 该场馆对应的token
     */
    @PostMapping("spot_sign_up")
    public RequestReturnObject spotSignUp(@RequestParam("password") String pwd, @RequestParam("name") String spotName,
                                          @RequestParam("site") String site, @RequestParam("seatInfos") String seatInfosJson,
                                          @RequestParam("seatsMap") String seatsMapJson, @RequestParam("curSeatTypeCount") int curSeatTypeCount,
                                          HttpServletRequest request) {
        logger.debug("INTO /login/spot_sign_up");

        List<SeatInfo> seatInfos = JSON.parseArray(seatInfosJson, SeatInfo.class);

        try {
            Spot curSpot = logInService.registerSpot(pwd, spotName, site, seatInfos, seatsMapJson, curSeatTypeCount);
            String token = "SPOT: " + curSpot.getId();
            HttpSession session = request.getSession();
            session.setAttribute(token, curSpot);
            return new RequestReturnObject(RequestReturnObjectState.OK, token);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }

    /**
     * 用户根据Token获取相应名字和角色
     */
    @PostMapping("info")
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /login/info: " + token);
        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute(token);

        UserType curUserType = UserType.valueOf(token.split(":")[0]);
        ContentUser contentUser = new ContentUser(curUser, curUserType);
        return new RequestReturnObject(RequestReturnObjectState.OK, contentUser);
    }

    /**
     * 用户登出
     */
    @PostMapping("logout")
    public RequestReturnObject logout(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /login/logout");
        HttpSession session = request.getSession();
        session.setAttribute(token, null);
        return new RequestReturnObject(RequestReturnObjectState.OK);
    }
}
