package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.task.MD5Task;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import cn.edu.nju.charlesfeng.util.filter.program.ProgramBrief;
import cn.edu.nju.charlesfeng.util.helper.ImageHelper;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import cn.edu.nju.charlesfeng.util.helper.TimeHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

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
    public RequestReturnObject login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) {
        logger.debug("INTO /user/login");
        try {
            User user = userService.logIn(email, password);
            String token = "USER:" + email;
            HttpSession session = request.getSession();
            session.setAttribute(token, new User(user));
            return new RequestReturnObject(RequestReturnObjectState.OK, token);
        } catch (UserNotExistException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
        } catch (WrongPwdException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_PWD_WRONG);
        } catch (UserNotActivatedException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_INACTIVE);
        }
    }

    /**
     * 用户注册
     *
     * @return 该用户对应的token
     */
    @PostMapping("/signUp")
    public RequestReturnObject memberSignUp(@RequestParam("username") String username, @RequestParam("password") String password,
                                            @RequestParam("email") String email, HttpServletRequest request) {

        logger.debug("INTO /user/user_sign_up");
        try {
            User user = new User();
            user.setEmail(email);
            user.setActivated(false);
            user.setPassword(MD5Task.encodeMD5(password));
            user.setName(username);
            user.setPortrait(ImageHelper.getBaseImg(Objects.requireNonNull(this.getClass().getClassLoader().getResource("default.png")).getPath()));
            userService.register(user);
            //TODO 注册后邮箱尚未验证，应该不需要把用户的实体置于session中吧
            String token = "USER: " + user.getEmail();
            HttpSession session = request.getSession();
            session.setAttribute(token, new User(user));
            return new RequestReturnObject(RequestReturnObjectState.OK, token);
        } catch (UserHasBeenSignUpException e) {
            return new RequestReturnObject(RequestReturnObjectState.USER_HAS_BEEN_SIGN_UP);
        } catch (InteriorWrongException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        }
    }

    /**
     * @return 邮箱链接验证
     */
    @PostMapping("/userActive")
    public RequestReturnObject verifyUserEmail(@RequestParam("active_url") String activeUrl) {
        logger.debug("INTO /user/userActive");
        System.out.println(activeUrl);
        try {
            userService.activateByMail(activeUrl);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } catch (UnsupportedEncodingException e) {
            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
        } catch (UserNotExistException e) {
            return new RequestReturnObject(RequestReturnObjectState.MEMBER_ACTIVATE_URL_WRONG);
        } catch (UserActiveUrlExpiredException e) {
            return new RequestReturnObject(RequestReturnObjectState.MEMBER_ACTIVATE_URL_EXPIRE);
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public RequestReturnObject logout(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/logout");
        HttpSession session = request.getSession();
        session.setAttribute(token, null);
        return new RequestReturnObject(RequestReturnObjectState.OK);
    }

    /**
     * 根据Token获取当前用户
     */
    @PostMapping("/token")
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);
        HttpSession session = request.getSession();
        Object o = session.getAttribute(token);
        assert o instanceof User;
        User curUser = (User) o;
        return new RequestReturnObject(RequestReturnObjectState.OK, curUser);
    }

    /**
     * 收藏节目
     */
    @PostMapping("/star")
    public RequestReturnObject star(@RequestParam("program_id") String programIDString, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);

        String[] parts = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        int now_num = userService.star(programID, user.getEmail());
        return new RequestReturnObject(RequestReturnObjectState.OK, now_num);
    }

    /**
     * 取消收藏节目
     */
    @PostMapping("/cancelStar")
    public RequestReturnObject cancelStar(@RequestParam("program_id") String programIDString, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);

        String[] parts = programIDString.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        int now_num = userService.cancelStar(programID, user.getEmail());
        return new RequestReturnObject(RequestReturnObjectState.OK, now_num);
    }

    /**
     * 获取收藏节目
     */
    @PostMapping("/getStarPrograms")
    public RequestReturnObject getStarPrograms(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        List<ProgramBrief> result = userService.getUserStarPrograms(user.getEmail());
        return new RequestReturnObject(RequestReturnObjectState.OK, result);
    }

    /**
     * 修改头像
     */
    @PostMapping("/modifyPortrait")
    public RequestReturnObject modifyPortrait(@RequestParam("portrait") String newPortrait, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserPortrait(user.getEmail(), newPortrait);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
        return new RequestReturnObject(RequestReturnObjectState.OK);
    }

    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    public RequestReturnObject modifyPassword(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword,
                                              @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute(token);
            userService.modifyUserPassword(user.getEmail(), oldPassword, newPassword);
            return new RequestReturnObject(RequestReturnObjectState.OK);
        } catch (WrongPwdException e) {
            e.printStackTrace();
            return new RequestReturnObject(RequestReturnObjectState.USER_PWD_WRONG);
        }
    }

    /**
     * 修改用户名
     */
    @PostMapping("/modifyName")
    public RequestReturnObject modifyName(@RequestParam("name") String name, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserName(user.getEmail(), name);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
        return new RequestReturnObject(RequestReturnObjectState.OK);
    }
}
