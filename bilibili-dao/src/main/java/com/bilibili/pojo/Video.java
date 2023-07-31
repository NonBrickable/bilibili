package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    private Long id;
    private Long userId;
    private String url;
    private String thumbnail;
    private String title;
    private String type;
    private String duration;
    private String area;
    private String description;
    private Date createTime;
    private Date updateTime;
}
