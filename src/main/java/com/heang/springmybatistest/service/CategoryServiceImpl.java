package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.CategoryRequest;
import com.heang.springmybatistest.mapper.CategoryMapper;
import com.heang.springmybatistest.model.Category;
import com.heang.springmybatistest.vo.CategoryWithProductsVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service Implementation
 * Following a User service pattern (User 서비스 패턴 따름)
 */
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectCategoryList();
    }

    @Override
    public Category findById(Long id) {
        Category category = categoryMapper.selectCategoryById(id);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        return category;
    }

    @Override
    public Category create(CategoryRequest request) {
        categoryMapper.insertCategory(request);
        return categoryMapper.selectCategoryByName(request.getName());
    }

    @Override
    public Category update(Long id, CategoryRequest request) {
        Category existing = categoryMapper.selectCategoryById(id);
        if (existing == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryMapper.updateCategory(id, request);
        return categoryMapper.selectCategoryById(id);
    }

    @Override
    public void delete(Long id) {
        Category existing = categoryMapper.selectCategoryById(id);
        if (existing == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryMapper.deleteCategory(id);
    }

    @Override
    public int count() {
        return categoryMapper.countCategory();
    }

    @Override
    public List<CategoryWithProductsVO> findAllWithProducts() {
        return categoryMapper.selectAllWithProducts();
    }
}
