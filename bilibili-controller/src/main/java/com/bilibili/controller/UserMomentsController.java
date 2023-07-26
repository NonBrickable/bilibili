package com.bilibili.controller;

import com.bilibili.common.AuthRoleConstant;
import com.bilibili.common.JsonResponse;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.UserMoments;
import com.bilibili.pojo.annotation.ControllerLimitedRole;
import com.bilibili.pojo.annotation.DataLimited;
import com.bilibili.pojo.auth.AuthRole;
import com.bilibili.service.UserMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserMomentsController {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserMomentsService userMomentsService;

    /**
     * 新增动态
     *
     * @param userMoments
     * @return
     * @throws Exception
     */
    @ControllerLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @DataLimited
    @PostMapping("/add-moment")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoments userMoments) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoments.setUserId(userId);
        userMomentsService.addUserMoments(userMoments);
        return new JsonResponse<>("新建动态成功");
    }

    /**
     * 获取动态
     *
     * @return
     */
    @GetMapping("/user-subscribed-moments")
    public JsonResponse<List<UserMoments>> getUserSubscribedMoments() {
        long userId = userSupport.getCurrentUserId();
        List<UserMoments> userMomentsList = userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(userMomentsList);
    }
}
