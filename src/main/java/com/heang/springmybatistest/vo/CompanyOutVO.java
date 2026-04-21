package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyOutVO extends CmmVO {

    // Core fields shown in the list grid (Page 18 of UI doc)
    private Long   id;
    private String companyName;      // 기업명
    private String ceoName;          // 대표자명
    private String businessNo;       // 사업자등록번호
    private String phone;            // 전화번호
    private String address;          // 주소
    private String addressDetail;    // 상세주소
    private String paymentType;      // 식권/카드사
    private String paymentProvider;  // 결제사명
    private String applyChannel;     // 신청매체 (ONLINE/OFFLINE)
    private String status;           // 신청상태 (신청/승인/반려)
    private String contactName;      // 담당자명
    private String contactPhone;     // 담당자 연락처
    private int    workerCount;      // 참여신청 근로자수
    private String createdAt;        // 신청일자
    private String updatedAt;        // 접수일 (승인일)
}
