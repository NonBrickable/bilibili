package com.bilibili.dao;

import com.bilibili.pojo.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthRoleElementOperationDao {
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(@Param("roleIdList") List<Long> roleIdList);
}
