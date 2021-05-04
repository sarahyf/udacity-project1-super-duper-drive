package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

import org.springframework.stereotype.Service;

@Service
public class FileService {
    
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFileNameAvailable(String fileName, Integer userId) {
        return fileMapper.getFile(fileName, userId) == null;
    }

    public int uploadFile(File file) {
      return fileMapper.insertFile(file);
    }

    public File getFile(File file) {
        return fileMapper.getFileById(file);
    }

    public void deleteFile(File file) {
        fileMapper.deleteFile(file);
    }

    public List<File> getAllUserFile(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

}
