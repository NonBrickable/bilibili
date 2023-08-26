package com.bilibili.dao;

import com.bilibili.pojo.*;
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

    void addVideoLike(@Param("videoId") Long videoId, @Param("userId") long userId);

    void deleteVideoLike(@Param("videoId") Long videoId, @Param("userId") long userId);

    Long getVideoLikes(@Param("videoId") Long videoId);

    void deleteVideoCollection(@Param("videoId") Long videoId, @Param("userId") Long userId);

    void addVideoCollection(VideoCollection videoCollection);

    Long getVideoCollections(@Param("videoId") Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    VideoCoin getVideoCoinByVideoIdAndUserId(@Param("videoId") Long videoId, @Param("userId") Long userId);

    void addVideoCoins(VideoCoin videoCoin);

    void updateVideoCoin(VideoCoin videoCoin);

    Long getVideoCoins(@Param("videoId") Long videoId);

    void addVideoComment(VideoComment videoComment);

    Integer pageCountVideoComment(@Param("videoId") Long videoId);

    List<VideoComment> pageListVideoComments(Map<String, Object> params);

    List<VideoComment> batchGetChildVideoComments(List<Long> rootIdList);
}
