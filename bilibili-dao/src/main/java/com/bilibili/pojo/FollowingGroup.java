package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
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
