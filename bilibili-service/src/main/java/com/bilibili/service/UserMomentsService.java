package com.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.common.UserMomentsConstant;
import com.bilibili.dao.UserMomentsDao;
import com.bilibili.pojo.UserMoments;
import com.bilibili.util.RocketMQUtil;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserMomentsService {
    @Autowired
    private UserMomentsDao userMomentsDao;
    @Autowired
    private static ApplicationContext applicationContext;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void addUserMoments(UserMoments userMoments) throws Exception {
        userMomentsDao.addUserMoments(userMoments);
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentProducer");
        Message msg = new Message(UserMomentsConstant.MOMENTS_TOPIC, JSONObject.toJSONString(userMoments).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer,msg);
    }
    public List<UserMoments> getUserSubscribedMoments(Long userId) {
        String key ="subscribed-" + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        if(StringUtil.isNullOrEmpty(listStr)){
            return null;
        }
        return JSONArray.parseArray(listStr,UserMoments.class);
    }
}
