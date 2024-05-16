package com.bilibili.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.BarrageDao;
import com.bilibili.pojo.Barrage;
import com.bilibili.service.BarrageService;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BarrageServiceImpl implements BarrageService {
    private final BarrageDao barrageDao;
    private final RedisTemplate<String, String> redisTemplate;

    @Async
    public void asyncAddBarrage(Barrage barrage) {
        barrageDao.addBarrage(barrage);
    }

    public List<Barrage> getBarrages(Long videoId, String startTime, String endTime) throws Exception{
        String key = "barrage-video-" + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Barrage> list;
        if (!StringUtil.isNullOrEmpty(value)) {
            list = JSONArray.parseArray(value, Barrage.class);
            if (!StringUtil.isNullOrEmpty(startTime) && !StringUtil.isNullOrEmpty(endTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Barrage> result = new ArrayList<>();
                for (Barrage bar : list) {
                    Date createTime = bar.getCreateTime();
                    if (createTime.after(startDate) && createTime.before(endDate)) {
                        result.add(bar);
                    }
                }
                list = result;
            }
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("videoId", videoId);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            list = barrageDao.getBarrages(params);
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
        }
        return list;
    }


    public void addBarrageToRedis(Barrage barrage) {
        String key = "barrage-video-" + barrage.getVideoId();
        //获取redis里关于某视频的所有弹幕
        String value = redisTemplate.opsForValue().get(key);
        List<Barrage> list = new ArrayList<>();
        if (!StringUtil.isNullOrEmpty(value)) {
            list = JSONArray.parseArray(value, Barrage.class);
        }
        list.add(barrage);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }
}
