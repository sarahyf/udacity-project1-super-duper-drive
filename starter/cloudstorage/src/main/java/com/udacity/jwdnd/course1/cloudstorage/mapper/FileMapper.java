package com.udacity.jwdnd.course1.cloudstorage.mapper;

import java.io.InputStream;
import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFile(String fileName, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFiles(Integer userId);

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    File getFileById(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId}")
    void deleteFile(File file);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    List<String> getFilesName(Integer userId);

}
