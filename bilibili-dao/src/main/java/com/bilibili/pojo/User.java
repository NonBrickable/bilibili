package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String phone;
    private String email;
    private  String password;
    private  String salt;
    private Date createTime;
    private Date updateTime;

    private UserInfo userInfo;
}
