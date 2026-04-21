package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyInVO extends CmmVO {

    // Search conditions (검색 조건)
    private String companyName;
    private String status;
    private String dateFrom;
    private String dateTo;

    // Detail lookup / status update
    private Long id;

    // Registration fields (신규등록 입력값)
    private String ceoName;
    private String businessNo;
    private String phone;
    private String address;
    private String addressDetail;
    private String applyChannel;
    private String paymentType;
    private String paymentProvider;
    private int    workerCount;
    private String contactName;
    private String contactPhone;
}
