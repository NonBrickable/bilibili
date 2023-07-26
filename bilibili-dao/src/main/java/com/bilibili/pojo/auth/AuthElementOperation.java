package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 元素操作表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthElementOperation {
    private Long id;
    private String elementName;
    private String elementCode;
    private String operationType;
    private Date createTime;
    private Date updateTime;
}
