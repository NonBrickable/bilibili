package com.bilibili.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Barrage {
    private Long id;
    private Long videoId;
    private Long userId;
    private String content;
    private String occurTime;
    private Date createTime;
}
