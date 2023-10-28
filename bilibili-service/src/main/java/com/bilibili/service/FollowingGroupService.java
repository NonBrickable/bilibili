package com.bilibili.service;

import com.bilibili.common.UserConstant;
import com.bilibili.dao.FollowingGroupDao;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingGroupService {
    @Autowired
    private FollowingGroupDao followingGroupDao;

    public FollowingGroup getByType(String type){
        return followingGroupDao.getByType(type);
    }
    public FollowingGroup getById(Long id){
        return followingGroupDao.getById(id);
    }

    public List<FollowingGroup> getFollowingGroupByUserId(Long userId){
        return followingGroupDao.getFollowingGroupByUserId(userId);
    }
    public Long addUserFollowingGroup(FollowingGroup followingGroup){
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupDao.addUserFollowingGroup(followingGroup);
        return followingGroup.getId();
    }

    public void deleteUserFollowingGroup(FollowingGroup followingGroup) {
        Long id = followingGroup.getId();
        if(id == null){
            throw new ConditionException("分组不存在");
        }
        followingGroupDao.deleteUserFollowingGroup(followingGroup);
    }
}













