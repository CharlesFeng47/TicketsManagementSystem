package cn.edu.nju.charlesfeng.util.helper;

import cn.edu.nju.charlesfeng.model.AlipayAccount;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author Dong
 */
public class SystemHelper {


    private SystemHelper() {
    }

    /**
     * 获取系统的支付账户
     *
     * @return 账户
     */
    public static AlipayAccount getSystemAccount() {
        try {
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader
                    (Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties"))));
            AlipayAccount alipayAccount = new AlipayAccount();
            alipayAccount.setId(String.valueOf(properties.get("account")));
            alipayAccount.setPwd(String.valueOf(properties.get("password")));
            return alipayAccount;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 得到图片的服务器
     *
     * @return
     */
    public static String getDomainName() {
        try {
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader
                    (Thread.currentThread().getContextClassLoader().getResourceAsStream("system.properties"))));
            return String.valueOf(properties.get("imagesDomainName"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
