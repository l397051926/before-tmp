package com.gennlife.platform.util;

import com.gennlife.platform.mail.MailSenderInfo;
import com.gennlife.platform.mail.SimpleMailSender;
import com.gennlife.platform.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chensong on 2015/12/7.
 */
public class Mailer {
    private static Logger logger = LoggerFactory.getLogger(Mailer.class);

    public static void sendHTMLMail(String email, String url, User user) {
        MailSenderInfo mailInfo = MailSenderInfo.buildMailSenderInfo();
        StringBuffer demo = new StringBuffer();
        String aurl = "<a href=\"" + url + "\">" + url + "</a>";
        demo.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
                .append("<html>")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("<title>更新密码确认邮件邮件</title>")
                .append("<style type=\"text/css\">")
                .append(".test{font-family:\"Microsoft Yahei\";font-size: 18px;color: black;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<span class=\"test\">  " + email + ",您好！ <br></span>")
                .append("<span class=\"test\"> &nbsp;&nbsp;&nbsp;&nbsp;您正在生命奇点进行密码找回，点击以下链接完成验证，重新设置密码。 <br>" + aurl + "<br></span>")
                .append("<span class=\"test\"> &nbsp;&nbsp;&nbsp;&nbsp;（该链接在24小时内有效，24小时后需要重新获取验证邮件）" +
                        "如果该链接无法点击，请将其复制粘贴到你的浏览器地址栏中访问。" +
                        "如果这不是您的邮件，请忽略此邮件。" +
                        "这是生命奇点系统邮件，请勿回复。<br></span>")
                .append("</body>")
                .append("</html>");
        mailInfo.setToAddress(email);
        mailInfo.setContent(demo.toString());
        SimpleMailSender sms = new SimpleMailSender();
        // 发送文体格式
        sms.sendTextMail(mailInfo);
    }
}
