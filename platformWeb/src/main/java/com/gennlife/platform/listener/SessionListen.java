package com.gennlife.platform.listener;

import com.gennlife.platform.util.RedisUtil;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Chenjinfeng on 2017/2/23.
 */
public class SessionListen implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionid=se.getSession().getId();
        RedisUtil.userLogout(sessionid);

    }
}
