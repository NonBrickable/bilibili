package com.bilibili.dao;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.pojo.User;
import com.bilibili.pojo.UserFollowing;
import com.bilibili.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {

    User getUserByphone(String phone);
    Integer addUser(User user);
    Integer addUserInfo(UserInfo userInfo);
    User getUserById(Long id);
    UserInfo getUserInfoById(Long userId);
    Integer updateUsers(User user);
    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(List<Long> followingIdList);
}
