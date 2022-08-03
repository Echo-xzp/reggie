package com.echoes.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @Author : Xiaozp
 * @ClassName : Email
 * @Description : TODO
 * @create : 2022/7/29 16:03
 * @Version : v1.0
 * @Powered By Corner Lab
 */
@Component
public class Email {

    static private String emailAddress;
    static private String password;

    @Value("${reggie.email}")
    public void setEmailAddress(String email){
        emailAddress = email;
    }
    @Value("${reggie.emailPassword}")
    public void setPassword(String emailPassword){
        password = emailPassword;
    }

    /**
     * @Name : sendEmail
     * @description : 利用QQ邮箱发生登录验证码
     * @createTime : 2022/7/29 16:24
     * @param : email
     * @param : code
     * @return : void
     */
    static public  void sendEmail(String email,String code) throws javax.mail.MessagingException {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        session.setDebug(true);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress(emailAddress));
        // 设置收件人邮箱地址
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人

        // 设置邮件标题
        message.setSubject("睿纪外卖登录提醒:");
        // 设置邮件内容
        message.setText("尊敬的用户:\n  您好,您正在尝试登录瑞吉外卖系统,您的登录验证码为: "+code+"\n请勿泄露给他人！");
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect(emailAddress, password);// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码,输入自己的即可
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }



}