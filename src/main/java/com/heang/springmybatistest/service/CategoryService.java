package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.CategoryRequest;
import com.heang.springmybatistest.model.Category;
import com.heang.springmybatistest.vo.CategoryWithProductsVO;

import java.util.List;

/**
 * Category Service Interface
 * Following a User service pattern (User 서비스 패턴 따름)
 */
public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category create(CategoryRequest request);

    Category update(Long id, CategoryRequest request);

    void delete(Long id);

    int count();

    // 1:N JOIN — categories with their product lists (카테고리+상품 목록)
    List<CategoryWithProductsVO> findAllWithProducts();
}
