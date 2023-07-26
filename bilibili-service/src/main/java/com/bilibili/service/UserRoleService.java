package com.bilibili.service;

import com.bilibili.dao.UserRoleDao;
import com.bilibili.pojo.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户-角色关联
 */
@Service
public class UserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;

    /**
     * 获取useId对应的所有角色
     * @param userId
     * @return
     */
    public List<UserRole> getUserRole(long userId) {
        List<UserRole> userRoleList = userRoleDao.getUserRole(userId);
        return userRoleList;
    }
}
