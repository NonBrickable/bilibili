package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.common.PageResult;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.Video;
import com.bilibili.pojo.VideoCoin;
import com.bilibili.pojo.VideoCollection;
import com.bilibili.pojo.VideoComment;
import com.bilibili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserSupport userSupport;

    /**
     * 视频投稿
     *
     * @param video
     * @return
     */
    @PostMapping("/videos")
    public JsonResponse<String> addVideos(@RequestBody Video video) {
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideos(video);
        return JsonResponse.success();
    }

    /**
     * 瀑布流分页查询
     *
     * @param size 本页的视频数量
     * @param no   本页的编号
     * @param area 分区类型
     * @return
     */
    @GetMapping("/videos/page")
    public JsonResponse<PageResult<Video>> pageListVideos(@RequestParam Integer size, @RequestParam Integer no, String area) {
        PageResult<Video> result = videoService.pageListVideos(size, no, area);
        return new JsonResponse<>(result);
    }

    @GetMapping("/video-slices")
    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String path) throws Exception {
        videoService.viewVideosOnlineBySlices(request, response, path);
    }

    /**
     * 点赞视频
     *
     * @param videoId
     * @return
     */
    @PostMapping("/video-like")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId) {
        long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 取消点赞视频
     *
     * @param videoId
     * @return
     */
    @DeleteMapping("/video-like-del")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId) {
        long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 查询视频点赞数量
     *
     * @param videoId
     * @return
     */
    @GetMapping("/video-like-count")
    public JsonResponse<Map<String, Object>> getVideoLikes(@RequestParam Long videoId) {
        Long userId = -1L;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }
        Map<String, Object> result = videoService.getVideoLikes(userId, videoId);
        return new JsonResponse<>(result);
    }

    /**
     * 收藏视频
     *
     * @param videoCollection
     * @return
     */
    @PostMapping("/video-collection")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection) {
        Long userId = userSupport.getCurrentUserId();
        videoCollection.setUserId(userId);
        videoService.addVideoCollection(videoCollection);
        return JsonResponse.success();
    }

    /**
     * 取消收藏视频
     *
     * @param videoId
     * @return
     */
    @DeleteMapping("/video-collection-del")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId, userId);
        return JsonResponse.success();
    }

    /**
     * 查看视频收藏数量
     *
     * @param videoId
     * @return
     */
    @GetMapping("/video-collection-count")
    public JsonResponse<Map<String, Object>> getVideoCollections(@RequestParam Long videoId) {
        Long userId = -1L;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }
        Map<String, Object> result = videoService.getVideoCollections(videoId, userId);
        return new JsonResponse<>(result);
    }

    /**
     * 视频投币
     *
     * @param videoCoin
     * @return
     */
    @PostMapping("/video-coins")
    public JsonResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin) {
        Long userId = userSupport.getCurrentUserId();
        videoCoin.setUserId(userId);
        videoService.addVideoCoins(videoCoin);
        return JsonResponse.success();
    }

    /**
     * 查看视频投币数量
     *
     * @param videoId
     * @return
     */
    @GetMapping("/video-coins-count")
    public JsonResponse<Map<String, Object>> getVideoCoins(@RequestParam Long videoId) {
        Long userId = -1L;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception e) {
        }
        Map<String, Object> result = videoService.getVideoCoins(videoId, userId);
        return new JsonResponse<>(result);
    }

    /**
     * 添加视频评论
     *
     * @param videoComment
     * @return
     */
    @PostMapping("/video-comment")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment) {
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoComment(videoComment, userId);
        return JsonResponse.success();
    }

    /**
     * 分页查看评论
     * @param size
     * @param no
     * @param videoId
     * @return
     */
    @GetMapping("/video-comments-page")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer size,
                                                                        @RequestParam Integer no,
                                                                        @RequestParam Long videoId) {
        PageResult<VideoComment> result = videoService.pageListVideoComments(size, no, videoId);
        return new JsonResponse<>(result);
    }

    /**
     * 视频详情
     * @param videoId
     * @return
     */
    @GetMapping("/video-details")
    public JsonResponse<Map<String,Object>> getVideoDetails(@RequestParam Long videoId){
        Map<String,Object> result = videoService.getVideoDetails(videoId);
        return new JsonResponse<>(result);
    }
}
