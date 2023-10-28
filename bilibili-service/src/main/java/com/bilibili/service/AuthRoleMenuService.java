package com.bilibili.service;

import com.bilibili.dao.AuthRoleMenuDao;
import com.bilibili.pojo.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthRoleMenuService {
    @Autowired
    private AuthRoleMenuDao authRoleMenuDao;
    /**
     * 获取角色对应菜单的list
     * @param roleIdList
     * @return
     */
    public List<AuthRoleMenu> getAuthRoleMenus(List<Long> roleIdList) {
        return authRoleMenuDao.getAuthRoleMenus(roleIdList);
    }
}
