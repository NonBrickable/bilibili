package com.bilibili.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 操作表
 */
@Data
@NoArgsConstructor
public class AuthOperation {
    private Long id;
    private String elementName;
    private String elementCode;
    private String operationType;
    private Date createTime;
    private Date updateTime;
}
