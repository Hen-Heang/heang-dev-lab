package com.heang.springmybatistest.controller.store;

import com.heang.springmybatistest.common.api.ApiResponse;
import com.heang.springmybatistest.dto.ProductRequest;
import com.heang.springmybatistest.dto.ProductSearchRequest;
import com.heang.springmybatistest.model.Product;
import com.heang.springmybatistest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product REST API Controller
 * Following a User controller pattern (User 컨트롤러 패턴 따름)
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> findAll(ProductSearchRequest request) {
        List<Product> products = productService.search(request);
        return ApiResponse.success(products);
    }

    @GetMapping("/paged")
    public ApiResponse<Map<String, Object>> searchPaged(ProductSearchRequest request) {
        int total = productService.countSearch(request);
        List<Product> products = productService.searchPaged(request);

        int page = request.getPage() == null || request.getPage() < 1 ? 1 : request.getPage();
        int size = request.getLimit();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);

        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", totalPages);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ApiResponse.success(product);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<Product>> findByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productService.findByCategoryId(categoryId);
        return ApiResponse.success(products);
    }

    // @ModelAttribute instead of @RequestBody because file upload uses multipart/form-data
    @PostMapping(consumes = {"multipart/form-data", "application/x-www-form-urlencoded", "application/json"})
    public ApiResponse<Product> create(@ModelAttribute ProductRequest request) {
        Product product = productService.create(request);
        return ApiResponse.success(product);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data", "application/x-www-form-urlencoded", "application/json"})
    public ApiResponse<Product> update(@PathVariable Long id, @ModelAttribute ProductRequest request) {
        Product product = productService.update(id, request);
        return ApiResponse.success(product);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.success(null);
    }
}
