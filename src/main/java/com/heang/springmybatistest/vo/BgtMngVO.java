package com.heang.springmybatistest.vo;

import lombok.Data;

@Data
public class BgtMngVO {
    private Long   id;
    private String sidoCd;
    private String sidoNm;
    private String sigunguCd;
    private String sigunguNm;
    private String guCd;
    private String guNm;
    private String startYy;
    private String startMm;
    private String endYy;
    private String endMm;
    private Long   ntnlBgt;
    private Long   lclBgt;
    private String delYn;
}
