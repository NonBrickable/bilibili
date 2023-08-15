package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.common.PageResult;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.Video;
import com.bilibili.service.VideoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserSupport userSupport;

    /**
     * 视频投稿
     * @param video
     * @return
     */
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video){
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideos(video);
        return JsonResponse.success();
    }

    /**
     * 瀑布流分页查询
     * @param size 本页的视频数量
     * @param no   本页的编号
     * @param area 分区类型
     * @return
     */
    @GetMapping("/videos/page")
    public JsonResponse<PageResult<Video>> pageListVideos(@RequestParam Integer size, @RequestParam Integer no, String area){
        PageResult<Video> result = videoService.pageListVideos(size,no,area);
        return new JsonResponse<>(result);
    }

    @GetMapping("/video-slices")
    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String path) throws Exception {
        videoService.viewVideosOnlineBySlices(request,response,path);
    }

    /**
     * 点赞视频
     * @param videoId
     * @return
     */
    @PostMapping("/video-like")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId){
        long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId,userId);
        return JsonResponse.success();
    }

    /**
     * 取消点赞视频
     * @param videoId
     * @return
     */
    @DeleteMapping("/video-like-del")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId){
        long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId,userId);
        return JsonResponse.success();
    }

    /**
     * 查询视频点赞数量
     * @param videoId
     * @return
     */
    @GetMapping("/video-like-count")
    public JsonResponse<Map<String,Object>> getVideoLikes(@RequestParam Long videoId){
        Long userId = null;
        try{
            userId = userSupport.getCurrentUserId();
        }catch(Exception e){}
        Map<String,Object> result = videoService.getVideoLikes(userId,videoId);
        return new JsonResponse<>(result);
    }
    @PostMapping("/video-collection")
    public JsonResponse<String> addVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCollection(videoId,userId);
        return JsonResponse.success();
    }
}
