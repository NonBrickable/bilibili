package com.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.common.JsonResponse;
import com.bilibili.common.PageResult;
import com.bilibili.common.RedisCacheConstant;
import com.bilibili.common.UserConstant;
import com.bilibili.dao.UserDao;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.*;
import com.bilibili.util.MD5Util;
import com.bilibili.util.RSAUtil;
import com.bilibili.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 注册
     *
     * @param user
     * @return
     */
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
        userDao.addUser(user);
        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userDao.addUserInfo(userInfo);
        userRoleService.setDefaultRole(user.getId());
        return JsonResponse.success("成功");
    }

    public User getUserByphone(String phone) {
        return userDao.getUserByphone(phone);
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return
     */
    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoById(userId);
        user.setUserInfo(userInfo);
        return user;
    }


    //更新用户基本信息
    public void updateUsers(User user) throws Exception {
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);
        if (dbUser == null) {
            throw new ConditionException("用户不存在");
        }
        if (!StringUtils.isNullOrEmpty(user.getPassword())) {
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        userDao.updateUsers(user);
    }

    //更新用户详细信息
    public void updateUserInfos(UserInfo userInfo) {
        userDao.updateUserInfos(userInfo);
    }

    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    public List<UserInfo> getUserInfoByIds(List<Long> followingIdList) {
        return userDao.getUserInfoByUserIds(followingIdList);
    }

    //no-当前页码 size-当前一页有多少条数据 nick-昵称
    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("start", (no - 1) * size);//起始页码，查第一页的话，就从数据库第0条开始查
        params.put("limit", size);//每次查询多少条数据
        Integer total = userDao.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if (total > 0) {
            list = userDao.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

    /**
     * 双令牌登录
     * @param user
     * @return
     * @throws Exception
     */
    @Transactional
    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User dbuser = this.getUserByphone(phone);
        if (dbuser == null) {
            throw new ConditionException("该用户未注册");
        }
        String password = user.getPassword();//获取前端传过来的密码
        String rawpassword;
        try {
            rawpassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败");
        }
        String salt = dbuser.getSalt();
        String md5password = MD5Util.sign(rawpassword, salt, "UTF-8");
        if (!md5password.equals(dbuser.getPassword())) {
            throw new ConditionException("密码错误");
        }
        Long userId = dbuser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    /**
     * 登出
     * @param refreshToken
     * @param userId
     */
    public void logout(String accessToken,String refreshToken, Long userId) {
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_LOGOUT + accessToken,userId.toString(),1, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_LOGOUT + refreshToken,userId.toString(),7, TimeUnit.DAYS);
    }

    /**
     * 刷新accessToken
     * @param refreshToken
     * @return
     */
    public Map<String, String> refreshAccessToken(String refreshToken,Long userId) throws Exception {
        redisTemplate.opsForValue().set(RedisCacheConstant.USER_LOGOUT + refreshToken,userId.toString(),7,TimeUnit.DAYS);
        String accessToken = TokenUtil.generateToken(userId);
        String newRefreshToken = TokenUtil.generateRefreshToken(userId);
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken",accessToken);
        tokenMap.put("refreshToken",newRefreshToken);
        return tokenMap;
    }
    public List<UserInfo> batchGetUserInfo(Set<Long> userIdList) {
        return userDao.batchGetUserInfo(userIdList);
    }
}
