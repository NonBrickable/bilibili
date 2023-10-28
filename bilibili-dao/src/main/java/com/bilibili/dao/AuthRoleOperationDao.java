package com.bilibili.dao;

import com.bilibili.pojo.auth.AuthRoleOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthRoleOperationDao {
    List<AuthRoleOperation> getRoleOperationsByRoleIds(@Param("roleIdList") List<Long> roleIdList);
}
