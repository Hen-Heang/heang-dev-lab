package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.BoardSearchV0;
import com.heang.springmybatistest.vo.BoardVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * BoardService — Service Interface (게시판 서비스 인터페이스)
 * <p>
 * Defines WHAT the service can do — not HOW
 * 무엇을 하는지 정의 — 어떻게 하는지는 Impl에서
 * <p>
 * Controller depends on this interface, not the implementation
 * 컨트롤러는 구현체가 아닌 이 인터페이스에만 의존
 */
public interface BoardService {

    // Get all active boards (활성 게시글 전체 조회)
    List<BoardVO> findAll();

    // Get one board by PK (PK로 단건 조회)
    BoardVO findById(Long boardSn);

    // Search boards by title keyword (제목 키워드 검색)
    List<BoardVO> findByTitle(String keyword);

    // Insert new board (게시글 등록)
    void insert(BoardVO boardVO);

    // Update board title and content (게시글 수정)
    int update(BoardVO boardVO);

    // Soft delete — set use_yn = 'N' (소프트 삭제)
    int delete(Long boardSn);

    int countBySearch(BoardSearchV0 searchV0);
    List<BoardVO> findBySearch(BoardSearchV0 searchV0);

    // Insert board + save files atomically (게시글+첨부파일 트랜잭션 등록)
    void insertWithFiles(BoardVO boardVO, List<MultipartFile> files);
}
