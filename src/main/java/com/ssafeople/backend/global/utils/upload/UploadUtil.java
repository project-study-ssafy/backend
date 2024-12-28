package com.ssafeople.backend.global.utils.upload;

import java.util.List;

public interface UploadUtil {
    String uploadImage(byte[] image, String dirName);
    void deleteImages(List<String> fileKeys);
}
