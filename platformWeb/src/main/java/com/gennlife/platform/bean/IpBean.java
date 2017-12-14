package com.gennlife.platform.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Chenjinfeng on 2017/12/13.
 */
@Component
public class IpBean {
    @Value("${gennIpAndPort}")
    private String gennIpAndPort;

    public String getGennIpAndPort() {
        return gennIpAndPort;
    }

    public void setGennIpAndPort(String gennIpAndPort) {
        this.gennIpAndPort = gennIpAndPort;
    }
}
