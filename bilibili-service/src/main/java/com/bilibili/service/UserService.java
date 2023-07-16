package com.bilibili.service;

import com.bilibili.common.JsonResponse;
import com.bilibili.common.UserConstant;
import com.bilibili.dao.UserDao;
import com.bilibili.pojo.*;
import com.bilibili.exception.ConditionException;
import com.bilibili.util.MD5Util;
import com.bilibili.util.RSAUtil;
import com.bilibili.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    //注册
    public JsonResponse<String> addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User dbuser = this.getUserByphone(phone);
        if (dbuser != null) {
            throw new ConditionException("该手机号已经注册");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();//获取密码
        String rawpassword;
        try {
            rawpassword = RSAUtil.decrypt(password);//获得经过MD5加密接着进行解密后的密码
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        //将创建数据放入User
        String md5password = MD5Util.sign(rawpassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5password);
        user.setCreateTime(now);
        userDao.addUser(user);
        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
        return JsonResponse.success("成功");
    }

    public User getUserByphone(String phone) {
        return userDao.getUserByphone(phone);
    }


    //登录
    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User dbuser = this.getUserByphone(phone);
        if (dbuser == null) {
            throw new ConditionException("该用户未注册");
        }
        String password = user.getPassword();
        String rawpassword;
        try {
            rawpassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt = dbuser.getSalt();
        String md5password = MD5Util.sign(rawpassword, salt, "UTF-8");
        if (md5password.equals(password)) {
            throw new ConditionException("密码错误");
        }
        return TokenUtil.generateToken(dbuser.getId());
    }

    //获取用户信息
    public User getUserInfo(Long userid) {
        User user = userDao.getUserByid(userid);
        UserInfo userInfo = userDao.getUserInfoByid(userid);
        user.setUserInfo(userInfo);
        return user;
    }
}
