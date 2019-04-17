package com.gennlife.platform.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ProjectConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectConsumer.class);
    @Autowired
    private RocketMqContent rocketMqContent;

    @PostConstruct
    public void defaultMQPushConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rocketMqContent.getConsumerGroup());
        consumer.setNamesrvAddr(rocketMqContent.getNamesrvAddr());
        try {
            consumer.subscribe(rocketMqContent.getTopicPro(), "*");

            // 如果是第一次启动，从队列头部开始消费
            // 如果不是第一次启动，从上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //设置广播消费
            consumer.setMessageModel(MessageModel.BROADCASTING);

            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        //TODO 判定messageExt 中的 tag 的类别 然后做不同的业务
                        String tag = messageExt.getTags();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
            System.out.println("[Consumer 已启动]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}