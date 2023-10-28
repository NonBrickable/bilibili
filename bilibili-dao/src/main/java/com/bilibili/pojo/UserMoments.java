package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户动态表
 */
@Data
@NoArgsConstructor
public class UserMoments {
    private Long id;
    private Long userId;
    private String type;
    private Long contentId;
    private Date createTime;
    private Date updateTime;
}
