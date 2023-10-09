package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userId = #{userId} AND filename = #{fileName}")
    File getFile(String fileName, Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void storeFile(File file);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userid=#{userId}")
    int deleteFile(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileById(Integer fileId);
}
