package com.bilibili.service;

import com.bilibili.common.PageResult;
import com.bilibili.dao.VideoDao;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.Video;
import com.bilibili.pojo.VideoLike;
import com.bilibili.pojo.VideoTag;
import com.bilibili.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Transactional
    public void addVideos(Video video) {
        videoDao.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        for(VideoTag videoTag:videoTagList){
            videoTag.setVideoId(videoId);
        }
        videoDao.batchAddVideoTags(videoTagList);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if(size == null || no == null){
            throw new ConditionException("参数异常");
        }
        //建立一个map，传参给dao，会更清晰一些
        Map<String,Object> params = new HashMap<>();
        params.put("start",(no-1)*size);
        params.put("limit",size);
        params.put("area",area);
        List<Video> list = new ArrayList<>();
        Integer total = videoDao.pageCountVideos(params);
        if(total > 0){
            list = videoDao.pageListVideos(params);
        }
        return new PageResult<Video>(total,list);
    }

    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String path) throws Exception {
        fastDFSUtil.viewVideosOnlineBySlices(request,response,path);
    }

    public void addVideoLike(Long videoId, long userId) {
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("视频非法");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId,userId);
        if(videoLike != null){
            throw new ConditionException("视频已经赞过");
        }
        videoDao.addVideoLike(videoId,userId);
    }

    public void deleteVideoLike(Long videoId, long userId) {
        videoDao.deleteVideoLike(videoId,userId);
    }

    public Map<String, Object> getVideoLikes(Long userId, Long videoId) {
        Long count = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId,userId);
        boolean like = false;
        if(videoLike != null){
            like = true;
        }
        Map<String,Object> result = new HashMap<>();
        result.put("count",count);
        result.put("like",like);
        return result;
    }

    public void addVideoCollection(Long videoId, Long userId) {
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("视频非法");
        }
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserId(videoId,userId);
    }
}
