package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Setter
@Getter
public class FaqOutVO extends CmmVO {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long   bbsSn;
    private String bbsKiCd;
    private String bbsKiCdNm;     // joined from co_comm_cd_d
    private String bbsTitle;
    private String bbsCn;
    private String useYn;
    private String dataRegId;
    private String dataRegDt;
    private String dataChgId;
    private String dataChgDt;
}