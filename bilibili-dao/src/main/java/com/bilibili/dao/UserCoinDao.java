package com.bilibili.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserCoinDao {

    Long getUserCoinsAmount(@Param("userId") Long userId);

    void updateUserCoinsAmount(@Param("userId") Long userId, @Param("userCoinsAmount") Long userCoinsAmount);
}
