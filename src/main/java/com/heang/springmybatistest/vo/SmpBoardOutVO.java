package com.heang.springmybatistest.vo;

import com.heang.springmybatistest.common.CmmVO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * SmpBoardOutVO — Sample Board OUTPUT Value Object
 * <p>
 * Extends CmmVO, so it automatically inherits rnum (row number)
 * which can be displayed on screen as ${item.rnum}.
 * <p>
 * Purpose: holds data coming FROM the DB TO the view (HTML)
 *   - Mapped by the SQL resultMap in SmpBoardMapper.xml
 *   - Accessed in HTML via th:text="${item.boardTitle}"
 * <p>
 * InVO vs OutVO:
 *   InVO  = browser → server  (data the user sends)
 *   OutVO = DB      → view    (data the server displays)
 */
@Setter
@Getter
public class SmpBoardOutVO extends CmmVO {

    @Serial
    private static final long serialVersionUID = 1L;

    // ──────────────────────────────────────────────────
    // DB result fields (DB column → Java field)
    // Auto-mapped by the resultMap in SQL XML
    // ──────────────────────────────────────────────────

    /**
     * boardSn — board_sn (PK, post sequence number)
     * Used in detail/edit/delete links:
     * th:href="@{/smpBoard/detail.do(boardSn=${item.boardSn})}"
     */
    private Long boardSn;

    /**
     * boardTitle — board_title (post title)
     * Displayed as a clickable link on the list page
     */
    private String boardTitle;

    /**
     * boardCn — board_cn (post content)
     * Displayed on the detail page
     */
    private String boardCn;

    /**
     * useYn — use_yn (active flag)
     * 'Y' = active, 'N' = deleted
     * List queries filter WHERE use_yn = 'Y', so this is always 'Y' in results
     */
    private String useYn;

    /**
     * dataRegId — data_reg_id (created by user ID)
     * Who created the post. Injected from CmmVO._ssuserId on INSERT.
     */
    private String dataRegId;

    /**
     * dataRegDt — data_reg_dt (created datetime)
     * Displayed as "2026-03-31 14:30:00". Using String avoids format conversion.
     */
    private String dataRegDt;

    /**
     * dataChgId — data_chg_id (last modified by user ID)
     */
    private String dataChgId;

    /**
     * dataChgDt — data_chg_dt (last modified datetime)
     */
    private String dataChgDt;

    // ──────────────────────────────────────────────────
    // Getters & Setters
    // ──────────────────────────────────────────────────

}