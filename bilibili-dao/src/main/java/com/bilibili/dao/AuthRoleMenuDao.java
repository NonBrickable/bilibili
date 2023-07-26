package com.bilibili.dao;

import com.bilibili.pojo.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface AuthRoleMenuDao {
    List<AuthRoleMenu> getAuthRoleMenus(List<Long> roleIdList);
}
