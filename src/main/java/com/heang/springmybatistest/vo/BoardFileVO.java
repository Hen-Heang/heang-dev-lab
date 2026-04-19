package com.heang.springmybatistest.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * BoardFileVO — Board file attachment record (게시판 첨부파일 VO)
 *
 * Maps to the board_file table.
 * origName  = original filename shown to users   (원본 파일명)
 * savedName = UUID filename stored on disk       (저장 파일명, UUID)
 */
@Data
public class BoardFileVO {
    private Long          fileSn;    // PK — auto generated
    private Long          boardSn;   // FK → co_smp_board_m.board_sn
    private String        origName;  // original filename e.g. "report.pdf"
    private String        savedName; // saved on disk e.g. "a1b2c3.pdf"
    private long          fileSize;  // bytes
    private LocalDateTime createdAt;
}
