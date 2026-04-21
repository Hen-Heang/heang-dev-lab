package com.heang.springmybatistest.service;

import com.heang.springmybatistest.dao.BoardDAO;
import com.heang.springmybatistest.exception.NotFoundException;
import com.heang.springmybatistest.vo.BoardSearchV0;
import com.heang.springmybatistest.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * BoardServiceImpl — Service Implementation (게시판 서비스 구현체)
 * <p>
 * Defines HOW each method works (실제 비즈니스 로직 구현)
 *
 * @Service              → Spring manages this class as a Bean
 * @RequiredArgsConstructor → injects BoardDAO via constructor (no @Autowired needed)
 */
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardDAO         boardDAO;
    private final BoardFileService boardFileService;

    /**
     * SELECT ALL
     * Returns an empty list if no boards — never null (빈 리스트 반환, null 아님)
     */
    @Override
    public List<BoardVO> findAll() {
        return boardDAO.findAll();
    }

    /**
     * SELECT BY ID
     * Throws NotFoundException if not found (없으면 예외 발생)
     * null check here — controller never receives null
     */
    @Override
    public BoardVO findById(Long boardSn) {
        BoardVO board = boardDAO.findById(boardSn);

        // Business logic: handle null here, not in controller
        if (board == null) {
            throw new NotFoundException("Board not found: " + boardSn);
        }

        return board;
    }

    /**
     * SEARCH BY TITLE KEYWORD
     * Returns an empty list if no match — never null
     */
    @Override
    public List<BoardVO> findByTitle(String keyword) {
        return boardDAO.findByTitle(keyword);
    }

    /**
     * INSERT
     * Sets default useYn = 'Y' if not provided (기본값 설정)
     * Business logic lives here — not in Controller, not in DAO
     */
    @Override
    public void insert(BoardVO boardVO) {
        // Business logic: set a default value if not provided
        if (boardVO.getUseYn() == null) {
            boardVO.setUseYn("Y");
        }

        boardDAO.insert(boardVO);
    }

    /**
     * UPDATE
     * Checks board exists before updating (수정 전 존재 여부 확인)
     */
    @Override
    public int update(BoardVO boardVO) {
        // Business logic: verify exists first
        BoardVO existing = boardDAO.findById(boardVO.getBoardSn());

        if (existing == null) {
            throw new NotFoundException("Board not found: " + boardVO.getBoardSn());
        }

        return boardDAO.update(boardVO);
    }

    /**
     * SOFT DELETE
     * If 0 rows updated → board not found → throw exception
     * (0행 수정 = 존재하지 않음 → 예외 발생)
     */
    @Override
    public int delete(Long boardSn) {
        int rows = boardDAO.delete(boardSn);

        if (rows == 0) {
            throw new NotFoundException("Board not found: " + boardSn);
        }

        return rows;
    }

    @Override
    public int countBySearch(BoardSearchV0 searchV0) {
        return boardDAO.countBySearch(searchV0);
    }

    @Override
    public List<BoardVO> findBySearch(BoardSearchV0 searchV0) {
        return boardDAO.findBySearch(searchV0);
    }

    /**
     * INSERT board + files in ONE transaction (게시글+첨부파일 트랜잭션 등록)
     *
     * @Transactional means:
     *   - If boardDAO.insert() succeeds but boardFileService.saveFiles() throws
     *     → the board INSERT is rolled back automatically
     *   - Either BOTH succeed or NOTHING is saved
     * <p>
     * This is the most important pattern to understand before working in Korea.
     * Every write operation that touches 2+ tables must be @Transactional.
     */
    @Override
    @Transactional
    public void insertWithFiles(BoardVO boardVO, List<MultipartFile> files) {
        // Step 1: Insert board → boardVO.boardSn is filled by useGeneratedKeys
        if (boardVO.getUseYn() == null) {
            boardVO.setUseYn("Y");
        }
        boardDAO.insert(boardVO); // after this, boardVO.getBoardSn() has the new PK

        // Step 2: Save files to disk + insert DB records
        // If this throws → Step 1 is rolled back automatically
        boardFileService.saveFiles(boardVO.getBoardSn(), files);
    }
}
