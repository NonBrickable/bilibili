package com.playtube.dao;

import com.playtube.pojo.Barrage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BarrageDao {
    void addBarrage(Barrage barrage);
    List<Barrage> getBarrages(Map<String, Object> params);
}
