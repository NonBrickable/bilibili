package com.bilibili.dao;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.pojo.User;
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
    User getUserByid(Long id);
    UserInfo getUserInfoByid(Long userId);

}
