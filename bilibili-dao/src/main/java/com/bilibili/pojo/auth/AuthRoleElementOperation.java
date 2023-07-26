package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 角色-元素操作关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRoleElementOperation {
    private Long id;
    private Long roleId;
    private Long elementId;
    private Date createTime;
    private Date updateTime;
    private AuthElementOperation authElementOperation;//联表，防止查询两次
}
