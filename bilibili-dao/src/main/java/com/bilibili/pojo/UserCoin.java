package com.bilibili.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class UserCoin {
    private Long id;
    private Long userId;
    private Long amount;
    private Date createTime;
    private Date updateTime;
}
