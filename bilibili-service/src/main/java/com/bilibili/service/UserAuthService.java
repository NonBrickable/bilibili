package com.bilibili.service;

import com.bilibili.pojo.auth.AuthRoleMenu;
import com.bilibili.pojo.auth.AuthRoleElementOperation;
import com.bilibili.pojo.auth.UserAuthorities;
import com.bilibili.pojo.auth.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限集成
 */
@Service
public class UserAuthService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AuthRoleService authRoleService;
    public UserAuthorities getUserAuthorities(long userId) {
        List<UserRole> userRoleList = userRoleService.getUserRole(userId);
        List<Long> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        //批量查询权限
        List<AuthRoleElementOperation> authRoleElementOperationList = authRoleService.getRoleElementOperations(roleIdList);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenus(roleIdList);
        UserAuthorities userAuthorities = new UserAuthorities(authRoleElementOperationList,authRoleMenuList);
        return userAuthorities;
    }
}
