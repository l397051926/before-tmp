package com.gennlife.platform.util;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.gennlife.platform.configuration.MemCachedConf;
import com.gennlife.platform.model.User;
import org.springframework.context.ApplicationContext;

/**
 * Created by chen-song on 16/9/23.
 */
public class MemCachedUtil {
    private static MemCachedClient mcc = new MemCachedClient();
    private static MemCachedConf memCachedConf =  null;

    private MemCachedUtil() {};

    static {
        ApplicationContext context = SpringContextUtil.getApplicationContext();
        memCachedConf = (MemCachedConf) context.getBean("MemCachedConf");

        /* 拿到一个连接池的实例 */
        SockIOPool pool = SockIOPool.getInstance();

        /* 设置服务器信息 */
        pool.setServers(memCachedConf.getServers());
        pool.setWeights(memCachedConf.getWeights());

        /* 配置缓冲池的一些基础信息 */
        pool.setInitConn(memCachedConf.getInitConnections());
        pool.setMinConn(memCachedConf.getMinConnections());
        pool.setMaxConn(memCachedConf.getMaxConnections());
        pool.setMaxIdle(memCachedConf.getMaxIdle());

        /* 设置线程休眠时间 */
        pool.setMaintSleep(memCachedConf.getMaintSleep());

        /* 设置关于TCP连接 */
        pool.setNagle(memCachedConf.isSetNagle());// 禁用nagle算法
        pool.setSocketConnectTO(memCachedConf.getSocketConnectTO());
        pool.setSocketTO(memCachedConf.getSocketTO());

        /* 初始化 */
        pool.initialize();

    }

    public static boolean set(String key, String value) {
        return mcc.set(key, value);
    }

    public static String get(String key) {
        if (mcc.keyExists(key))
            return (String) mcc.get(key);
        else
            return null;
    }
    public static boolean addUser(String email,User user){
        return mcc.set(email, user);
    }
}
