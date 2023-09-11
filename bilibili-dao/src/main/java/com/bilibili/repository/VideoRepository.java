package com.bilibili.repository;

import com.bilibili.pojo.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 用于和es仓库交互，通过继承的方式实现@mapper效果
 */
public interface VideoRepository extends ElasticsearchRepository<Video,Long> {

    Video findByTitleLike(String keyword);
}
