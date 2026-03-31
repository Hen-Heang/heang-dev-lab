package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * SmpBoardInVO — Sample Board INPUT Value Object
 * <p>
 * Extends CmmVO, so it automatically inherits:
 *   _rowcount, _startrow, _endrow (pagination)
 *   _ssuserId, _ssuserIp (security/audit)
 *   rnum (row number)
 * <p>
 * Purpose: holds data coming FROM the browser TO the server
 *   - Search conditions (searchCondition, searchKeyword)
 *   - Form input values (boardTitle, boardCn)
 *   - PK for single-record lookup/update/delete (boardSn)
 * <p>
 * Used in: Controller, ServiceImpl, Mapper, SQL XML
 */
@Setter
@Getter
public class SmpBoardInVO extends CmmVO {

    @Serial
    private static final long serialVersionUID = 1L;

    // ──────────────────────────────────────────────────
    // Search Conditions
    // ──────────────────────────────────────────────────

    /**
     * searchCondition — search type code
     * "1" = search by title
     * "2" = search by content
     * "3" = search by title + content
     * HTML: <select name="searchCondition">
     */
    private String searchCondition;

    /**
     * searchKeyword — search keyword
     * HTML: <input name="searchKeyword" type="text">
     * SQL:  LIKE '%' || #{searchKeyword} || '%'
     */
    private String searchKeyword;

    // ──────────────────────────────────────────────────
    // Form Input Fields (insert / update)
    // ──────────────────────────────────────────────────

    /**
     * boardSn — Board Sequence Number (PK)
     * Used for single-record lookup, update, and delete
     * HTML: <input type="hidden" name="boardSn" th:value="${board.boardSn}">
     */
    private Long boardSn;

    /**
     * boardTitle — post title
     * HTML: <input name="boardTitle" type="text">
     */
    private String boardTitle;

    /**
     * boardCn — post content (CN = Content)
     * HTML: <textarea name="boardCn">
     */
    private String boardCn;

    /**
     * useYn — active flag (Y = active, N = deleted)
     * Set to 'N' on soft delete; default 'Y' set in ServiceImpl
     */
    private String useYn;

    // ──────────────────────────────────────────────────
    // Getters & Setters
    // ──────────────────────────────────────────────────

}