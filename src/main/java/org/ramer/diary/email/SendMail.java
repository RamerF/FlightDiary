package org.ramer.diary.email;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林漠 on 2017/6/21.
 */
@Data
public class SendMail {

    //收件人邮箱地址
    private String to;
    //发件人邮箱地址
    private String from;
    //SMTP服务器地址
    private String smtpServer;
    //登录SMTP服务器的用户名
    private String username ;
    //登录SMTP服务器的密码
    private String password ;
    //邮件主题
    private String subject;
    //邮件正文
    private String content;
    //记录所有附件文件的集合
    List<String> attachments = new ArrayList<String>();
    //无参数的构造器
    public SendMail()
    {
    }

    public SendMail(String to , String from , String smtpServer
            , String username , String password
            , String subject , String content)
    {
        this.to = to;
        this.from = from;
        this.smtpServer = smtpServer;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.content = content;
    }

    //增加附件，将附件文件名添加的List集合中
    public void attachfile(String fname) {
        attachments.add(fname);
    }
}

