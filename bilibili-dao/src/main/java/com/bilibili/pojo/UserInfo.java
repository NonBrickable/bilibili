package com.bilibili.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 用户信息表
 */
@Data
@Document(indexName = "user-infos")
public class UserInfo {
    @Id
    private Long id;
    private Long userId;
    @Field(type = FieldType.Text)
    private String nick;
    private String avatar;
    private String sign;
    private String gender;
    private String birth;
    private Boolean followed;
    @Field(type = FieldType.Date)
    private Date createTime;
    @Field(type = FieldType.Date)
    private Date updateTime;
}
