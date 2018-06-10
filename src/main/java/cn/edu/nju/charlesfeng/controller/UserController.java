package cn.edu.nju.charlesfeng.controller;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.task.MD5Task;
import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import cn.edu.nju.charlesfeng.util.helper.ImgHelper;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
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
    @PostMapping("/user_login")
    public RequestReturnObject login(@RequestParam("email") String email, @RequestParam("password") String pwd, HttpServletRequest request) {
        logger.debug("INTO /user/login");
        //TODO 需要考虑用户的重复登录问题
        try {
            User user = userService.logIn(email, pwd);
            String token = "USER:" + ": " + email;
            HttpSession session = request.getSession();
            session.setAttribute(token, user);
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
    @PostMapping("/user_sign_up")
    public RequestReturnObject memberSignUp(@RequestParam("username") String username, @RequestParam("password") String pwd,
                                            @RequestParam("email") String email, HttpServletRequest request) {

        logger.debug("INTO /user/user_sign_up");
        try {
            User user = new User();
            user.setEmail(email);
            user.setActivated(false);
            user.setPassword(MD5Task.encodeMD5(pwd));
            user.setName(username);
            user.setPortrait(ImgHelper.getBaseImg(Objects.requireNonNull(this.getClass().getClassLoader().getResource("default.png")).getPath()));
            userService.register(user);
            //TODO 注册后邮箱尚未验证，应该不需要把用户的实体置于session中吧
            String token = "USER: " + user.getEmail();
            HttpSession session = request.getSession();
            session.setAttribute(token, user);
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
    @PostMapping("/user_active")
    public RequestReturnObject verifyUserEmail(@RequestParam("activeUrl") String activeUrl, HttpServletRequest request) {
        logger.debug("INTO /user/user_active");
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
    @PostMapping
    public RequestReturnObject getToken(@RequestParam("token") String token, HttpServletRequest request) {
        logger.debug("INTO /user: " + token);
        HttpSession session = request.getSession();
//        User curUser = (User) session.getAttribute(token);
        Object o = session.getAttribute(token);
        assert o instanceof User;
        User curUser = (User) o;
        return new RequestReturnObject(RequestReturnObjectState.OK, curUser);
    }

//    /**
//     * 会员修改
//     *
//     * @return 该场馆对应的token
//     */
//    @PostMapping("member_modify")
//    public RequestReturnObject memberModify(@RequestParam("token") String token, @RequestParam("pwd") String pwd,
//                                            HttpServletRequest request) {
//        logger.debug("INTO /user/member_modify");
//
//        HttpSession session = request.getSession();
////        Member curMember = (Member) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Member;
//        Member curMember = (Member) o;
//
//        try {
//            boolean result = userService.modifyMember(curMember, pwd);
//            if (result) {
//                // 重新设置session attribute
//                User nowMember = userService.getUser(curMember.getId(), UserType.MEMBER);
//                session.setAttribute(token, nowMember);
//                return new RequestReturnObject(RequestReturnObjectState.OK);
//            } else return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }

//    /**
//     * 场馆修改
//     *
//     * @return 该场馆对应的token
//     */
//    @PostMapping("spot_modify")
//    public RequestReturnObject spotModify(@RequestParam("token") String token, @RequestParam("password") String pwd,
//                                          @RequestParam("name") String spotName, @RequestParam("site") String site,
//                                          @RequestParam("alipayId") String alipayId, @RequestParam("seatInfos") String seatInfosJson,
//                                          @RequestParam("seatsMap") String seatsMapJson, @RequestParam("curSeatTypeCount") int curSeatTypeCount,
//                                          HttpServletRequest request) {
//        logger.debug("INTO /user/spot_modify");
//        HttpSession session = request.getSession();
////        Spot curSpot = (Spot) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Spot;
//        Spot curSpot = (Spot) o;
//
//
//        List<SeatInfo> seatInfos = JSON.parseArray(seatInfosJson, SeatInfo.class);
//        try {
//            boolean result = userService.modifySpot(curSpot, pwd, spotName, site, alipayId, seatInfos, seatsMapJson, curSeatTypeCount);
//            if (result) {
//                // 重新设置session attribute
//                User nowSpot = userService.getUser(curSpot.getId(), UserType.SPOT);
//                session.setAttribute(token, nowSpot);
//                return new RequestReturnObject(RequestReturnObjectState.OK, token);
//            } else return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        } catch (AlipayEntityNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.ALIPAY_ENTITY_NOT_EXIST);
//        }
//    }

//    /**
//     * 兑换优惠券
//     *
//     * @return 是否兑换成功，失败则提示原因
//     */
//    @PostMapping("coupon_convert")
//    public RequestReturnObject couponConvert(@RequestParam("token") String token, @RequestParam("description") String description,
//                                             @RequestParam("offPrice") double offPrice, @RequestParam("neededCredit") double neededCredit,
//                                             HttpServletRequest request) {
//        logger.debug("INTO /user/coupon_convert");
//
//        HttpSession session = request.getSession();
////        Member curMember = (Member) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Member;
//        Member curMember = (Member) o;
//
//        try {
//            Member convertedMember = userService.memberConvertCoupon(curMember, new Coupon(offPrice, neededCredit, description));
//            // 更新session中的会员实体
//            session.setAttribute(token, convertedMember);
//            return new RequestReturnObject(RequestReturnObjectState.OK);
//        } catch (MemberConvertCouponCreditNotEnoughException e) {
//            return new RequestReturnObject(RequestReturnObjectState.COUPON_CONVERT_CREDIT_NOT_ENOUGH);
//        }
//    }

//    /**
//     * @return 场馆获取到的会员信息
//     */
//    @PostMapping("spot_get_member_info")
//    public RequestReturnObject spotGetMemberInfo(@RequestParam("mid") String mid, HttpServletRequest request) {
//        logger.debug("INTO /user/spot_get_member_info");
//
//        try {
//            ContentMemberOfSpot result = userService.getMemberOfSpot(mid);
//            return new RequestReturnObject(RequestReturnObjectState.OK, result);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.USER_NOT_EXIST);
//        }
//    }

//    /**
//     * @return 经理获取未审批的用户
//     */
//    @PostMapping("unexamined_spots")
//    public RequestReturnObject getAllUnexaminedSpots(@RequestParam("token") String token, HttpServletRequest request) {
//        logger.debug("INTO /user/unexamined_spots");
//
//        HttpSession session = request.getSession();
////        Manager curManager = (Manager) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Manager;
//
//        try {
//            List<UnexaminedSpot> result = userService.getAllUnexaminedSpots();
//            return new RequestReturnObject(RequestReturnObjectState.OK, result);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }

//    /**
//     * @return 经理获取单个场馆的信息
//     */
//    @PostMapping("/spot")
//    public RequestReturnObject getOneSpot(@RequestParam("token") String token, @RequestParam("spotId") String spotId,
//                                          HttpServletRequest request) {
//        logger.debug("INTO /user/spot");
//
//        HttpSession session = request.getSession();
////        Manager curManager = (Manager) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Manager;
//
//        try {
//            Spot curSpot = (Spot) userService.getUser(spotId, UserType.SPOT);
//            return new RequestReturnObject(RequestReturnObjectState.OK, curSpot);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }

//    /**
//     * @return 经理获取未审批的用户
//     */
//    @PostMapping("examine")
//    public RequestReturnObject examineSpot(@RequestParam("token") String token, @RequestParam("spotId") String spotId,
//                                           HttpServletRequest request) {
//        logger.debug("INTO /user/examine");
//
//        HttpSession session = request.getSession();
////        Manager curManager = (Manager) session.getAttribute(token);
//        Object o = session.getAttribute(token);
//        assert o != null && o instanceof Manager;
//
//        try {
//            userService.examineSpot(spotId);
//            return new RequestReturnObject(RequestReturnObjectState.OK);
//        } catch (UserNotExistException e) {
//            return new RequestReturnObject(RequestReturnObjectState.INTERIOR_WRONG);
//        }
//    }
}
