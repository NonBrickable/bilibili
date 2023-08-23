package com.bilibili.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class VideoCoin {
    private Long id;
    private Long userId;
    private Long videoId;
    /**
     * 本次要投币的数量
     */
    private Integer amount;
    private Date createTime;
    private Date updateTime;
}
