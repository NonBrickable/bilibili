package com.bilibili.util;

import com.bilibili.exception.ConditionException;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * FastDFS工具类
 */
@Component
public class FastDFSUtil {
    // 为方便项目开发集成的简单接口
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    // 支持文件续传操作的接口
    @Autowired
    private AppendFileStorageClient appendFileStorageClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String DEFAULT_GROUP = "group1";
    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key";
    private static final String UPLOADED_NO_KEY = "uploaded-size-key";
    private static final String PATH_KEY = "path-key:";
    private static final int SLICE_SIZE = 1024 * 1024 * 2;

    //1.获取文件类型，为其它方法提供参数
    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new ConditionException("非法内容！");
        }
        String fileName = file.getOriginalFilename();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }

    //2.上传中小文件，没有断点续传功能
    public String uploadCommonFile(MultipartFile file) throws Exception {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    //3.删除文件
    public void deleteFile(String filePath) {
        fastFileStorageClient.deleteFile(filePath);
    }

    //4.上传可以断点续传的文件
    public String uploadAppenderFile(MultipartFile file) throws Exception {
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    //5.修改可以断点续传的文件
    public void modifyAppenderFile(MultipartFile file, String filePath, long offset) throws Exception {
        appendFileStorageClient.modifyFile(DEFAULT_GROUP, filePath, file.getInputStream(), file.getSize(), offset);
    }

    /**
     * 断点续传
     *
     * @param file         文件
     * @param fileMD5      文件经过MD5加密形成的唯一字符串
     * @param sliceNo      当前的分片编号，用于和totalSliceNo比较确定是不是应该返回path
     * @param totalSliceNo 总分片数
     * @return
     */
    public String uploadFileBySlices(MultipartFile file, String fileMD5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        if (file == null || sliceNo == null || totalSliceNo == null) {
            throw new ConditionException("参数异常");
        }
        //已经上传的分片的大小
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMD5;
        //路径信息
        String pathKey = PATH_KEY + fileMD5;

        //已经上传的分片的序号
        String uploadedNoKey = UPLOADED_NO_KEY + fileMD5;
        if (sliceNo == 1) {
            String path = this.uploadAppenderFile(file);
            if (StringUtil.isNullOrEmpty(path)) {
                throw new ConditionException("上传失败");
            }
            redisTemplate.opsForValue().set(pathKey, path);
            redisTemplate.opsForValue().set(uploadedNoKey, "1");
            redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(file.getSize()));
        } else {
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtil.isNullOrEmpty(filePath)) {
                throw new ConditionException("参数错误");
            } else {
                String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedNoKey);
                Long uploadedSize = Long.valueOf(uploadedSizeStr);
                this.modifyAppenderFile(file, filePath, uploadedSize);
                redisTemplate.opsForValue().increment(uploadedNoKey);
                uploadedSize += file.getSize();
                redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
            }
        }
        //判断全部上传，如果全部上传，则清空redis里所有的key和value
        String uploadedNoStr = redisTemplate.opsForValue().get(uploadedNoKey);
        String resultPath = "";
        if (Integer.valueOf(uploadedNoStr) == totalSliceNo) {
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

    /**
     * 文件分片
     *
     * @param multipartFile
     * @throws Exception
     */
    public void convertFileToSlices(MultipartFile multipartFile) throws Exception {
        String fileType = this.getFileType(multipartFile);
        //生成临时文件，将MultipartFile转为File
        File file = this.multipartFileToFile(multipartFile);
        long fileLength = file.length();
        int count = 1;
        // 开始切片
        for (int i = 0; i < fileLength; i += SLICE_SIZE) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(i);
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path = "D:/app-files/IdeaTest/" + count + "." + fileType;
            File slice = new File(path);
            FileOutputStream fos = new FileOutputStream(slice);
            fos.write(bytes, 0, len);
            fos.close();
            randomAccessFile.close();
            count++;
        }
        //删除临时文件
        file.delete();
    }

    public File multipartFileToFile(MultipartFile multipartFile) throws Exception {
        String originalFileName = multipartFile.getOriginalFilename();
        String[] fileName = originalFileName.split("\\.");
        File file = File.createTempFile(fileName[0], "." + fileName[1]);
        multipartFile.transferTo(file);
        return file;
    }

    @Value("${fdfs.http.storage-addr}")
    private String httpFdfsStorageAddr;
    public void viewVideosOnlineBySlices(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String path) throws Exception {
        //在文件服务器中查询文件信息（分组+相对路径）
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(DEFAULT_GROUP, path);
        long totalFileSize = fileInfo.getFileSize();
        String url = httpFdfsStorageAddr + path;
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,Object> headers = new HashMap<>();
        while(headerNames.hasMoreElements()){
            String header = headerNames.nextElement();
            headers.put(header,request.getHeader(header));
        }
        String rangeStr = request.getHeader("Range");
        String[] range;
        if(StringUtil.isNullOrEmpty(rangeStr)){
            rangeStr = "byte=0-" + (totalFileSize - 1);
        }
        range = rangeStr.split("byte=|-");
        Long begin = 0L;
        if(range.length >= 2){
            begin = Long.valueOf(range[1]);
        }
        Long end = 0L;
        if(range.length >= 3){
            end = Long.valueOf(range[2]);
        }
        long len = end - begin + 1;
        String contentRange = "bytes "+ begin + "-" + end + "/" + totalFileSize;
        response.setHeader("Content-Range",contentRange);
        response.setHeader("Accept-Ranges","bytes");
        response.setHeader("Content-Type","video/mp4");
        response.setContentLength((int)len);
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        HttpUtil.get(url,headers,response);
    }

}
