package com.gennlife.platform.configuration;


import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * Created by chen-song on 2016/11/15.
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private JedisCluster jedisCluster;
    private int connectionTimeout = 2000;
    private int soTimeout = 3000;
    private int maxRedirections = 5;
    private Set<String> jedisClusterNodes;

    public JedisClusterFactory() {
    }

    public JedisCluster getObject() throws Exception {
        return this.jedisCluster;
    }

    public Class<?> getObjectType() {
        return this.jedisCluster != null?this.jedisCluster.getClass():JedisCluster.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        if(this.jedisClusterNodes != null && this.jedisClusterNodes.size() != 0) {
            HashSet haps = new HashSet();
            Iterator i$ = this.jedisClusterNodes.iterator();

            while(i$.hasNext()) {
                String node = (String)i$.next();
                String[] arr = node.split(":");
                if(arr.length != 2) {
                    throw new ParseException("node address error !", node.length() - 1);
                }

                haps.add(new HostAndPort(arr[0], Integer.valueOf(arr[1]).intValue()));
            }

            this.jedisCluster = new JedisCluster(haps, this.connectionTimeout, this.soTimeout, this.maxRedirections, this.genericObjectPoolConfig);
        } else {
            throw new NullPointerException("jedisClusterNodes is null.");
        }
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return this.genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public JedisCluster getJedisCluster() {
        return this.jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxRedirections() {
        return this.maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public Set<String> getJedisClusterNodes() {
        return this.jedisClusterNodes;
    }

    public void setJedisClusterNodes(Set<String> jedisClusterNodes) {
        this.jedisClusterNodes = jedisClusterNodes;
    }
}
