package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.dto.ProductRequest;
import com.heang.springmybatistest.dto.ProductSearchRequest;
import com.heang.springmybatistest.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Product Mapper Interface
 * Following a User mapper pattern (User 매퍼 패턴 따름)
 */
@Mapper
public interface ProductMapper {

    void insertProduct(ProductRequest productRequest);

    List<Product> selectProductList();
    List<Product> searchProducts(ProductSearchRequest request);
    List<Product> searchProductsPaged(ProductSearchRequest request);
    int countProducts(ProductSearchRequest request);

    Product selectProductById(Long id);

    List<Product> selectProductByCategoryId(Long categoryId);

    void updateProduct(@Param("id") Long id,
                       @Param("req") ProductRequest productRequest);

    void deleteProduct(Long id);

    int countProduct();

}
