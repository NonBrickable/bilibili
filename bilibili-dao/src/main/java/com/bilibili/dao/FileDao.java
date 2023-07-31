package com.bilibili.dao;

import com.bilibili.pojo.File;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FileDao {
    Integer addFile(File file);
    File getFileByMD5(String Md5);

}
