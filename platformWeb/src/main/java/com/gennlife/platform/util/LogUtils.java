package com.gennlife.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chensong on 2015/12/4.
 */
public class LogUtils {
    private static Logger logger = LoggerFactory.getLogger("businesslog");
    public static void BussnissLog(String log){
        logger.info(log);
    }
}
