package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 控制登录页面
 */
@RestController
@RequestMapping("/login")
public class LogInController {

    private static final Logger logger = Logger.getLogger(LogInController.class);

    private final LogInService logInService;

    private final UserService userService;

    @Autowired
    public LogInController(UserService userService, LogInService logInService) {
        this.userService = userService;
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
     * 用户获取Token
     */
    @PostMapping("info")
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /login/info");
        System.out.println(token);
        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute(token);
        return new RequestReturnObject(RequestReturnObjectState.OK, curUser);
    }

    /**
     * 用户登出
     */
    @PostMapping("logout")
    public RequestReturnObject login(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /login/logout");
        HttpSession session = request.getSession();
        session.setAttribute(token, null);
        return new RequestReturnObject(RequestReturnObjectState.OK);
    }
}
