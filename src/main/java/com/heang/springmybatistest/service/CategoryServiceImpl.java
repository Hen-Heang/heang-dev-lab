package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.CategoryRequest;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.mapper.CategoryMapper;
import com.heang.springmybatistest.model.Category;
import com.heang.springmybatistest.vo.CategoryWithProductsVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service Implementation
 * Following a User service pattern (User 서비스 패턴 따름)
 */
@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        log.debug("Fetching all categories");
        List<Category> categories = categoryMapper.selectCategoryList();
        log.debug("Found {} categories", categories.size());
        return categories;
    }

    @Override
    public Category findById(Long id) {
        log.debug("Finding category by id: {}", id);
        Category category = categoryMapper.selectCategoryById(id);
        if (category == null) {
            log.warn("Category not found: id={}", id);
            throw new NotFoundException("Category not found with id: " + id);
        }
        log.debug("Category found: id={}, name={}", category.getId(), category.getName());
        return category;
    }

    @Override
    public Category create(CategoryRequest request) {
        log.info("Creating category: name={}", request.getName());
        categoryMapper.insertCategory(request);
        Category created = categoryMapper.selectCategoryByName(request.getName());
        log.info("Category created successfully: name={}", request.getName());
        return created;
    }

    @Override
    public Category update(Long id, CategoryRequest request) {
        log.info("Updating category: id={}", id);
        Category existing = categoryMapper.selectCategoryById(id);
        if (existing == null) {
            log.warn("Cannot update — category not found: id={}", id);
            throw new NotFoundException("Category not found with id: " + id);
        }
        categoryMapper.updateCategory(id, request);
        log.info("Category updated successfully: id={}, name={}", id, request.getName());
        return categoryMapper.selectCategoryById(id);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting category: id={}", id);
        Category existing = categoryMapper.selectCategoryById(id);
        if (existing == null) {
            log.warn("Cannot delete — category not found: id={}", id);
            throw new NotFoundException("Category not found with id: " + id);
        }
        categoryMapper.deleteCategory(id);
        log.info("Category deleted successfully: id={}, name={}", id, existing.getName());
    }

    @Override
    public int count() {
        return categoryMapper.countCategory();
    }

    @Override
    public List<CategoryWithProductsVO> findAllWithProducts() {
        log.debug("Fetching all categories with products");
        return categoryMapper.selectAllWithProducts();
    }
}
