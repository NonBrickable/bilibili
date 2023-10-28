package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 角色表
 */
@Data
@NoArgsConstructor
public class AuthRole {
    private Long id;
    private String name;
    private String code;
    private Date createTime;
    private Date updateTime;
}
