package com.bilibili.service;

import com.bilibili.dao.AuthRoleDao;
import com.bilibili.pojo.auth.AuthRoleOperation;
import com.bilibili.pojo.auth.AuthRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthRoleService {

    @Autowired
    private AuthRoleOperationService authRoleOperationService;
    @Autowired
    private AuthRoleMenuService authRoleMenuService;
    @Autowired
    private AuthRoleDao authRoleDao;

    /**
     * 批量获取角色对应的操作权限
     * @param roleIdList
     * @return
     */
    public List<AuthRoleOperation> getRoleOperations(List<Long> roleIdList) {
        return authRoleOperationService.getRoleOperationsByRoleIds(roleIdList);
    }

    /**
     * 批量获取角色对应的菜单
     * @param roleIdList
     * @return
     */
    public List<AuthRoleMenu> getAuthRoleMenus(List<Long> roleIdList) {
        return authRoleMenuService.getAuthRoleMenus(roleIdList);
    }

    public Long getIdByRoleCode(String roleCode) {
        return authRoleDao.getIdByRoleCode(roleCode);
    }
}
