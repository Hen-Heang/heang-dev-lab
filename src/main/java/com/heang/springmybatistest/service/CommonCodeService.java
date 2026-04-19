package com.heang.springmybatistest.service;

import com.heang.springmybatistest.model.CommonCode;

import java.util.List;

/**
 * CommonCodeService — 공통코드 서비스 인터페이스
 */
public interface CommonCodeService {

    // Dropdown use — get active codes for a group (드롭다운용: 그룹별 활성 코드)
    List<CommonCode> findByGroup(String codeGroup);

    // Management page — all groups (관리 페이지: 그룹 목록)
    List<String> findAllGroups();

    // Management page — all codes, optional group filter (관리 페이지: 전체 코드)
    List<CommonCode> findAll(String codeGroup);

    // Register new code with duplicate check (중복 확인 후 등록)
    void insert(CommonCode code);

    // Update existing code (코드 수정)
    void update(CommonCode code);

    // Delete code (코드 삭제)
    void delete(String codeGroup, String codeValue);
}
