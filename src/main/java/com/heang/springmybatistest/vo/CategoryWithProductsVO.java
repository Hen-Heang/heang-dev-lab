package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.model.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CategoryWithProductsVO — 카테고리 + 상품 목록 VO (1:N JOIN 결과)
 *
 * This VO is NOT a table entity.
 * It holds the result of a JOIN query:
 *   category (1) ←→ product (N)
 *
 * MyBatis <collection> maps the flat JOIN rows into this nested structure:
 *
 * DB rows (flat):                    Java objects (nested):
 * cat_id | cat_name | prod_id       CategoryWithProductsVO
 *   1    | Electronics | 1    →       ├─ categoryId=1, categoryName="Electronics"
 *   1    | Electronics | 2    →       └─ products=[Product(1), Product(2), Product(3)]
 *   1    | Electronics | 3
 *   2    | Clothing    | 5    →     CategoryWithProductsVO
 *   2    | Clothing    | 6    →       ├─ categoryId=2, categoryName="Clothing"
 *                                     └─ products=[Product(5), Product(6)]
 *
 * ONE query → List<CategoryWithProductsVO>, each with List<Product> inside.
 * No N+1 problem (N+1 = 1 query for categories + N queries for each category's products).
 */
@Data
public class CategoryWithProductsVO {

    // Category fields (카테고리 필드)
    private Long          categoryId;
    private String        categoryName;
    private LocalDateTime categoryCreatedAt;

    // 1:N — list of products in this category (이 카테고리의 상품 목록)
    // MyBatis <collection> fills this list automatically
    private List<Product> products;

    // Convenience method — count products (상품 수 계산)
    public int getProductCount() {
        return (products == null) ? 0 : products.size();
    }

    // Total stock across all products in this category (카테고리 총 재고)
    public int getTotalStock() {
        if (products == null) return 0;
        return products.stream()
                .mapToInt(p -> p.getStock() == null ? 0 : p.getStock())
                .sum();
    }
}
