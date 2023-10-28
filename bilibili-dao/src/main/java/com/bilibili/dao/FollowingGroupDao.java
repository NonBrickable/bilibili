package com.bilibili.dao;

import com.bilibili.pojo.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowingGroupDao {
    FollowingGroup getByType(String type);
    FollowingGroup getById(Long id);
    List<FollowingGroup> getFollowingGroupByUserId(Long userId);

    Integer addUserFollowingGroup(FollowingGroup followingGroup);

    void deleteUserFollowingGroup(FollowingGroup followingGroup);
}
