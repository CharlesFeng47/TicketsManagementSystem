package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.InteriorWrongException;
import cn.edu.nju.charlesfeng.util.exceptions.member.*;
import cn.edu.nju.charlesfeng.util.filter.program.ProgramBrief;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 对用户信息访问的控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @return 系统服务状态
     */
    @PostMapping("/login")
    public RequestReturnObject login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) throws UserNotExistException, WrongPwdException, UserNotActivatedException {
        logger.debug("INTO /member/login");
        User user = userService.logIn(email, password);
        String token = "USER:" + email;
        HttpSession session = request.getSession();
        session.setAttribute(token, new User(user));
        return new RequestReturnObject(ExceptionCode.OK, token);
    }

    /**
     * 用户注册
     *
     * @return 该用户对应的token
     */
    @PostMapping("/signUp")
    public RequestReturnObject memberSignUp(@RequestParam("username") String username, @RequestParam("password") String password,
                                            @RequestParam("email") String email, HttpServletRequest request) throws UserHasBeenSignUpException, InteriorWrongException {

        logger.debug("INTO /member/user_sign_up");
        User user = new User(username, password, email);
        userService.register(user);
        //TODO 注册后邮箱尚未验证，应该不需要把用户的实体置于session中吧
        String token = "USER: " + user.getEmail();
        HttpSession session = request.getSession();
        session.setAttribute(token, new User(user));
        return new RequestReturnObject(ExceptionCode.OK, token);
    }

    /**
     * @return 邮箱链接验证
     */
    @PostMapping("/userActive")
    public RequestReturnObject verifyUserEmail(@RequestParam("active_url") String activeUrl) {
        logger.debug("INTO /member/userActive");
        System.out.println(activeUrl);
        try {
            userService.activateByMail(activeUrl);
            return new RequestReturnObject(ExceptionCode.OK);
        } catch (UnsupportedEncodingException e) {
            return new RequestReturnObject(ExceptionCode.INTERIOR_WRONG);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(ExceptionCode.MEMBER_ACTIVATE_URL_WRONG);
        } catch (UserActiveUrlExpiredException e) {
            return new RequestReturnObject(ExceptionCode.MEMBER_ACTIVATE_URL_EXPIRE);
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public RequestReturnObject logout(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member/logout");
        HttpSession session = request.getSession();
        session.setAttribute(token, null);
        return new RequestReturnObject(ExceptionCode.OK);
    }

    /**
     * 根据Token获取当前用户
     */
    @PostMapping("/token")
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member/token, token: " + token);
        HttpSession session = request.getSession();
        Object o = session.getAttribute(token);
        assert o instanceof User;
        User curUser = (User) o;
        return new RequestReturnObject(ExceptionCode.OK, curUser);
    }

    /**
     * 收藏节目
     */
    @PostMapping("/star")
    public RequestReturnObject star(@RequestParam("program_id") String programIDString, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);

        String[] parts = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        int now_num = userService.star(programID, user.getEmail());
        return new RequestReturnObject(ExceptionCode.OK, now_num);
    }

    /**
     * 取消收藏节目
     */
    @PostMapping("/cancelStar")
    public RequestReturnObject cancelStar(@RequestParam("program_id") String programIDString, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);

        String[] parts = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        int now_num = userService.cancelStar(programID, user.getEmail());
        return new RequestReturnObject(ExceptionCode.OK, now_num);
    }

    /**
     * 获取收藏节目
     */
    @PostMapping("/getStarPrograms")
    public RequestReturnObject getStarPrograms(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        List<ProgramBrief> result = userService.getUserStarPrograms(user.getEmail());
        return new RequestReturnObject(ExceptionCode.OK, result);
    }

    /**
     * 修改头像
     */
    @PostMapping("/modifyPortrait")
    public RequestReturnObject modifyPortrait(@RequestParam("portrait") String newPortrait, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserPortrait(user.getEmail(), newPortrait);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
        return new RequestReturnObject(ExceptionCode.OK);
    }

    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    public RequestReturnObject modifyPassword(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword,
                                              @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            userService.modifyUserPassword(user.getEmail(), oldPassword, newPassword);
            return new RequestReturnObject(ExceptionCode.OK);
        } catch (WrongPwdException e) {
            e.printStackTrace();
            return new RequestReturnObject(ExceptionCode.USER_PWD_WRONG);
        }
    }

    /**
     * 修改用户名
     */
    @PostMapping("/modifyName")
    public RequestReturnObject modifyName(@RequestParam("name") String name, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /member: " + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserName(user.getEmail(), name);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
        return new RequestReturnObject(ExceptionCode.OK);
    }
}
