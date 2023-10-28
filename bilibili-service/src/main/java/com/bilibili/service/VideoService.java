package com.bilibili.service;

import com.bilibili.common.PageResult;
import com.bilibili.dao.VideoDao;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.*;
import com.bilibili.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private FastDFSUtil fastDFSUtil;
    @Autowired
    private UserCoinService userCoinService;
    @Autowired
    private UserService userService;

    @Transactional
    public void addVideos(Video video) {
        videoDao.addVideos(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        for (VideoTag videoTag : videoTagList) {
            videoTag.setVideoId(videoId);
        }
        videoDao.batchAddVideoTags(videoTagList);
    }

    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if (size == null || no == null) {
            throw new ConditionException("参数异常");
        }
        //建立一个map，传参给dao，会更清晰一些
        Map<String, Object> params = new HashMap<>();
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        params.put("area", area);
        List<Video> list = new ArrayList<>();
        Integer total = videoDao.pageCountVideos(params);
        if (total > 0) {
            list = videoDao.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }

    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String path) throws Exception {
        fastDFSUtil.viewVideosOnlineBySlices(request, response, path);
    }

    public void addVideoLike(Long videoId, long userId) {
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("视频非法");
        }
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (videoLike != null) {
            throw new ConditionException("视频已经赞过");
        }
        videoDao.addVideoLike(videoId, userId);
    }

    public void deleteVideoLike(Long videoId, long userId) {
        videoDao.deleteVideoLike(videoId, userId);
    }

    public Map<String, Object> getVideoLikes(Long userId, Long videoId) {
        Long count = videoDao.getVideoLikes(videoId);
        VideoLike videoLike = videoDao.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = false;
        if (videoLike != null) {
            like = true;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("like", like);
        return result;
    }

    @Transactional
    public void addVideoCollection(VideoCollection videoCollection) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if (videoId == null || groupId == null) {
            throw new ConditionException("参数异常");
        }
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("视频非法");
        }
        videoDao.deleteVideoCollection(videoId, videoCollection.getUserId());
        videoDao.addVideoCollection(videoCollection);
    }

    public void deleteVideoCollection(Long videoId, Long userId) {
        videoDao.deleteVideoCollection(videoId, userId);
    }

    public Map<String, Object> getVideoCollections(Long videoId, Long userId) {
        Long count = videoDao.getVideoCollections(videoId);
        boolean collect = false;
        VideoCollection videoCollection = videoDao.getVideoCollectionByVideoIdAndUserId(videoId, userId);
        if (videoCollection != null) {
            collect = true;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("collect", collect);
        return result;
    }

    @Transactional
    //1.参数合法性：null以及存在   2.硬币数够不够
    public void addVideoCoins(VideoCoin videoCoin) {
        Long videoId = videoCoin.getVideoId();
        if (videoId == null) {
            throw new ConditionException("参数错误");
        }
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("视频不存在");
        }
        //获取用户有多少硬币
        Long userCoinsAmount = userCoinService.getUserCoinsAmount(videoCoin.getUserId());
        //判断是否为null
        userCoinsAmount = userCoinsAmount == null ? 0 : userCoinsAmount;
        Integer videoCoinAmount = videoCoin.getAmount();
        if (userCoinsAmount < videoCoinAmount) {
            throw new ConditionException("硬币数量不足");
        }
        //查询当前登录用户已经对该视频投了多少币
        VideoCoin dbvideoCoin = videoDao.getVideoCoinByVideoIdAndUserId(videoId, videoCoin.getUserId());
        //更改t_video_coin表
        if (dbvideoCoin == null) {
            videoDao.addVideoCoins(videoCoin);
        } else {
            Integer dbAmount = dbvideoCoin.getAmount();
            dbAmount += videoCoinAmount;
            videoCoin.setAmount(dbAmount);
            videoDao.updateVideoCoin(videoCoin);
        }
        //更改t_user_coin表
        userCoinService.updateUserCoinsAmount(videoCoin.getUserId(), (userCoinsAmount - videoCoinAmount));
    }

    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        Long amount = videoDao.getVideoCoins(videoId);
        boolean coin = false;
        VideoCoin videoCoin = videoDao.getVideoCoinByVideoIdAndUserId(videoId, userId);
        if (videoCoin != null) {
            coin = true;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("coin", coin);
        result.put("amount", amount);
        return result;
    }

    public void addVideoComment(VideoComment videoComment, Long userId) {
        Long videoId = videoComment.getVideoId();
        if (videoId == null) {
            throw new ConditionException("参数错误");
        }
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("视频不存在");
        }
        videoComment.setUserId(userId);
        videoDao.addVideoComment(videoComment);
    }

    public PageResult<VideoComment> pageListVideoComments(Integer size, Integer no, Long videoId) {
        Video video = videoDao.getVideoById(videoId);
        if (video == null) {
            throw new ConditionException("参数错误");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", size * (no - 1));
        params.put("limit", size);
        params.put("videoId", videoId);
        //查询主评论数量
        Integer total = videoDao.pageCountVideoComment(videoId);
        List<VideoComment> list = new ArrayList<>();
        if (total > 0) {
            list = videoDao.pageListVideoComments(params);
            //获取一级评论id的list
            List<Long> rootIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            //根据一级评论idList获取到所有的二级评论
            List<VideoComment> childVideoCommentList = videoDao.batchGetChildVideoComments(rootIdList);
            //抽取二级评论的userId，放到set里
            Set<Long> userIdList = childVideoCommentList.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            //抽取二级评论回复对象的userId，放到set里
            Set<Long> replyUserIdList = childVideoCommentList.stream().map(VideoComment::getReplyUserId).collect(Collectors.toSet());
            //合并userIdList
            userIdList.addAll(replyUserIdList);
            List<UserInfo> userInfoList = userService.batchGetUserInfo(userIdList);
            Map<Long, UserInfo> map = new HashMap<>();
            for (UserInfo ui : userInfoList) {
                map.put(ui.getUserId(), ui);
            }
            for (VideoComment rvc : list) {
                rvc.setUserInfo(map.get(rvc.getUserId()));
                List<VideoComment> childList = new ArrayList<>();
                for (VideoComment cvc : childVideoCommentList) {
                    if (cvc.getRootId() == rvc.getId()) {
                        cvc.setUserInfo(map.get(cvc.getUserId()));
                        cvc.setReplyUserInfo(map.get(cvc.getReplyUserId()));
                        rvc.getChildList().add(cvc);
                    }
                }
                rvc.setChildList(childList);
            }
        }
        return new PageResult<>(total, list);
    }

    /**
     * 获取视频详情
     * @param videoId
     * @return
     */
    public Map<String, Object> getVideoDetails(Long videoId) {
        Video video = videoDao.getVideoById(videoId);
        if(video == null){
            throw new ConditionException("参数错误");
        }
        Long userId = video.getUserId();
        User user = userService.getUserInfo(userId);
        UserInfo userInfo = user.getUserInfo();
        Map<String,Object> result = new HashMap<>();
        result.put("userInfo",userInfo);
        result.put("video",video);
        return result;
    }
}
