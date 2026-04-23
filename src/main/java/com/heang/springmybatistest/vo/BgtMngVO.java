package com.heang.springmybatistest.vo;

import lombok.Data;

/**
 * BgtMngVO — Detail VO for Budget record (예산 상세 VO)
 *
 * Used only when fetching a single record for the edit popup (수정 팝업).
 * Contains the full set of editable fields from the budget_mng table.
 *
 * Flow:
 *   User clicks a grid row
 *   → JS sends POST /budget/selectBgtMngDetail.do { id: rowId }
 *   → Controller → Service → Mapper → DB
 *   → This VO is returned as JSON
 *   → JS fills each popup field: $('#pop_sigungu').val(d.sigunguCd).trigger('change')
 *
 * Difference from BgtMngOutVO:
 *   BgtMngOutVO  = list row   (only display fields, computed values like jijacheNm, bgtSum)
 *   BgtMngVO     = detail     (all raw editable fields: sidoCd, startYy, startMm, etc.)
 *
 * @Data (Lombok) → auto-generates getter, setter, toString, equals, hashCode
 */
@Data
public class BgtMngVO {
    private Long   id;          // PK

    // Region codes and names (지자체 코드/명)
    private String sidoCd;      // 시/도 code   e.g. "41"
    private String sidoNm;      // 시/도 name   e.g. "경기도"
    private String sigunguCd;   // 시/군 code   e.g. "41110"
    private String sigunguNm;   // 시/군 name   e.g. "수원시"
    private String guCd;        // 구 code
    private String guNm;        // 구 name

    // Budget period — stored as separate year/month columns in DB
    private String startYy;     // start year  e.g. "2026"
    private String startMm;     // start month e.g. "01"
    private String endYy;       // end year    e.g. "2026"
    private String endMm;       // end month   e.g. "12"

    // Budget amounts (unit: 원)
    private Long   ntnlBgt;     // 국비 (national budget)
    private Long   lclBgt;      // 지방비 (local/regional budget)

    /**
     * delYn — soft delete flag (논리삭제 여부)
     *   'N' = active record (normal)
     *   'Y' = deleted record (hidden from all queries)
     *
     * This field is rarely shown in the UI but is included here
     * in case the popup needs to display or check the deletion status.
     */
    private String delYn;
}
