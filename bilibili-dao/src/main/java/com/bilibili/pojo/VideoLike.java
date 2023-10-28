package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 视频点赞表
 */
@Data
@NoArgsConstructor
public class VideoLike {
    private Long id;
    private Long userId;
    private Long videoId;
    private Date createTime;
}
