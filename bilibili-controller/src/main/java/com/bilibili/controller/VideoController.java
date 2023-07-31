package com.bilibili.controller;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserSupport userSupport;
}
