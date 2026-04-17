package com.heang.springmybatistest.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

    // Save file to disk, return the URL path e.g. "/uploads/abc123.jpg"
    String save(MultipartFile file);

    // Delete old file from disk when product image is replaced
    void delete(String imageUrl);
}
