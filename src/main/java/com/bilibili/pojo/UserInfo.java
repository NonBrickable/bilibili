package com.bilibili.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private Long userId;
    private String nick;
    private String avatar;
    private String sign;
    private String gender;
    private String birth;
    private Boolean followed;
    private Date createTime;
    private Date updateTime;
}
