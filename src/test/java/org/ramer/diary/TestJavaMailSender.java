package org.ramer.diary;

import org.ramer.diary.email.JavaMailSender;
import org.ramer.diary.email.SendMail;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * Created by 林漠 on 2017/6/21.
 */
public class TestJavaMailSender {

    public static void main(String[] args)
    {
        JavaMailSender javaMailSend = new JavaMailSender() {

        };
        SendMail sendMail = new SendMail();
        sendMail.setSmtpServer("smtp.qq.net");
        //此处设置登录的用户名
        sendMail.setUsername("cdcdc@qq.com");
        //此处设置登录的密码
        sendMail.setPassword("*****");
        //设置收件人的地址
        sendMail.setTo("12222555n@163.com");
        //设置发送人地址
        sendMail.setFrom("yyhmmwan@yeah.net");
        //设置标题
        sendMail.setSubject(
                "测试邮件");
        StringBuffer demo = new StringBuffer();
        demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
                .append("<html>")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("<title>测试邮件</title>")
                .append("<style type=\"text/css\">")
                .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<span class=\"test\"><a href='http://www.baidu.com'>大家好，这里是测试Demo</a></span>")
                .append("</body>")
                .append("</html>");
        //设置内容
        sendMail.setContent(demo.toString());
        //粘贴附件
//        sendMail.attachfile("E:python//a.txt");
        if (javaMailSend.send(sendMail,JavaMailSender.MailType.text,JavaMailSender.Character.utf8))
        {
            System.out.println("---邮件发送成功---");
        }
    }
}
