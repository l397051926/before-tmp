package com.gennlife.platform.bean;

/**
 * Created by chensong on 2015/12/12.
 */
public class PoolBean {
    private String name;
    private int corePoolSize;
    private int maximumPoolSize;
    private int keepAliveTime;
    private int queueSize;
    private boolean rejectDiscard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public boolean isRejectDiscard() {
        return rejectDiscard;
    }

    public void setRejectDiscard(boolean rejectDiscard) {
        this.rejectDiscard = rejectDiscard;
    }
}
