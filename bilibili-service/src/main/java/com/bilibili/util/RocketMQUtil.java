package com.bilibili.util;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

//RocketMQ工具类
public class RocketMQUtil {
    public static void syncSendMsg(DefaultMQProducer producer, Message msg)throws Exception{
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
        producer.shutdown();
    }
    public static void asyncSendMsg(DefaultMQProducer producer,Message msg) throws Exception{
        int messageCount = 2;
        for(int i = 0;i < messageCount;i++){
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable throwable) {
                    System.out.println("出现异常：" + throwable);
                    throwable.printStackTrace();
                }
            });
        }
        producer.shutdown();
    }
    public static void oneWaySendMsg(DefaultMQProducer producer,Message msg) throws RemotingException, InterruptedException, MQClientException {
        int messageCount = 2;
        for(int i = 0;i < messageCount;i++){
            producer.sendOneway(msg);
        }
        producer.shutdown();
    }

}
