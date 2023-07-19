package com.bilibili.dao;

import com.bilibili.pojo.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowingDao {
    Integer deleteFollowings(@Param("userId") Long userId,@Param("followingId") Long followingId);
    Integer addFollowings(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowings(Long userId);
    List<UserFollowing> getUserFansList(Long userId);

    Integer checkFollowingStatus(@Param("followingId") Long followingId, @Param("userId") Long userId);
}
