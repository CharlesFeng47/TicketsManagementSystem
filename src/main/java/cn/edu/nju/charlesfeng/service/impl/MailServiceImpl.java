package cn.edu.nju.charlesfeng.service.impl;

import cn.edu.nju.charlesfeng.entity.Member;
import cn.edu.nju.charlesfeng.service.MailService;
import io.github.biezhi.ome.OhMyEmail;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendMail(Member toSend) throws IOException, MessagingException {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new InputStreamReader
                (Thread.currentThread().getContextClassLoader().getResourceAsStream("my_mail_config.properties"))));

        final String mailContent = getMailContent(toSend);
        OhMyEmail.config(OhMyEmail.SMTP_ENT_QQ(true), (String) properties.get("id"), (String) properties.get("pwd"));
        OhMyEmail.subject("Marvel Ticket 会员验证")
                .from("Marvel Ticket 官方")
                .to(toSend.getEmail())
                .html(mailContent)
                .send();
    }

    /**
     * 获得邮件内容
     */
    private String getMailContent(Member tosend) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>尊敬的 Marvel Ticket 会员，您好！</div><br>")
                .append("<div>欢迎注册使用 Marvel Ticket，请点击此链接激活您的账户：")
                .append("<a href=\"http://localhost:9528/member_active/")
                .append(getActiveUrl(tosend))
                .append("\">您的专属链接</a>")
                .append("</div><br>")
                .append("<div>感谢您的使用！谢谢！</div>")
                .append("<br>------------------</div>")
                .append("<div>Marvel Ticket 官方</div>");
        return sb.toString();
    }

    /**
     * 获得会员激活的链接
     */
    private String getActiveUrl(Member toSend) throws UnsupportedEncodingException {
        return Base64.getUrlEncoder().encodeToString(toSend.getId().getBytes("utf-8"));
    }
}
