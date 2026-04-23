package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.BgtMngMapper;
import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * BgtMngServiceImpl — Service implementation for Budget Management (예산 관리 서비스 구현체)
 *
 * @Service       → marks this class as a Spring service bean (Spring manages its lifecycle)
 * @RequiredArgsConstructor (Lombok) → generates a constructor that injects all 'final' fields
 *   equivalent to writing:
 *     public BgtMngServiceImpl(BgtMngMapper bgtMngMapper) {
 *         this.bgtMngMapper = bgtMngMapper;
 *     }
 *
 * implements BgtMngService → must provide a body for every method declared in the interface
 */
@Service
@RequiredArgsConstructor
public class BgtMngServiceImpl implements BgtMngService {

    /**
     * Spring injects BgtMngMapper automatically (via @RequiredArgsConstructor).
     * 'final' means this field is set once in the constructor and never changed.
     */
    private final BgtMngMapper bgtMngMapper;

    /**
     * Fetch paginated list + total count (목록 + 건수 조회)
     *
     * Calls TWO mapper methods with the same inVO:
     *   1) selectBgtMngList      → returns the current page rows (LIMIT/OFFSET applied in SQL)
     *   2) selectBgtMngListCount → returns the total matching row count (no LIMIT)
     *
     * Both use the same WHERE conditions (same search filters in inVO).
     * Result is packed into a Map so the controller can return both in one JSON response.
     */
    @Override
    public Map<String, Object> selectBgtMngList(BgtMngInVO inVO) {
        Map<String, Object> result = new HashMap<>();
        result.put("list",       bgtMngMapper.selectBgtMngList(inVO));
        result.put("totalCount", bgtMngMapper.selectBgtMngListCount(inVO));
        return result;
    }

    /**
     * Fetch one record's full detail (상세 조회)
     * Delegates directly to the mapper — no extra business logic needed here.
     */
    @Override
    public BgtMngVO selectBgtMngDetail(BgtMngInVO inVO) {
        return bgtMngMapper.selectBgtMngDetail(inVO);
    }

    /**
     * Insert new budget record (등록)
     */
    @Override
    public void insertBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.insertBgtMng(inVO);
    }

    /**
     * Update existing budget record (수정)
     */
    @Override
    public void updateBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.updateBgtMng(inVO);
    }

    /**
     * Soft delete budget record — sets del_yn = 'Y' (논리삭제)
     */
    @Override
    public void deleteBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.deleteBgtMng(inVO);
    }
}
