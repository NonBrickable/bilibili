package com.bilibili.dao;

import com.bilibili.pojo.Video;
import com.bilibili.pojo.VideoLike;
import com.bilibili.pojo.VideoTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoDao {
    Integer addVideos(Video video);
    Integer batchAddVideoTags(List<VideoTag> videoTagList);

    Integer pageCountVideos(Map<String, Object> params);
    List<Video> pageListVideos(Map<String, Object> params);

    Video getVideoById(Long videoId);

    VideoLike getVideoLikeByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") long userId);

    void addVideoLike(@Param("videoId") Long videoId, @Param("userId")long userId);

    void deleteVideoLike(@Param("videoId") Long videoId, @Param("userId") long userId);

    Long getVideoLikes(@Param("videoId") Long videoId);
}
