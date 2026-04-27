package com.heang.springmybatistest.controller.store;

import com.heang.springmybatistest.common.api.ApiResponse;
import com.heang.springmybatistest.dto.CategoryRequest;
import com.heang.springmybatistest.model.Category;
import com.heang.springmybatistest.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category REST API Controller
 * Following a User controller pattern (User 컨트롤러 패턴 따름)
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Get all categories (전체 카테고리 조회)
     * GET /api/categories
     */
    @GetMapping
    public ApiResponse<List<Category>> findAll() {
        List<Category> categories = categoryService.findAll();
        return ApiResponse.success(categories);
    }

    /**
     * Get category by ID (ID로 카테고리 조회)
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ApiResponse.success(category);
    }

    /**
     * Create category (카테고리 등록)
     * POST /api/categories
     * Body: { "name": "Electronics" }
     */
    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.create(request);
        return ApiResponse.success(category);
    }

    /**
     * Update category (카테고리 수정)
     * PUT /api/categories/{id}
     * Body: { "name": "Home Appliances" }
     */
    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.update(id, request);
        return ApiResponse.success(category);
    }

    /**
     * Delete category (카테고리 삭제)
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success(null);
    }
}
