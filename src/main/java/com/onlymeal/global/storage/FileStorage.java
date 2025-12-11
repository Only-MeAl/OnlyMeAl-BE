package com.onlymeal.global.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    String store(MultipartFile file, Long userId, String mealType);
    void delete(String fileUrl);
}