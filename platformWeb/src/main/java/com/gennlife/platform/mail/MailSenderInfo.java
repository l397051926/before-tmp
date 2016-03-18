package com.gennlife.platform.mail;

import java.util.Properties;

/**
 * Created by chensong on 2015/12/7.
 */
public class MailSenderInfo {
    // 发送邮件的服务器的IP(或主机地址)
    private String mailServerHost;
    // 发送邮件的服务器的端口
    private String mailServerPort = "25";
    // 发件人邮箱地址
    private String fromAddress = "changepwd@gennlife.com";
    // 收件人邮箱地址
    private String toAddress;
    // 登陆邮件发送服务器的用户名
    private String userName = "changepwd@gennlife.com";
    // 登陆邮件发送服务器的密码
    private String password = "Genn@2015";
    // 是否需要身份验证
    private boolean validate = true;
    // 邮件主题
    private String subject = "方舟计划信息平台密码修改验证";
    // 邮件的文本内容
    private String content;
    // 邮件附件的文件名
    private String[] attachFileNames;
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.mxhichina.com");
        p.put("mail.smtp.port", "25");
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }
    public String getMailServerHost() {
        return mailServerHost;
    }
    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }
    public String getMailServerPort() {
        return mailServerPort;
    }
    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }
    public boolean isValidate() {
        return validate;
    }
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public String[] getAttachFileNames() {
        return attachFileNames;
    }
    public void setAttachFileNames(String[] fileNames) {
        this.attachFileNames = fileNames;
    }
    public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getToAddress() {
        return toAddress;
    }
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String textContent) {
        this.content = textContent;
    }
}
