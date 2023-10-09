package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;


    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFileAvailable(String fileName, Integer userId) {
        File file = fileMapper.getFile(fileName, userId);
        return file != null;
    }

    public void addFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File file = new File();
        file.setContentType(fileUpload.getContentType());
        file.setFileData(fileUpload.getBytes());
        file.setFileName(fileUpload.getOriginalFilename());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setUserId(userId);

        fileMapper.storeFile(file);
    }


    public List<File> getAllFiles(Integer userId) {
        return fileMapper.getAllFiles(userId);
    }

    public void deleteFile(Integer fileId, Integer userId) {
        fileMapper.deleteFile(fileId, userId);
    }

    public File getFileById(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }
}
