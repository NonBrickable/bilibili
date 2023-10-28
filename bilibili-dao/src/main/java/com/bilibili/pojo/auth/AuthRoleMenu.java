package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 角色-菜单关联表
 */
@Data
@NoArgsConstructor
public class AuthRoleMenu {
    private Long id;

    private Long roleId;

    private Long menuId;

    private Date createTime;

    private Date updateTime;
    private AuthMenu authMenu;//冗余字段
}
