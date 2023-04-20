package com.example.doantotnghiep.service;

import com.example.doantotnghiep.entity.Image;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ImageService {
    void saveImage(Image image);
    void deleteImage(String uploadDir,String filename);
    List<String> getListImageOfUser(long userId);
    UrlResource downloadFile1(String filename);
    String uploadFile1( MultipartFile file);
}
