package com.gennlife.platform.mail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by chensong on 2015/12/7.
 */
public class GennAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public GennAuthenticator() {
    }

    public GennAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
