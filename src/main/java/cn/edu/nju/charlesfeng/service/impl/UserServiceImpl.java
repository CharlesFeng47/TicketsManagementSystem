package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.Program;
import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.model.id.ProgramID;
import cn.edu.nju.charlesfeng.repository.UserRepository;
import cn.edu.nju.charlesfeng.service.ProgramService;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.task.MD5Task;
import cn.edu.nju.charlesfeng.task.MailTask;
import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.member.*;
import cn.edu.nju.charlesfeng.util.exceptions.unknown.InteriorWrongException;
import cn.edu.nju.charlesfeng.dto.program.ProgramBriefDTO;
import cn.edu.nju.charlesfeng.util.helper.ImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ProgramService programService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProgramService programService) {
        this.userRepository = userRepository;
        this.programService = programService;
    }

    /**
     * @param user 欲注册用户实体
     * @return 是否成功注册，成功则返回此会员实体
     */
    @Override
    //@Transactional
    public boolean register(User user) throws UserHasBeenSignUpException, InteriorWrongException {
        user.setPassword(MD5Task.encodeMD5(user.getPassword()));
        user.setPortrait(ImageHelper.getBaseImg(Objects.requireNonNull(this.getClass().getClassLoader().getResource("default.png")).getPath()));

        User verifyUser = userRepository.findByEmail(user.getEmail());
        if (verifyUser != null) {
            throw new UserHasBeenSignUpException(ExceptionCode.USER_HAS_BEEN_SIGN_UP);
        }
        userRepository.save(user);

        // 注册成功后发送邮箱链接
        MailTask mailTask = new MailTask();
        try {
            mailTask.sendMail(user);
        } catch (IOException | MessagingException e1) {
            e1.printStackTrace();
            throw new InteriorWrongException(ExceptionCode.INTERIOR_WRONG);
        }
        return true;
    }

    /**
     * 【所有用户】的登录
     *
     * @param id  欲登录的用户ID
     * @param pwd 欲登录的用户密码
     * @return 登录结果，成功则返回此用户实体
     */
    @Override
    public User logIn(String id, String pwd) throws UserNotExistException, WrongPwdException, UserNotActivatedException {
        User user = userRepository.findByEmail(id);
        if (user == null) {
            throw new UserNotExistException(ExceptionCode.USER_NOT_EXIST);
        }

        if (!user.getPassword().equals(MD5Task.encodeMD5(pwd))) {
            throw new WrongPwdException(ExceptionCode.USER_PWD_WRONG);
        }

        if (!user.isActivated()) {
            throw new UserNotActivatedException(ExceptionCode.USER_INACTIVE);
        }
        return user;
    }

    /**
     * 【用户】通过邮箱验证用户，验证后才可登录
     *
     * @param activeUrl 验证的连接参数
     * @return 邮箱验证结果
     */
    @Override
    //@Transactional
    public boolean activateByMail(String activeUrl) throws UnsupportedEncodingException, UserNotExistException, UserActiveUrlExpiredException {
        byte[] base64decodedBytes = Base64.getUrlDecoder().decode(activeUrl);
        String toActivateUserId = new String(base64decodedBytes, "utf-8");
        User toActivate = userRepository.findByEmail(toActivateUserId);
        if (toActivate == null) {
            throw new UserNotExistException(ExceptionCode.USER_NOT_EXIST);
        }

        if (toActivate.isActivated()) {
            throw new UserActiveUrlExpiredException(ExceptionCode.MEMBER_ACTIVATE_URL_EXPIRE);
        }

        toActivate.setActivated(true);
        userRepository.save(toActivate);
        return true;
    }

    /**
     * @param user 欲修改用户的实体
     * @return 修改结果，成果则true
     */
    @Override
    public boolean modifyUser(User user) throws UserNotExistException {
        User verifyUser = userRepository.findByEmail(user.getEmail());
        if (verifyUser == null) {
            throw new UserNotExistException(ExceptionCode.USER_NOT_EXIST);
        }
        userRepository.save(user);
        return true;
    }

    /**
     * @param userID   欲修改用户ID
     * @param portrait 头像
     * @return 修改结果，成果则true
     */
    @Override
    public boolean modifyUserPortrait(String userID, String portrait) {
        userRepository.modifyUserPortrait(userID, portrait);
        return true;
    }

    /**
     * @param userID       欲修改用户ID
     * @param old_password 过去的密码
     * @param new_password 新密码
     * @return 修改结果，成果则true
     */
    @Override
    public boolean modifyUserPassword(String userID, String old_password, String new_password) throws WrongPwdException {
        User user = userRepository.findByEmail(userID);
        if (!user.getPassword().equals(MD5Task.encodeMD5(old_password))) {
            throw new WrongPwdException(ExceptionCode.USER_PWD_WRONG);
        }
        userRepository.modifyUserPassword(userID, MD5Task.encodeMD5(new_password));
        return true;
    }

    /**
     * @param userID 欲修改用户ID
     * @param name   用户名
     * @return 修改结果，成果则true
     */
    @Override
    public boolean modifyUserName(String userID, String name) {
        userRepository.modifyUserName(userID, name);
        return true;
    }

    /**
     * @param id 要查看的用户ID
     * @return 用户详情
     */
    @Override
    public User getUser(String id) {
        return userRepository.findByEmail(id);
    }

    /**
     * 收藏，设为喜欢
     *
     * @param programID 节目ID
     * @param userID    用户ID
     * @return 收藏后的节目数
     */
    @Override
    public int star(ProgramID programID, String userID) {
        User user = userRepository.findByEmail(userID);
        Program program = programService.getOneProgram(programID);
        user.getPrograms().add(program);
        userRepository.save(user);
        return user.getPrograms().size();
    }

    /**
     * 取消收藏
     *
     * @param programID 节目ID
     * @param userID    用户ID
     * @return 取消收藏后的节目数
     */
    @Override
    public int cancelStar(ProgramID programID, String userID) {
        User user = userRepository.findByEmail(userID);
        user.setPrograms(excludeProgram(user.getPrograms(), programID));
        userRepository.save(user);
        return user.getPrograms().size();
    }

    /**
     * 获取用户star的节目
     *
     * @param userID 用户ID
     * @return 节目列表
     */
    @Override
    public List<ProgramBriefDTO> getUserStarPrograms(String userID) {
        User user = userRepository.findByEmail(userID);
        Set<Program> programs = user.getPrograms();
        List<ProgramBriefDTO> result = new ArrayList<>();
        for (Program program : programs) {
            result.add(new ProgramBriefDTO(program));
        }
        return result;
    }

    /**
     * 判断用户是否喜欢指定的节目
     *
     * @param userID  用户ID
     * @param program 节目
     * @return 是否喜欢
     */
    @Override
    public boolean isLike(String userID, Program program) {
        User user = userRepository.findByEmail(userID);
        Set<Program> programs = user.getPrograms();
        return programs.contains(program);
    }

    /**
     * 除去不需要的节目
     *
     * @param programs  节目列表
     * @param programID 不需要的节目ID
     * @return 新的节目列表
     */
    private Set<Program> excludeProgram(Set<Program> programs, ProgramID programID) {
        Set<Program> result = new HashSet<>();
        for (Program program : programs) {
            if (!program.getProgramID().equals(programID)) {
                result.add(program);
            }
        }
        return result;
    }
}
