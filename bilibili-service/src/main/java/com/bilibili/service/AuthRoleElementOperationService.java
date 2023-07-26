package com.bilibili.service;

import com.bilibili.dao.AuthRoleElementOperationDao;
import com.bilibili.pojo.auth.AuthRoleElementOperation;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色-元素操作关联
 */
@Service
public class AuthRoleElementOperationService {
    @Autowired
    private AuthRoleElementOperationDao authRoleElementOperationDao;
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(List<Long> roleIdList) {
        return authRoleElementOperationDao.getRoleElementOperationsByRoleIds(roleIdList);
    }
}
