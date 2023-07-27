package com.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.common.JsonResponse;
import com.bilibili.common.PageResult;
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
    @Autowired
    private UserRoleService userRoleService;

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
     * 登录
     *
     * @param user
     * @return
     * @throws Exception
     */
    public String login(User user) throws Exception {
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
        return TokenUtil.generateToken(dbuser.getId());
    }

    //获取用户信息
    public User getUserInfo(Long userid) {
        User user = userDao.getUserById(userid);
        UserInfo userInfo = userDao.getUserInfoById(userid);
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
        //保存refreshToken到数据库
        userDao.deleteRefreshToken(refreshToken,userId);
        userDao.addRefreshToken(refreshToken,userId);
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
    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken,userId);
    }

    /**
     * 刷新accessToken
     * @param refreshToken
     * @return
     */
    public String refreshAccessToken(String refreshToken) throws Exception {
        FreshTokenDetail freshTokenDetail = userDao.getFreshTokenDetail(refreshToken);
        if(freshTokenDetail == null){
            throw new ConditionException("555","token过期");
        }
        Long userId = freshTokenDetail.getUserId();
        String accessToken = TokenUtil.generateToken(userId);
        return accessToken;
    }
}
