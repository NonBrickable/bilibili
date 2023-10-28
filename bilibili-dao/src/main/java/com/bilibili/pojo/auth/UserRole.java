package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户角色关联表
 */
@Data
@NoArgsConstructor
public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
    private String roleName;//冗余1
    private String roleCode;//冗余2
    private Date createTime;
    private Date updateTime;
}
