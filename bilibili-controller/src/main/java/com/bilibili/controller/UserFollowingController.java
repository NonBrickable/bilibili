package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.FollowingGroup;
import com.bilibili.pojo.UserFollowing;
import com.bilibili.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserFollowingController {
    @Autowired
    private UserFollowingService userFollowingService;
    @Autowired
    private UserSupport userSupport;

    //1.新增关注用户
    @PostMapping("/add-following")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing) {
        long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }


    //2.获取关注分组
    // TODO: 2023/7/19 存在bug
    @GetMapping("/following-list")
    public JsonResponse<List<FollowingGroup>> getUserFollowings() {
        long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(list);
    }

    //3.获取粉丝列表
    // TODO: 2023/7/19  存在bug
    @GetMapping("/fans-list")
    public JsonResponse<List<UserFollowing>> getUserFans() {
        long userId = userSupport.getCurrentUserId();
        List<UserFollowing> list = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(list);
    }

    @GetMapping("/test")
    public JsonResponse<List<FollowingGroup>> test(){
        long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.test(userId);
        return new JsonResponse<>(list);
    }
}
