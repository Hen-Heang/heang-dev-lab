package com.heang.springmybatistest.vo;

import lombok.Data;

@Data
public class BgtMngInVO {
    // search filters (검색 조건)
    private String sidoCd;
    private String sigunguCd;
    private String guCd;
    private String emdCd;
    private String srchStrtMm;
    private String srchEndMm;
    private String wholPrdYn;

    // save / update / delete fields (저장·수정·삭제용)
    private Long   id;
    private String sidoNm;
    private String sigunguNm;
    private String guNm;
    private String startYy;
    private String startMm;
    private String endYy;
    private String endMm;
    private Long   ntnlBgt;
    private Long   lclBgt;
}
