package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class FaqInVO extends CmmVO {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long   bbsSn;
    private String bbsKiCd;       // board kind code (게시판 구분)
    private String bbsTitle;
    private String bbsCn;
    private String useYn;

    // search
    private String searchCondition; // "1"=title, "2"=content, "3"=both
    private String searchKeyword;
    private String searchStartDt;
    private String searchEndDt;
}