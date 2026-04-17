package com.heang.springmybatistest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    // Folder inside static/ where uploaded images are saved
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @Override
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            // Create uploads folder if it doesn't exist yet
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename to avoid overwriting e.g. "abc123.jpg"
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String newFilename = UUID.randomUUID().toString() + extension;

            // Save file to disk
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            // Return URL path used in <img src="...">
            return "/uploads/" + newFilename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file: " + e.getMessage());
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) return;

        try {
            // Convert URL "/uploads/abc.jpg" → file path "src/main/resources/static/uploads/abc.jpg"
            String filename = imageUrl.replace("/uploads/", "");
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file: " + e.getMessage());
        }
    }
}
