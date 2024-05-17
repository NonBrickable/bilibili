package com.playtube.pojo.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 菜单表
 */
@Data
@NoArgsConstructor
public class AuthMenu {
    private Long id;
    private String name;
    private String code;
    private Date createTime;
    private Date updateTime;
}
