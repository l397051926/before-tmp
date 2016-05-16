package com.gennlife.platform.util;

import com.gennlife.platform.mail.MailSenderInfo;
import com.gennlife.platform.mail.SimpleMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chensong on 2015/12/7.
 */
public class Mailer {
    private static Logger logger = LoggerFactory.getLogger(Mailer.class);
    private static MailSenderInfo mailInfo = new MailSenderInfo();
    public static void sendMail(String uname,String email,String url){
        mailInfo.setToAddress(email);
        logger.info("给"+uname +"的邮箱"+email+"发送邮件");
        StringBuffer buffer = new StringBuffer();
        buffer.append("用户 "+uname+":\n");
        buffer.append("     修改密码连接:"+url + " \n");
        buffer.append("     点击连接会跳转到修改密码页面进行密码修改");
        mailInfo.setContent(buffer.toString());
        // 发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        // 发送文体格式
        sms.sendTextMail(mailInfo);
    }

    public static void main(String[] args){
        String uname = "陈松";
        String email = "duanjinhui@gennlife.com";
        String url = "http://192.168.1.127:63342/uranus/ubasicinfo/update_password.html";
        sendHTMLMail(uname,email,url);
    }

    public static void sendHTMLMail(String uname,String email,String url){
        StringBuffer demo = new StringBuffer();
        demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
                .append("<html>")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("<title>测试邮件</title>")
                .append("<style type=\"text/css\">")
                .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: black;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<span class=\"test\"> 你好,"+uname+" <br></span>")
                .append("<span class=\"test\"> &nbsp; &nbsp;&nbsp;&nbsp;    修改密码连接: "+url+"<br></span>")
                .append("<span class=\"test\"> &nbsp;&nbsp;&nbsp;&nbsp;      点击连接会跳转到修改密码页面进行密码修改<br></span>")
                .append("</body>")
                .append("</html>");
        mailInfo.setToAddress(email);
        mailInfo.setContent(demo.toString());
        SimpleMailSender sms = new SimpleMailSender();
        // 发送文体格式
        sms.sendTextMail(mailInfo);
    }
}
