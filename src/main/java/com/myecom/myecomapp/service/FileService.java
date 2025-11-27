package com.myecom.myecomapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public String uploadFileS3(MultipartFile file);

    public boolean deleteFileS3(String fileUrl);
}
