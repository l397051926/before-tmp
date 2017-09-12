package com.gennlife.platform.server;

import com.gennlife.platform.service.ArkService;
import com.gennlife.platform.util.ConfigUtils;
import com.gennlife.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by chensong on 2015/12/10.
 */
@Component
public class ArkServer implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ArkServer.class);
    //未启动
    private static final int SERVER_STATUS_PENDING = 0;
    //已启动
    private static final int SERVER_STATUS_STARTED = 1;
    //在启动
    private static final int SERVER_STATUS_STARTING = 2;
    //在关闭
    private static final int SERVER_STATUS_STOPPING = 3;
    @Autowired
    private ArkService arkService;
    private static volatile int SERVER_STATUS = SERVER_STATUS_PENDING;
    @Autowired
    private ConfigUtils configUtils;

    public synchronized void SERVER_START() {
        if (SERVER_STATUS != SERVER_STATUS_PENDING) {
            return;
        }
        long serverStart = System.currentTimeMillis();
        SERVER_STATUS = SERVER_STATUS_STARTING;
        logger.info("ArkServer 启动中 .......");
        arkService.init(null, null);
        logger.info("ArkServer 启动耗时：" + (System.currentTimeMillis() - serverStart) + "ms");
        SERVER_STATUS = SERVER_STATUS_STARTED;
    }

    public synchronized void SERVER_STOP() {
        long serverStop = System.currentTimeMillis();
        if (SERVER_STATUS != SERVER_STATUS_STARTED)
            return;
        SERVER_STATUS = SERVER_STATUS_STOPPING;
        logger.info("ArkServer 启动销毁中 .......");
        arkService.destroy();
        RedisUtil.clearAll();
        logger.info("ArkServer 销毁耗时：" + (System.currentTimeMillis() - serverStop) + "");
        SERVER_STATUS = SERVER_STATUS_PENDING;
    }

    @Override
    public void destroy() throws Exception {
        SERVER_STOP();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SERVER_STATUS = SERVER_STATUS_PENDING;
        SERVER_START();
    }

}
