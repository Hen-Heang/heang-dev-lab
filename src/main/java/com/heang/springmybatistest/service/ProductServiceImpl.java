package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dto.ProductRequest;
import com.heang.springmybatistest.dto.ProductSearchRequest;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.mapper.ProductMapper;
import com.heang.springmybatistest.model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Product Service Implementation
 * Following a User service pattern (User 서비스 패턴 따름)
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final FileUploadService fileUploadService;


    @Override
    public List<Product> search(ProductSearchRequest request) {
        log.debug("Searching products with filter: keyword={}, categoryId={}",
                request.getKeyword(), request.getCategoryId());
        return productMapper.searchProducts(request);
    }

    @Override
    public List<Product> searchPaged(ProductSearchRequest request) {
        log.debug("Searching products paged: page={}, limit={}", request.getPage(), request.getLimit());
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
        log.debug("Finding product by id: {}", id);
        Product product = productMapper.selectProductById(id);
        if (product == null) {
            log.warn("Product not found: id={}", id);
            throw new NotFoundException("Product not found with id: " + id);
        }
        log.debug("Product found: id={}, name={}", product.getId(), product.getName());
        return product;
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        log.debug("Finding products by categoryId: {}", categoryId);
        return productMapper.selectProductByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public Product create(ProductRequest request) {
        log.info("Creating product: name={}, price={}, categoryId={}",
                request.getName(), request.getPrice(), request.getCategoryId());

        String imageUrl = fileUploadService.save(request.getImageFile());
        request.setImageUrl(imageUrl);

        productMapper.insertProduct(request);
        List<Product> products = productMapper.selectProductList();
        Product created = products.isEmpty() ? null : products.getFirst();

        log.info("Product created successfully: name={}", request.getName());
        return created;
    }

    @Override
    public Product update(Long id, ProductRequest request) {
        log.info("Updating product: id={}", id);
        Product existing = productMapper.selectProductById(id);
        if (existing == null) {
            log.warn("Cannot update — product not found: id={}", id);
            throw new NotFoundException("Product not found with id: " + id);
        }

        if (request.getImageFile() != null && !request.getImageFile().isEmpty()) {
            log.debug("New image uploaded, replacing old image for product id={}", id);
            fileUploadService.delete(existing.getImageUrl());
            String imageUrl = fileUploadService.save(request.getImageFile());
            request.setImageUrl(imageUrl);
        } else {
            log.debug("No new image uploaded, keeping existing image for product id={}", id);
            request.setImageUrl(existing.getImageUrl());
        }

        productMapper.updateProduct(id, request);
        log.info("Product updated successfully: id={}, name={}", id, request.getName());
        return productMapper.selectProductById(id);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting product: id={}", id);
        Product existing = productMapper.selectProductById(id);
        if (existing == null) {
            log.warn("Cannot delete — product not found: id={}", id);
            throw new NotFoundException("Product not found with id: " + id);
        }
        productMapper.deleteProduct(id);
        log.info("Product deleted successfully: id={}, name={}", id, existing.getName());
    }

    @Override
    public int count() {
        return productMapper.countProduct();
    }
}
