package com.gennlife.platform.rocketmq;

import com.alibaba.fastjson.JSONObject;
import com.gennlife.platform.ReadConfig.ReadConditionByRedis;
import com.gennlife.platform.model.Group;
import com.gennlife.platform.model.Role;
import com.gennlife.platform.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProducerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerService.class);
    private static Gson gson = GsonUtil.getGson();

    @Autowired
    private RocketMqContent rocketMqContent;

    private DefaultMQProducer producer;

    @PostConstruct
    public void initProducer() {
        producer = new DefaultMQProducer(rocketMqContent.getProducerGroup());
        producer.setNamesrvAddr(rocketMqContent.getNamesrvAddr());
        producer.setRetryTimesWhenSendFailed(3);
        try {
            producer.start();
            System.out.println("[Producer 已启动]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String send(String topic, String tags, String msg) {
        SendResult result = null;
        try {
            Message message = new Message(topic, tags, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            result = producer.send(message);
            LOGGER.info("发送了一条消息  msgID(" + result.getMsgId() + ") 结果为： " + result.getSendStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"MsgId\":\"" + result.getMsgId() + "\"}";
    }

    @PreDestroy
    public void shutDownProducer() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    public void checkOutPower(String userId) {
        String msg = "管理员调整了你的权限";
        JSONObject data = new JSONObject()
            .fluentPut("user_id", userId)
            .fluentPut("msg", addPowerMsg(msg));
        send(rocketMqContent.getTopicAuth(), rocketMqContent.getChangeUserPowerTag(), data.toJSONString());
    }

    public void sendSystemMessage(String content,String msg) {
        JSONObject data = new JSONObject()
            .fluentPut("content", addSysMsg(content))
            .fluentPut("detail", msg);
        send(rocketMqContent.getTopicSys(), rocketMqContent.getSysUpdateTag(), data.toJSONString());
    }

    private String addPowerMsg(String msg){
        return "【权限】 " + msg;
    }
    private String addSysMsg(String msg){
        return "【系统】 " + msg;
    }

    public void checkOutPowerByAddRole(JsonObject paramObj) {
        Role role = gson.fromJson(paramObj, Role.class);
        List<String> uidList = (List<String>) role.getStaff();
        for (String uid : uidList){
            checkOutPower(uid);
        }
    }

    public void checkOutPowerByEditGroup(String param) {
       Group group = gson.fromJson(param, Group.class);
        List<String> list = (List<String>) group.getMembers();
        for (String uid : list){
            checkOutPower(uid);
        }
    }

    public void checkOutPowerByUids(List<String> uids) {
        Set<String> set = new HashSet<>();
        for (String uid : uids){
            if(!set.contains(uid)){
                set.add(uid);
                checkOutPower(uid);
            }
        }
    }

    public void sendSystemStartMessage() {
        JsonObject config = ReadConditionByRedis.getSysMessageConfig();
        String content = config.get("content").getAsString();
        String msg = config.get("detail").getAsString();
//        sendSystemMessage(content,msg);

    }
}

