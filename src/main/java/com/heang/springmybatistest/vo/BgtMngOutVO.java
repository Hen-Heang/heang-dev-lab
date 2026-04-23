package com.heang.springmybatistest.vo;

import lombok.Data;

/**
 * BgtMngOutVO — Output VO for Budget List (예산 목록 출력 VO)
 *
 * "OutVO" naming convention (Korean enterprise pattern):
 *   Used only for READ (SELECT) responses sent back to the client.
 *   Contains only the fields needed to display one row in the grid.
 *
 * This VO is mapped from SQL in BgtMngMapper.xml via resultMap "bgtMngOutResult":
 *   DB column (snake_case)  →  Java field (camelCase)
 *   rn                      →  rn          (ROW_NUMBER() from SQL)
 *   jijache_nm              →  jijacheNm   (computed: sido_nm + sigungu_nm)
 *   start_mm_full           →  startMm     (computed: start_yy || '-' || start_mm)
 *   end_mm_full             →  endMm
 *   ntnl_bgt                →  ntnlBgt
 *   lcl_bgt                 →  lclBgt
 *   ntnl_bgt + lcl_bgt      →  bgtSum      (computed in SQL)
 *
 * @Data (Lombok) → auto-generates getter, setter, toString, equals, hashCode
 */
@Data
public class BgtMngOutVO {
    private Long   id;          // PK — used in onclick="fn_openPopup(row.id)" in the grid
    private int    rn;          // 순번 — row number from SQL ROW_NUMBER() OVER (ORDER BY id)
    private String jijacheNm;   // 지자체명 — region display name (e.g. "경기도 수원시")
    private String startMm;     // 시작월 — format "YYYY-MM" (e.g. "2026-01")
    private String endMm;       // 종료월 — format "YYYY-MM" (e.g. "2026-12")
    private Long   ntnlBgt;     // 국비 (national budget)       unit: 원
    private Long   lclBgt;      // 지방비 (local/regional budget) unit: 원
    private Long   bgtSum;      // 예산합계 = ntnlBgt + lclBgt (computed in SQL)
}
