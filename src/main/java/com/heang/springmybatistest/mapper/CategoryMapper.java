package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.dto.CategoryRequest;
import com.heang.springmybatistest.model.Category;
import com.heang.springmybatistest.vo.CategoryWithProductsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Category Mapper Interface
 * Following a User mapper pattern (User 매퍼 패턴 따름)
 */
@Mapper
public interface CategoryMapper {

    void insertCategory(CategoryRequest categoryRequest);

    List<Category> selectCategoryList();

    Category selectCategoryById(Long id);

    Category selectCategoryByName(String name);

    void updateCategory(@Param("id") Long id,
                        @Param("req") CategoryRequest categoryRequest);

    void deleteCategory(Long id);

    int countCategory();

    /**
     * SELECT all categories with their products using LEFT JOIN (카테고리+상품 JOIN 조회)
     *
     * Returns flat rows → MyBatis <collection> groups them into
     * List<CategoryWithProductsVO>, each with List<Product> inside.
     */
    List<CategoryWithProductsVO> selectAllWithProducts();
}
