package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.RequestReturnObject;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.LogInService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.enums.UserType;
import cn.edu.nju.charlesfeng.util.exceptions.UserNotExistException;
import cn.edu.nju.charlesfeng.util.exceptions.WrongPwdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 控制登录页面
 */
@RestController
@RequestMapping("/user")
public class LogInController {

    private final LogInService logInService;

    private final UserService userService;

    @Autowired
    public LogInController(UserService userService, LogInService logInService) {
        this.userService = userService;
        this.logInService = logInService;
    }

    @GetMapping
    public RequestReturnObject getUser() {
        try {
            return new RequestReturnObject(RequestReturnObjectState.OK, userService.getUser("admin", UserType.MANAGER));
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        }
    }

    @PostMapping("login")
    public RequestReturnObject login(@RequestParam("username") String uid, @RequestParam("password") String pwd,
                                     @RequestParam("userType") UserType userType) {
        try {
            User curUser = logInService.logIn(uid, pwd, userType);
            return new RequestReturnObject(RequestReturnObjectState.OK, curUser);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        } catch (WrongPwdException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_PWD_WRONG);
        }
    }
}
