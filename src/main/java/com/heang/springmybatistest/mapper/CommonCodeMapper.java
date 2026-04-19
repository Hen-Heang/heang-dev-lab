package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.model.CommonCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CommonCodeMapper — 공통코드 매퍼
 *
 * Most used method in real Korean projects: findByGroup()
 * Called from every controller that renders a dropdown.
 */
@Mapper
public interface CommonCodeMapper {

    // Get all active codes for a group — used for dropdowns (그룹별 코드 조회)
    List<CommonCode> findByGroup(String codeGroup);

    // Get all distinct group names — used for management page filter (그룹 목록 조회)
    List<String> findAllGroups();

    // Get ALL codes (optionally filtered by group) — for management page (전체 코드 조회)
    List<CommonCode> findAll(@Param("codeGroup") String codeGroup);

    // INSERT new code (코드 등록)
    void insert(CommonCode code);

    // UPDATE code name and sort order (코드 수정)
    void update(CommonCode code);

    // DELETE by composite PK (코드 삭제)
    void delete(@Param("codeGroup") String codeGroup,
                @Param("codeValue") String codeValue);

    // Check if composite PK already exists — for duplicate validation (중복 확인)
    int countByGroupAndValue(@Param("codeGroup") String codeGroup,
                             @Param("codeValue") String codeValue);
}
