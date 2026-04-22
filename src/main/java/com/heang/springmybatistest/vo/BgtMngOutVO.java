package com.heang.springmybatistest.vo;

import lombok.Data;

@Data
public class BgtMngOutVO {
    private Long   id;
    private int    rn;
    private String jijacheNm;
    private String startMm;
    private String endMm;
    private Long   ntnlBgt;
    private Long   lclBgt;
    private Long   bgtSum;
}
