package com.bilibili.controller;

import com.bilibili.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestController {
    @Autowired
    private FastDFSUtil fastDFSUtil;


    @GetMapping("/slices")
    public void slices(MultipartFile file) throws Exception {
        fastDFSUtil.convertFileToSlices(file);
    }
}
