package com.gennlife.platform.listener;

import com.gennlife.platform.dao.AllDao;
import com.gennlife.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Chenjinfeng on 2017/2/23.
 */
public class SessionListen implements HttpSessionListener {
    private static final Logger logger= LoggerFactory.getLogger(SessionListen.class);
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionid=se.getSession().getId();
        logger.info("sessionid " +sessionid+" create");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionid=se.getSession().getId();
        logger.info("sessionid " +sessionid+" time out");
        RedisUtil.userLogout(sessionid);
        AllDao.getInstance().getSessionDao().deleteBySessionID(sessionid);
    }
}
