package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.BoardFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * BoardFileService — File attachment service interface (첨부파일 서비스)
 *
 * Handles saving files to disk AND inserting records to DB.
 * Called from BoardServiceImpl inside a @Transactional method.
 * If this throws → the parent transaction rolls back the board insert too.
 */
public interface BoardFileService {

    // Save files to disk + insert DB records (파일 저장 + DB 등록)
    void saveFiles(Long boardSn, List<MultipartFile> files);

    // Get all files for a board post (게시글 첨부파일 목록 조회)
    List<BoardFileVO> findByBoardSn(Long boardSn);

    // Get one file by PK — for download (다운로드용 단건 조회)
    BoardFileVO findById(Long fileSn);
}
