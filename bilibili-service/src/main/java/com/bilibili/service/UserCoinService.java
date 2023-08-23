package com.bilibili.service;

import com.bilibili.dao.UserCoinDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCoinService {
    @Autowired
    private UserCoinDao userCoinDao;

    public Long getUserCoinsAmount(Long userId) {
        return userCoinDao.getUserCoinsAmount(userId);
    }

    public void updateUserCoinsAmount(Long userId, Long userCoinsAmount) {
        userCoinDao.updateUserCoinsAmount(userId,userCoinsAmount);
    }
}
