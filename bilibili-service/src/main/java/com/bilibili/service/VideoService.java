package com.bilibili.service;

import com.bilibili.dao.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    @Autowired
    private VideoDao videoDao;
}
