package com.bilibili.config;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.common.UserMomentsConstant;
import com.bilibili.pojo.UserFollowing;
import com.bilibili.pojo.UserMoments;
import com.bilibili.service.UserFollowingService;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

//mq的配置类
@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserFollowingService userFollowingService;

    @Bean("momentProducer")
    public DefaultMQProducer momentProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.MOMENTS_GROUP);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    @Bean("momentConsumer")
    public DefaultMQPushConsumer momentConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.MOMENTS_GROUP);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.MOMENTS_TOPIC, "*");//订阅主题与二级主题
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                if(msg == null){
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(msg.getBody());
                UserMoments userMoments = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr),UserMoments.class);
                Long userId = userMoments.getUserId();
                List<UserFollowing> userFans = userFollowingService.getUserFans(userId);
                for(UserFollowing fan : userFans){
                    String key = "subscribed-" + fan.getUserId();//定义redis里的key
                    String subscribedListStr = redisTemplate.opsForValue().get(key);
                    List<UserMoments> userMomentsList;
                    if(StringUtil.isNullOrEmpty(subscribedListStr)){
                        userMomentsList = new ArrayList<>();
                    }else{
                        userMomentsList = JSONArray.parseArray(subscribedListStr,UserMoments.class);
                    }
                    userMomentsList.add(userMoments);
                    redisTemplate.opsForValue().set(key,JSONObject.toJSONString(userMomentsList));
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }
}
