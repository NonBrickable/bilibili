package com.bilibili.service;
import com.bilibili.dao.AuthRoleOperationDao;
import com.bilibili.pojo.auth.AuthRoleOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色-元素操作关联
 */
@Service
public class AuthRoleOperationService {
    @Autowired
    private AuthRoleOperationDao authRoleOperationDao;
    public List<AuthRoleOperation> getRoleOperationsByRoleIds(List<Long> roleIdList) {
        return authRoleOperationDao.getRoleOperationsByRoleIds(roleIdList);
    }
}
