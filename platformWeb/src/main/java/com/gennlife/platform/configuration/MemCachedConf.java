package com.gennlife.platform.configuration;

/**
 * Created by chen-song on 16/9/23.
 */
public class MemCachedConf {
    /* cache server的IP和端口*/
    private String[] servers;
    /* 每个cache server的权重*/
    private Integer[] weights;
    /* 初始时每个cache服务器的可用连接数 */
    private int initConnections;
    /* 设置每个服务器最少可用连接数 */
    private int minConnections;
    /* 设置每个服务器最大可用连接数 */
    private int maxConnections;
    /* 设置可用连接池的最长等待时间*/
    private long maxIdle;
    /* 设置连接池维护线程的睡眠时间 */
    private long maintSleep;
    /* 是否使用Nagle算法*/
    private boolean setNagle;
    /* socket的连接等待超时值*/
    private int socketConnectTO;
    /* socket的读取等待超时值*/
    private int socketTO;

    public String[] getServers() {
        return servers;
    }

    public void setServers(String[] servers) {
        this.servers = servers;
    }

    public Integer[] getWeights() {
        return weights;
    }

    public void setWeights(Integer[] weights) {
        this.weights = weights;
    }

    public int getInitConnections() {
        return initConnections;
    }

    public void setInitConnections(int initConnections) {
        this.initConnections = initConnections;
    }

    public int getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(int minConnections) {
        this.minConnections = minConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public long getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(long maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaintSleep() {
        return maintSleep;
    }

    public void setMaintSleep(long maintSleep) {
        this.maintSleep = maintSleep;
    }

    public boolean isSetNagle() {
        return setNagle;
    }

    public void setSetNagle(boolean setNagle) {
        this.setNagle = setNagle;
    }

    public int getSocketConnectTO() {
        return socketConnectTO;
    }

    public void setSocketConnectTO(int socketConnectTO) {
        this.socketConnectTO = socketConnectTO;
    }

    public int getSocketTO() {
        return socketTO;
    }

    public void setSocketTO(int socketTO) {
        this.socketTO = socketTO;
    }
}
