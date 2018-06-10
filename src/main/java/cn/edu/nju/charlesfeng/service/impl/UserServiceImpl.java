package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.model.User;
import cn.edu.nju.charlesfeng.repository.UserRepository;
import cn.edu.nju.charlesfeng.service.UserService;
import cn.edu.nju.charlesfeng.task.MD5Task;
import cn.edu.nju.charlesfeng.task.MailTask;
import cn.edu.nju.charlesfeng.util.exceptions.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param user 欲注册用户实体
     * @return 是否成功注册，成功则返回此会员实体
     */
    @Override
    //@Transactional
    public boolean register(User user) throws UserHasBeenSignUpException, InteriorWrongException {
        User verifyUser = userRepository.findByEmail(user.getEmail());
        if (verifyUser != null) {
            throw new UserHasBeenSignUpException();
        }
        userRepository.save(user);

        // 注册成功后发送邮箱链接
        MailTask mailTask = new MailTask();
        try {
            mailTask.sendMail(user);
        } catch (IOException | MessagingException e1) {
            e1.printStackTrace();
            throw new InteriorWrongException();
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
            throw new UserNotExistException();
        }

        if (!user.getPassword().equals(MD5Task.encodeMD5(pwd))) {
            throw new WrongPwdException();
        }

        if (!user.isActivated()) {
            throw new UserNotActivatedException();
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
            throw new UserNotExistException();
        }

        if (toActivate.isActivated()) {
            throw new UserActiveUrlExpiredException();
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
            throw new UserNotExistException();
        }
        userRepository.save(user);
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
}
