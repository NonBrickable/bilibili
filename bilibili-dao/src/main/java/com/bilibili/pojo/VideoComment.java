package com.bilibili.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class VideoComment {
    private Long id;
    private Long videoId;
    private Long userId;
    private String comment;
    private Long replyUserId;
    private Long rootId;
    private Date createTime;
    private Date updateTime;
    //子评论列表
    private List<VideoComment> childList;
    //发表评论的用户的基本信息

    //一二级评论都要
    private UserInfo userInfo;
    //回复评论的用户的基本信息，二级需要
    private UserInfo replyUserInfo;
}
