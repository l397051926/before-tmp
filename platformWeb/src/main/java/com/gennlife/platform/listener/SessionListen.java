package com.gennlife.platform.listener;

import com.gennlife.platform.util.LogUtils;
import com.gennlife.platform.util.RedisUtil;
import com.gennlife.platform.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Chenjinfeng on 2017/2/23.
 */
@WebListener
public class SessionListen implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionListen.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionid = se.getSession().getId();
        LogUtils.BussnissLog("sessionid " + sessionid + " create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionid = se.getSession().getId();
        LogUtils.BussnissLog("sessionid " + sessionid + " time out");
        RedisUtil.userLogout(sessionid);
        // RedisUtil.delImageIdFromFs(sessionid);
    }
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        new SpringContextUtil().setApplicationContext(applicationContext);
    }
}
