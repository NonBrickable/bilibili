package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoments {
    private Long id;
    private Long userId;
    private String type;
    private Long contentId;
    private Date createTime;
    private Date updateTime;
}
