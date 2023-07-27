package com.bilibili.controller;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.common.JsonResponse;
import com.bilibili.common.PageResult;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.User;
import com.bilibili.pojo.UserInfo;
import com.bilibili.service.UserFollowingService;
import com.bilibili.service.UserService;
import com.bilibili.util.RSAUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSupport userSupport;

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 获取RSA公钥
     * @return
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String pk = RSAUtil.getPublicKeyStr();
        return JsonResponse.success(pk);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    /**
     * 单令牌登录
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/user-tokens")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/userInfo")//4.获取用户信息
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    /**
     * 更新用户基本信息
     * @param user
     * @return
     * @throws Exception
     */
    @PutMapping("/updateUsers")
    public JsonResponse<String> updateUsers(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUsers(user);
        return JsonResponse.success();
    }

    /**
     * 更新用户详细信息
     * @param userInfo
     * @return
     */
    @PutMapping("/updateUserInfos")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

    /**
     * 分页查询用户信息
     * @param no 当前页码
     * @param size 当前页有多少条数据
     * @param nick 昵称
     * @return
     */
    @GetMapping("page-user")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer no, @RequestParam Integer size, String nick) {
        Long userId = userSupport.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("no", no);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        if (result.getTotal() > 0){
            List<UserInfo> userInfoList = userFollowingService.checkFollowingStatus(result.getList(),userId);
            result.setList(userInfoList);
        }
            return new JsonResponse<>(result);
    }

    /**
     * 双令牌登录
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/user-dts")
    public JsonResponse<Map<String,Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String,Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    /**
     * 登出
     * @param request
     * @return
     */
    @DeleteMapping("/logout")
    public JsonResponse<String> logout(HttpServletRequest request){
        String refreshToken = request.getHeader("refreshToken");
        Long userId = userSupport.getCurrentUserId();
        userService.logout(refreshToken,userId);
        return JsonResponse.success();
    }

    /**
     * 获取新的accessToken————流程：accessToken过期，前端调用生成该接口
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("new-access-token")
    public JsonResponse<String> refreshAccessToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }
}
