package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.UserMoments;
import com.bilibili.service.UserMomentsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMomentsController {
    private UserSupport userSupport;
    private UserMomentsService userMomentsService;

    //1.新增动态
    @PostMapping("/add-moment")
    public JsonResponse<String> addUserMoments(UserMoments userMoments){
        Long userId = userSupport.getCurrentUserId();
        userMoments.setUserId(userId);
        userMomentsService.addUserMoments(userMoments);
        return new JsonResponse<>("新建动态成功");
    }
}
