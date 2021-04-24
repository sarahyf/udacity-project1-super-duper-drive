package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName}" +
            "AND userId = (SELECT userId FROM USERS WHERE username = #{userName})")
    File getFile(String fileName, String userName);

    /*@Select("SELECT * FROM FILES WHERE userId = (SELECT userId FROM USERS WHERE username = #{userName})")
    File getAllFile(String userName);*/

    @Insert("INSERT INTO FILES(filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}"
            + "AND userId = (SELECT userId FROM USERS WHERE username = #{userName})")
    void deleteFile(String fileName, String userName);

}
