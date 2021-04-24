package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.springframework.stereotype.Service;

@Service
public class FileService {
    
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFileNameAvailable(String fileName, String username) {
        return fileMapper.getFile(fileName, username) == null;
    }

    public int uploadFile(File file) {
       return fileMapper.insertFile(new File(null, file.getFileName(), file.getContentType(), file.getFileSize(), file.getUserId(), file.getFileData()));
    }

    public File downloadFile(String fileName, String username) {
        return fileMapper.getFile(fileName, username);
    }

    public void deleteFile(String fileName, String username) {
        fileMapper.deleteFile(fileName, username);
    }

    public void getAllUserFile(String username) {

    }

}
