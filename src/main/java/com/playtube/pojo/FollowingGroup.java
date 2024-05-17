package com.playtube.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 用户关注分组表
 */
@Data
@NoArgsConstructor
public class FollowingGroup {
    private Long id;
    private Long userId;
    private String name;
    private String type;
    private Date createTime;
    private Date updateTime;
    private List<UserInfo> userInfoList;
}
