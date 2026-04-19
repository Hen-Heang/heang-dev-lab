package com.heang.springmybatistest.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * CommonCode — 공통코드 엔티티 (Common Code Entity)
 *
 * Korean enterprise pattern:
 *   Every system has a common_code table instead of hard-coded enums in Java.
 *   Admin users can add/change labels in DB without touching code.
 *
 * Example rows:
 *   code_group='USER_STATUS', code_value='ACTIVE',   code_name='활성'
 *   code_group='USER_STATUS', code_value='INACTIVE',  code_name='비활성'
 *   code_group='BOARD_TYPE',  code_value='01',        code_name='공지사항'
 */
@Data
public class CommonCode {
    private String        codeGroup;   // group key  e.g. "USER_STATUS"
    private String        codeValue;   // code value e.g. "ACTIVE"
    private String        codeName;    // display label e.g. "활성"
    private int           sortOrder;   // order in dropdown
    private String        useYn;       // 'Y' = active, 'N' = hidden
    private LocalDateTime createdAt;
}
