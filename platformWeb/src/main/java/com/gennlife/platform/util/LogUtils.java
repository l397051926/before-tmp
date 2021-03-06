package com.gennlife.platform.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chensong on 2015/12/4.
 */
public class LogUtils {
    private static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat time_ = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    private static Logger logger = LoggerFactory.getLogger("businesslog");

    public static void BussnissLog(String log) {
        logger.info(log);
    }

    public static void BussnissLogError(String log) {
        logger.error(log);
    }

    public static void BussnissLogError(String log, Exception e) {
        logger.error(log, e);
    }

    public static String getStringTime() {
        return time.format(new Date());
    }

    public static String getString_Time() {
        return time_.format(new Date());
    }

    public static Boolean decideDate(String effective,String failure)  {
        Date date=new Date();
        try {
            return date.before(time.parse(failure)) && date.after(time.parse(effective));
        } catch (ParseException e) {
            logger.error(e+"");
        }
        return false;
    }

}
