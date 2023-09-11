package com.bilibili.pojo;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
@Document(indexName = "videos")
@Data
public class Video {
    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 用户id
     */
    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 视频链接
     */
    private String url;

    /**
     * 封面链接
     */
    private String thumbnail;

    /**
     * 视频标题
     */
    @Field(type = FieldType.Text)
    private String title;

    /**
     * 视频类型 0原创 1转载
     */
    private String type;

    /**
     * 视频时长
     */
    private String duration;

    /**
     * 视频分区 0鬼畜 1音乐 2怀旧
     */
    private String area;

    /**
     * 视频简介
     */
    @Field(type = FieldType.Text)
    private String description;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date)
    private Date updateTime;
    private List<VideoTag> videoTagList;//冗余，存放标签类型
}
