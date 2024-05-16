package com.bilibili.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class VideoTag {
    private Long id;
    private Long videoId;
    private Long tagId;
    private Date createTime;
}
