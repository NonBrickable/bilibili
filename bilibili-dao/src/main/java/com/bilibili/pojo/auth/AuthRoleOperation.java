package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 角色-操作关联表
 */
@Data
@NoArgsConstructor
public class AuthRoleOperation {
    private Long id;
    private Long roleId;
    private Long elementId;
    private Date createTime;
    private Date updateTime;
    private AuthOperation authOperation;//联表，防止查询两次
}
