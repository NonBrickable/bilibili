package com.bilibili.util;

import com.bilibili.exception.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FastDFS工具类
 */
@Component
public class FastDFSUtil {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private AppendFileStorageClient appendFileStorageClient;

    private static final String DEFAULT_GROUP = "group1";
    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key";
    private static final String UPLOADED_NO_KEY = "uploaded-size-key";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String PATH_KEY = "path-key:";


    //获取文件类型
    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new ConditionException("非法内容！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1, fileName.length());
    }

    //上传
    public String uploadCommonFile(MultipartFile file) throws Exception {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    //上传可以断点续传的为文件
    public String uploadAppenderFile(MultipartFile file) throws Exception {
        String fileName = file.getName();
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    /**
     * 断点续传
     *
     * @param file         文件
     * @param fileMD5      文件经过MD5加密形成的唯一字符串
     * @param sliceNo      当前的分片编号
     * @param totalSliceNo 总分片数
     * @return
     */
    public String uploadFileBySlices(MultipartFile file, String fileMD5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        if (file == null || sliceNo == null || totalSliceNo == null) {
            throw new ConditionException("参数异常");
        }
        //路径信息
        String pathKey = PATH_KEY + fileMD5;

        //已经上传的分片的大小
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMD5;

        //已经上传的分片的序号
        String uploadedNoKey = UPLOADED_NO_KEY + fileMD5;

        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if (!StringUtil.isNullOrEmpty(uploadedSizeStr)) {
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        String fileType = this.getFileType(file);
        if (sliceNo == 1) {
            String path = this.uploadAppenderFile(file);
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNoKey, "1");
        } else {
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new ConditionException("参数错误");
            } else {
                this.modifyAppenderFile(file, filePath, uploadedSize);
                redisTemplate.opsForValue().increment(uploadedNoKey);
            }
        }
        //修改历史上传文件大小
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        //判断全部上传，如果全部上传，则清空redis里所有的key和value
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        String resultPath = "";
        if(Integer.valueOf(uploadedNoStr) == totalSliceNo){
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> list = new ArrayList<>();
            list.add(pathKey);
            list.add(uploadedSizeKey);
            list.add(uploadedNoKey);
            redisTemplate.delete(list);
            return resultPath;
        }
        return resultPath;
    }


    //删除文件
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }
}
