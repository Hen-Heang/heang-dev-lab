package com.heang.springmybatistest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Product 생성/수정 요청 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private Integer price;
    private Integer stock;
    private Long categoryId;
    private MultipartFile imageFile;  // incoming file from the form
    private String imageUrl;          // saved URL path set by FileUploadService
}
