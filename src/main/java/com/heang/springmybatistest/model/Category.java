package com.heang.springmybatistest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Category Entity (카테고리 엔티티)
 * Maps 1:1 to category table (테이블과 1:1 매핑)
 * <p>
 * Table: category
 * - id: Primary Key, auto-increment (기본키, 자동 증가)
 * - name: Category name, required (카테고리명, 필수)
 * - created_at: Created timestamp, auto (생성일시, 자동)
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private Long id;            // PK
    private String name;        // Category name (카테고리명)
    private LocalDateTime createdAt;  // Created timestamp (DB: created_at → Java: createdAt)
}
