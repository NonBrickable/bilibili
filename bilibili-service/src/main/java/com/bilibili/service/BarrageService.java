package com.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.BarrageDao;
import com.bilibili.pojo.Barrage;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BarrageService {
    @Autowired
    private BarrageDao barrageDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Async
    public void asyncAddBarrage(Barrage barrage) {
        barrageDao.addBarrage(barrage);
    }

    /**
     * 获取弹幕/条件筛选弹幕
     * @param videoId
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public List<Barrage> getBarrages(Long videoId, String startTime, String endTime) throws Exception {
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

    /**
     * 放一条数据到Redis中
     * @param barrage
     */
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
