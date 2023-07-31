package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;


    /**
     * 获取文件对应的md5字符串
     * @param file
     * @return
     */
    @PostMapping("/md5files")
    public JsonResponse<String> getFileMD5(MultipartFile file) throws Exception {
        String fileMD5 = fileService.getFileMD5(file);
        return new JsonResponse<>(fileMD5);
    }

    /**
     * 断点续传
     * @param file MultipartFile类型的文件
     * @param fileMd5 文件二进制流形成的Md5字符串
     * @param sliceNo 分片编号
     * @param totalSliceNo 分片总数
     * @return
     * @throws Exception
     */
    @PutMapping("/file-slices")
    public JsonResponse<String> uploadFileBySlices(MultipartFile file,String fileMd5,Integer sliceNo,Integer totalSliceNo) throws Exception {
        String filePath = fileService.uploadFileBySlices(file,fileMd5,sliceNo,totalSliceNo);
        return new JsonResponse<>(filePath);
    }

}
