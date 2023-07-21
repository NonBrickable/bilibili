package com.bilibili.service;

import com.bilibili.dao.UserMomentsDao;
import com.bilibili.pojo.UserMoments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMomentsService {
    @Autowired
    private UserMomentsDao userMomentsDao;

    public void addUserMoments(UserMoments userMoments) {
        userMomentsDao.addUserMoments(userMoments);
    }
}
