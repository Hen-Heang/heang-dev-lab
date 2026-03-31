package com.heang.springmybatistest.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * CmmVO — Common Value Object
 *
 * This is the parent class for all InVO and OutVO classes.
 * In real team projects this would extend egovframework.com.cmm.CmmVO.
 * This is a simplified version for practice without the UCM framework.
 *
 * Responsibilities:
 *   1. Pagination  — _rowcount, _startrow, _endrow, rnum
 *   2. Security    — _ssuserId, _ssuserNo, _ssuserIp, _txid
 *   3. Domain info — _appCd, _menuId, _userType, _ctpvCd
 *
 * Usage:
 *   public class SmpBoardInVO extends CmmVO { ... }
 *   public class SmpBoardOutVO extends CmmVO { ... }
 */
@Setter
@Getter
public class CmmVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ──────────────────────────────────────────────────
    // 1. Pagination
    // ──────────────────────────────────────────────────

    /**
     * rnum — Row Number
     * Sequential number assigned to each row. Used to display 1, 2, 3... on screen.
     * Filled by SQL ROW_NUMBER() or the resultMap.
     */
    private Integer rnum;

    /**
     * idx — Index
     * 0-based index. Sometimes used instead of OFFSET.
     */
    private Integer idx;

    /**
     * _rowcount — number of rows to show per page
     * Example: 10 means fetch 10 records per page
     * SQL: LIMIT #{_rowcount}
     */
    private Integer _rowcount = 10; // default 10

    /**
     * _startrow — start row (OFFSET)
     * Page 1: 0,  Page 2: 10,  Page 3: 20 ...
     * SQL: OFFSET #{_startrow}
     *
     * Formula: (pageIndex - 1) * _rowcount
     */
    private Integer _startrow = 0;

    /**
     * _endrow — end row
     * Used in Oracle-style pagination: WHERE rnum BETWEEN _startrow AND _endrow
     * PostgreSQL uses LIMIT/OFFSET instead, so this is for reference only.
     *
     * Formula: pageIndex * _rowcount
     */
    private Integer _endrow = 10;

    // ──────────────────────────────────────────────────
    // 2. Security & Audit
    // ──────────────────────────────────────────────────

    // In real team projects, the setter automatically reads userId from the Session.
    // Practice version: set manually
    /**
     * _ssuserId — Session Security USER ID
     * Real projects: setter ignores the parameter and reads userId from Session automatically
     * Practice projects: set directly ("admin", "user01", etc.)
     */
    private String _ssuserId;

    /**
     * _ssuserNo — Session Security USER NUMBER
     * User identification number (e.g. "20240001")
     */
    private String _ssuserNo;

    // In real team projects, injected automatically via UcmContext.get(AttrKey.IP)
    /**
     * _ssuserIp — Session Security USER IP
     * Real projects: setter injects via UcmContext.get(AttrKey.IP) automatically
     * Recorded in audit log on INSERT/UPDATE
     */
    private String _ssuserIp;

    /**
     * _txid — Transaction ID
     * Unique ID generated per request. Used for log tracing.
     */
    private String _txid;

    /**
     * _menuId — MENU ID
     * Tracks which menu the request came from. Recorded in access logs.
     */
    private String _menuId;

    /**
     * _appCd — APPLICATION CODE
     * Identifies which system/module sent the request.
     */
    private String _appCd;

    // ──────────────────────────────────────────────────
    // 3. Domain Info
    // ──────────────────────────────────────────────────

    /**
     * _userType — USER TYPE
     * Example: "ADMIN", "USER", "GUEST"
     * Real projects: auto-injected from UcmProfile.DOMAIN_INFO.getUserType()
     */
    private String _userType;

    /**
     * _ctpvCd — Province/City Code
     * Example: Seoul = "11", Busan = "21", Gyeonggi = "41"
     * Real projects: auto-injected from UcmProfile.DOMAIN_INFO.getCtpvCd()
     * Used in public-sector systems that need region-based data separation
     */
    private String _ctpvCd;

    // ──────────────────────────────────────────────────
    // Getters & Setters
    // ──────────────────────────────────────────────────

}