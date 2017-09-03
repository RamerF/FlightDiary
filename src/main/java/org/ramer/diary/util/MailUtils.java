/*
 * 
 */
package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱工具类:
 *  功能:
 *    1.发送邮件
 *    2.验证一个字符串是否为邮箱格式
 * @author ramer
 *
 */
@Slf4j
public class MailUtils{

    /**
     * 发送一封邮件.
     *
     * @param mailTo 接收地址
     * @param top 邮件主题
     * @param content 邮件内容
     */
    public static void sendMail(String mailTo, String top, String content) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(props);
        //    Session session = Session.getInstance(props, new Authenticator() {
        //      @Override
        //      protected PasswordAuthentication getPasswordAuthentication() {
        //        return new PasswordAuthentication("1390635973@qq.com", "wafpvdagpaquibfa");
        //      }
        //    });
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("1390635973@qq.com"));
            InternetAddress to[] = new InternetAddress[1];
            to[0] = new InternetAddress(mailTo);
            message.setRecipients(Message.RecipientType.TO, to);
            message.setSubject(top);
            message.setContent(content, "text/html;charset=UTF-8");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.qq.com", "1390635973@qq.com", "wafpvdagpaquibfa");
            transport.sendMessage(message, to);
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检测是否给定的字符串是否为邮箱.
     *
     * @param email 字符串
     * @return true, 如果是邮箱
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            log.debug("是邮箱");
            return true;
        }
        log.debug("不是邮箱");
        return false;
    }

    /**
     * 数据库是否存在.
     *
     * @param email 未加密邮箱
     * @param userService the UserService
     * @return 存在, 返回true
     */
    public static boolean exist(String email, UserService userService) {
        User user = userService.getByEmail(EncryptUtil.execEncrypt(email));
        return user == null ? false : true;

    }
}
