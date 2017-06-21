package org.ramer.diary.email;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by 林漠 on 2017/6/21.
 */
public class JavaMailSender {

    //邮件发送类型
    public enum MailType{
        text,
        html
    }

    public enum Character{
        gb2312,
        GBK,
        utf8
    }

    //发送邮件
    public boolean send(SendMail sendMail,MailType mailType,Character character)
    {
        //创建邮件Session所需的Properties对象
        Properties props = new Properties();
        props.put("mail.smtp.host" , sendMail.getSmtpServer());
        props.put("mail.smtp.auth" , "true");

        final String userName = sendMail.getUsername();
        final String passWord = sendMail.getPassword();
        //创建Session对象
        Session session = Session.getDefaultInstance(props
                //以匿名内部类的形式创建登录服务器的认证对象
                , new Authenticator()
                {
                    public PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(userName,passWord);
                    }
                });
        try
        {
            //构造MimeMessage并设置相关属性值
            MimeMessage msg = new MimeMessage(session);
            //设置发件人
            msg.setFrom(new InternetAddress(sendMail.getFrom()));
            //设置收件人
            InternetAddress[] addresses = {new InternetAddress(sendMail.getTo())};
            msg.setRecipients(Message.RecipientType.TO , addresses);
            //设置邮件主题
            String subject = transferChinese(sendMail.getSubject(),character);
            msg.setSubject(subject);
            //构造Multipart
            Multipart mp = new MimeMultipart();
            //向Multipart添加正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            if("text".equals(mailType.name())){
                mbpContent.setText(sendMail.getContent());
            }
            if("html".equals(mailType.name())){
                mbpContent.setContent(sendMail.getContent(),"text/html; charset=utf-8");
            }
            //将BodyPart添加到MultiPart中
            mp.addBodyPart(mbpContent);
            //向Multipart添加附件
            //遍历附件列表，将所有文件添加到邮件消息里
            for(String efile : sendMail.getAttachments())
            {
                MimeBodyPart mbpFile = new MimeBodyPart();
                //以文件名创建FileDataSource对象
                FileDataSource fds = new FileDataSource(efile);
                //处理附件
                mbpFile.setDataHandler(new DataHandler(fds));
                mbpFile.setFileName(transferChinese(fds.getName(),character));
                //将BodyPart添加到MultiPart中
                mp.addBodyPart(mbpFile);
            }
            //清空附件列表
            sendMail.getAttachments().clear();
            //向Multipart添加MimeMessage
            msg.setContent(mp);
            //设置发送日期
            msg.setSentDate(new Date());
            //发送邮件
            Transport.send(msg);
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    //把邮件主题转换为中文
    public String transferChinese(String strText,Character character)
    {
        String characterSet = character.name();
        if("utf8".equals(characterSet)){
            characterSet="utf-8";
        }
        try
        {
            strText = MimeUtility.encodeText(
                    new String(strText.getBytes()
                            , characterSet), characterSet, "B");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return strText;
    }
}
