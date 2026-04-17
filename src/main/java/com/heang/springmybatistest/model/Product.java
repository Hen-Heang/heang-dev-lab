package com.heang.springmybatistest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Product Entity (상품 엔티티)
 * Maps 1:1 to product table (테이블과 1:1 매핑)
 * <p>
 * Table: product
 * - id: Primary Key, auto-increment (기본키, 자동 증가)
 * - name: Product name, required (상품명, 필수)
 * - price: Price, required, default 0 (가격, 필수)
 * - stock: Stock quantity, required, default 0 (재고, 필수)
 * - category_id: Category FK, required (카테고리 FK, 필수)
 * - created_at: Created timestamp, auto (생성일시, 자동)
 * <p>
 * Relationship: Product (N): Category (1) (관계)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Long id;            // PK
    private String name;        // Product name (상품명)
    private Integer price;      // Price (가격)
    private Integer stock;      // Stock quantity (재고)
    private Long categoryId;    // FK (DB: category_id → Java: categoryId)
    private LocalDateTime createdAt;  // Created timestamp (생성일시)

    // Field for JOIN result (Not a table column!) - JOIN 결과용 필드 (테이블 컬럼 아님!)
    private String categoryName;  // category.name (LEFT JOIN result)
    private String imageUrl;

}
