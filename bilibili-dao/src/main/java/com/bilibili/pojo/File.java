package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 文件表，存储上传的文件信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long id;
    private String url;
    private String type;
    private String md5;
    private Date createTime;
}
