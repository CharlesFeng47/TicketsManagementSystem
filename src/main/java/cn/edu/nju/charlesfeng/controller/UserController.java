package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.dto.program.ProgramBriefDTO;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.member.*;
import cn.edu.nju.charlesfeng.util.exceptions.unknown.InteriorWrongException;
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
 *
 * @author Dong
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
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletRequest request) throws UserNotExistException, WrongPwdException, UserNotActivatedException {
        logger.debug("INTO /user/login");
        User user = userService.logIn(email, password);
        String token = "USER:" + email;
        HttpSession session = request.getSession();
        session.setAttribute(token, new User(user));
        return token;
    }

    /**
     * 用户注册
     *
     * @return 该用户对应的token
     */
    @PostMapping("/signUp")
    public String memberSignUp(@RequestParam("username") String username, @RequestParam("password") String password,
                               @RequestParam("email") String email, HttpServletRequest request) throws UserHasBeenSignUpException, InteriorWrongException {

        logger.debug("INTO /user/user_sign_up");
        User user = new User(username, password, email);
        userService.register(user);
        //TODO 注册后邮箱尚未验证，应该不需要把用户的实体置于session中吧
        String token = "USER: " + user.getEmail();
        HttpSession session = request.getSession();
        session.setAttribute(token, new User(user));
        return token;
    }

    /**
     * @return 邮箱链接验证
     */
    @PostMapping("/userActive")
    public void verifyUserEmail(@RequestParam("active_url") String activeUrl) throws UnsupportedEncodingException, UserActivateUrlExpiredException, UserActivateUrlWrongException {
        logger.debug("INTO /user/userActive");
        System.out.println(activeUrl);
        userService.activateByMail(activeUrl);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public void logout(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/logout");
        HttpSession session = request.getSession();
        session.setAttribute(token, null);
    }

    /**
     * 根据Token获取当前用户
     */
    @PostMapping("/token")
    public User getToken(@RequestParam("token") String token, HttpServletRequest request) throws UserTokenExpiredException {
        logger.debug("INTO /user/token, token: " + token);
        HttpSession session = request.getSession();
        Object o = session.getAttribute(token);

        if (o == null) throw new UserTokenExpiredException(ExceptionCode.USER_TOKEN_EXPIRED);

        assert o instanceof User;
        return (User) o;
    }

    /**
     * 收藏节目
     */
    @PostMapping("/star")
    public Integer star(@RequestParam("program_id") String programIDStr, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/star: " + token);

        String[] parts = programIDStr.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        return userService.star(programID, user.getEmail());
    }

    /**
     * 取消收藏节目
     */
    @PostMapping("/cancelStar")
    public Integer cancelStar(@RequestParam("program_id") String programIDStr, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/cancelStar: " + token);

        String[] parts = programIDStr.split("-");
        ProgramID programID = new ProgramID();
        programID.setVenueID(Integer.parseInt(parts[0]));
        programID.setStartTime(TimeHelper.getLocalDateTime(Long.parseLong(parts[1])));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        return userService.cancelStar(programID, user.getEmail());
    }

    /**
     * 获取收藏节目
     */
    @PostMapping("/getStarPrograms")
    public List<ProgramBriefDTO> getStarPrograms(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/getStarPrograms: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        return userService.getUserStarPrograms(user.getEmail());
    }

    /**
     * 修改头像
     */
    @PostMapping("/modifyPortrait")
    public void modifyPortrait(@RequestParam("portrait") String newPortrait, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/modifyPortrait: " + token);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserPortrait(user.getEmail(), newPortrait);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
    }

    /**
     * 修改密码
     */
    @PostMapping("/modifyPassword")
    public void modifyPassword(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword,
                               @RequestParam("token") String token, HttpServletRequest request) throws WrongPwdException {
        logger.debug("INTO /user/modifyPassword: " + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserPassword(user.getEmail(), oldPassword, newPassword);
    }

    /**
     * 修改用户名
     */
    @PostMapping("/modifyName")
    public void modifyName(@RequestParam("name") String name, @RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user/modifyName: " + token);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(token);
        userService.modifyUserName(user.getEmail(), name);
        session.setAttribute(token, new User(userService.getUser(user.getEmail())));
    }
}
