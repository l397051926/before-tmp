package com.gennlife.platform.listener;

import com.gennlife.platform.server.ArkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by chensong on 2015/12/10.
 */
public class ArkListener implements ServletContextListener {
    Logger logger = LoggerFactory.getLogger(ArkListener.class);
    public void contextInitialized(ServletContextEvent sce) {
        ArkServer.SERVER_START();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        ArkServer.SERVER_STOP();
    }
}
