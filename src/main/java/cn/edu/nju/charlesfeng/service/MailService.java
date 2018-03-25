package cn.edu.nju.charlesfeng.service;

import cn.edu.nju.charlesfeng.entity.Member;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * 邮件服务
 */
public interface MailService {

    /**
     * @param toSend 发送邮件的用户
     */
    void sendMail(Member toSend) throws IOException, MessagingException;
}
