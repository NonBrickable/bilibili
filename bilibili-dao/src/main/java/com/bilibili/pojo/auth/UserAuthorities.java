package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装了某个用户的所有权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorities {
    //根据userId查到roleId，在通过roleId获取所有可以操作的元素List
    List<AuthRoleOperation> authRoleOperationList;
    //再通过roleId获取可以得到的菜单List
    List<AuthRoleMenu> authRoleMenuList;

}
