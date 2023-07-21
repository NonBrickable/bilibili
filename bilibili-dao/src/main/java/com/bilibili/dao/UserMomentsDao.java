package com.bilibili.dao;

import com.bilibili.pojo.UserMoments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentsDao {
    Integer addUserMoments(UserMoments userMoments);
}
