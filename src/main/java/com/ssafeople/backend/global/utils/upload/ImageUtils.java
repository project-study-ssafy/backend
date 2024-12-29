package com.ssafeople.backend.global.utils.upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUtils {
    String uploadImage(MultipartFile image, String dirName);
    void deleteImages(List<String> fileKeys);
}
