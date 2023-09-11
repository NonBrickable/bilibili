package com.bilibili.service;

import com.bilibili.pojo.Video;
import com.bilibili.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {
    @Autowired
    private VideoRepository videoRepository;
    public void addVideo(Video video){
        videoRepository.save(video);
    }
    public Video getVideos(String keyword){
        return videoRepository.findByTitleLike(keyword);
    }
    public void deleteAllVideos(){
        videoRepository.deleteAll();
    }
}
