package com.bilibili.util;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.util.concurrent.TimeUnit;

//RocketMQ工具类
public class RocketMQUtil {
    public static void syncSendMsg(DefaultMQProducer producer, Message msg)throws Exception{
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
    }
    public static void asyncSendMsg(DefaultMQProducer producer,Message msg) throws Exception{
        int messageCount = 2;
        CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for(int i = 0;i < messageCount;i++){
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.println("发送消息的时候出现异常" + e);
                    e.printStackTrace();
                }
            });
            countDownLatch.await(5, TimeUnit.SECONDS);
        }
    }

}
