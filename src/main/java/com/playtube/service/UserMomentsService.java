package com.playtube.service;

import com.playtube.pojo.UserMoments;

import java.util.List;

public interface UserMomentsService {

    /**
     * 新增动态
     * @param userMoments
     */
    void addUserMoments(UserMoments userMoments) throws Exception;

    /**
     * 获取动态
     * @param start 开始条数
     * @param end   结束条数
     * @return
     */
    List<UserMoments> getUserSubscribedMoments(Long start, Long end);
}
