package com.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.BarrageDao;
import com.bilibili.pojo.Barrage;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BarrageService {
    @Autowired
    private BarrageDao barrageDao;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    public void addBarrage(Barrage barrage){
        barrageDao.addBarrage(barrage);
    }
    public List<Barrage> getBarrages(Map<String,Object> params){
        return barrageDao.getBarrages(params);
    }
    public void addBarrageToRedis(Barrage barrage){
        String key = "barrage-video-" + barrage.getVideoId();
        //获取redis里关于某视频的所有弹幕
        String value = redisTemplate.opsForValue().get(key);
        List<Barrage> list= new ArrayList<>();
        if(!StringUtil.isNullOrEmpty(value)){
            list = JSONArray.parseArray(value,Barrage.class);
        }
        list.add(barrage);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }
}
