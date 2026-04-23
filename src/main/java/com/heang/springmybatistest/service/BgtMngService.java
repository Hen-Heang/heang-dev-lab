package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngVO;

import java.util.Map;

/**
 * BgtMngService — Service interface for Budget Management (예산 관리 서비스 인터페이스)
 *
 * Why interface + implementation (Korean enterprise pattern)?
 *   - The interface defines WHAT the service does (contract)
 *   - BgtMngServiceImpl defines HOW it does it (logic)
 *   - This separation makes it easy to swap implementations (e.g., for testing)
 *   - Spring injects the implementation wherever this interface is used
 *
 * Layer responsibility:
 *   Controller → Service (this) → Mapper → DB
 *   Controller handles HTTP,  Service handles business logic,  Mapper handles SQL
 */
public interface BgtMngService {

    /**
     * Returns paginated budget list + total count (페이징 목록 조회)
     *
     * Returns Map with two keys:
     *   "list"       → List<BgtMngOutVO>  — the current page of rows
     *   "totalCount" → Integer            — total matching rows (used to render pagination)
     *
     * Why Map<String, Object> instead of a dedicated VO?
     *   Convenient for combining two different types (list + count) without creating a wrapper class.
     */
    Map<String, Object> selectBgtMngList(BgtMngInVO inVO);

    /**
     * Returns full detail of one budget record for the edit popup (상세 조회)
     */
    BgtMngVO selectBgtMngDetail(BgtMngInVO inVO);

    /**
     * Insert a new budget record (등록)
     */
    void insertBgtMng(BgtMngInVO inVO);

    /**
     * Update an existing budget record (수정)
     */
    void updateBgtMng(BgtMngInVO inVO);

    /**
     * Soft delete a budget record — sets del_yn = 'Y' (논리삭제)
     */
    void deleteBgtMng(BgtMngInVO inVO);
}
