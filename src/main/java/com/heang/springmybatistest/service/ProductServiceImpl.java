package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.ProductRequest;
import com.heang.springmybatistest.dto.ProductSearchRequest;
import com.heang.springmybatistest.mapper.ProductMapper;
import com.heang.springmybatistest.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Product Service Implementation
 * Following a User service pattern (User 서비스 패턴 따름)
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final FileUploadService fileUploadService;


    @Override
    public List<Product> search(ProductSearchRequest request) {
        return productMapper.searchProducts(request);
    }

    @Override
    public List<Product> searchPaged(ProductSearchRequest request) {
        request.getOffset();
        request.getLimit();
        return productMapper.searchProductsPaged(request);
    }

    @Override
    public int countSearch(ProductSearchRequest request) {
        return productMapper.countProducts(request);
    }

    @Override
    public Product findById(Long id) {
        Product product = productMapper.selectProductById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        return product;
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productMapper.selectProductByCategoryId(categoryId);
    }

    @Override
    public Product create(ProductRequest request) {
        // Save image file first, then store the returned URL into the request
        String imageUrl = fileUploadService.save(request.getImageFile());
        request.setImageUrl(imageUrl);

        productMapper.insertProduct(request);
        List<Product> products = productMapper.selectProductList();
        return products.isEmpty() ? null : products.getFirst();
    }

    @Override
    public Product update(Long id, ProductRequest request) {
        Product existing = productMapper.selectProductById(id);
        if (existing == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        // If a new image is uploaded, delete the old one and save the new one
        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            fileUploadService.delete(existing.getImageUrl());
            String imageUrl = fileUploadService.save(request.getImageFile());
            request.setImageUrl(imageUrl);
        } else {
            // No new image uploaded — keep the existing image URL
            request.setImageUrl(existing.getImageUrl());
        }

        productMapper.updateProduct(id, request);
        return productMapper.selectProductById(id);
    }

    @Override
    public void delete(Long id) {
        Product existing = productMapper.selectProductById(id);
        if (existing == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productMapper.deleteProduct(id);
    }

    @Override
    public int count() {
        return productMapper.countProduct();
    }
}
