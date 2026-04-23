package com.heang.springmybatistest.vo;

import lombok.Data;

/**
 * BgtMngInVO — Input VO for Budget Management (예산 관리 입력 VO)
 *
 * "InVO" naming convention (Korean enterprise pattern):
 *   In  = data coming IN  from the client (request)
 *   Out = data going  OUT to  the client (response) → see BgtMngOutVO
 *
 * One VO covers three use cases:
 *   1) Search filters  → sent by the search form AJAX call
 *   2) Save fields     → sent by the popup form when saving (INSERT / UPDATE)
 *   3) Delete field    → only 'id' is needed for soft delete
 *
 * @Data (Lombok) → auto-generates: getter, setter, toString, equals, hashCode
 * Spring binds JSON request body → this VO via @RequestBody in the controller
 */
@Data
public class BgtMngInVO {

    // ── Search filters (검색 조건) ─────────────────────────────────────
    // These fields are used only in selectBgtMngList — ignored in INSERT/UPDATE

    private String sidoCd;      // 시/도 code   e.g. "41" (경기도)
    private String sigunguCd;   // 시/군 code   e.g. "41110" (수원시)
    private String guCd;        // 구   code
    private String emdCd;       // 읍면동 code

    private String srchStrtMm;  // search start month  format: "YYYY-MM"
    private String srchEndMm;   // search end   month  format: "YYYY-MM"

    /**
     * wholPrdYn — 전체기간 (all period) flag
     *   "Y"  = ignore date range filter → return all records regardless of date
     *   null = apply date range filter  → use srchStrtMm / srchEndMm
     *
     * IMPORTANT: must be null (not empty string "") when unchecked.
     * MyBatis XML uses: <if test="wholPrdYn == null">
     * If you send "" instead of null, the condition fails and date filter is skipped wrongly.
     */
    private String wholPrdYn;

    // ── Save / Update / Delete fields (저장·수정·삭제용) ──────────────
    // These fields are used in INSERT, UPDATE, and soft DELETE

    private Long   id;          // PK — null for INSERT, required for UPDATE / DELETE

    private String sidoNm;      // 시/도 name   e.g. "경기도"
    private String sigunguNm;   // 시/군 name   e.g. "수원시"
    private String guNm;        // 구   name
    private String guCd2;       // (reserved)

    private String startYy;     // budget start year   e.g. "2026"
    private String startMm;     // budget start month  e.g. "01"
    private String endYy;       // budget end   year   e.g. "2026"
    private String endMm;       // budget end   month  e.g. "12"

    private Long   ntnlBgt;     // 국비 (national budget)  unit: 원
    private Long   lclBgt;      // 지방비 (local budget)    unit: 원

    // ── Pagination (페이징) ───────────────────────────────────────────
    // Default values: page 1, 10 rows per page

    private int pageNo   = 1;   // current page number (1-based)
    private int pageSize = 10;  // number of rows per page

    /**
     * getOffset() — calculates the SQL OFFSET for pagination
     *
     * Formula: (pageNo - 1) * pageSize
     *   page 1 → offset 0   (rows 1–10)
     *   page 2 → offset 10  (rows 11–20)
     *   page 3 → offset 20  (rows 21–30)
     *
     * Used in MyBatis XML: LIMIT #{pageSize} OFFSET #{offset}
     * MyBatis calls getOffset() automatically because the property name is "offset"
     */
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
