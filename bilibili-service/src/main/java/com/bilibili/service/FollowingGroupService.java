package com.bilibili.service;

import com.bilibili.dao.FollowingGroupDao;
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
}
