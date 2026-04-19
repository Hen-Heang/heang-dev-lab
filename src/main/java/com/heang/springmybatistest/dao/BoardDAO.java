package com.heang.springmybatistest.dao;

import com.heang.springmybatistest.vo.BoardSearchV0;
import com.heang.springmybatistest.vo.BoardVO;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BoardDAO — Data Access Object (게시판 데이터 접근 객체)
 * <p>
 * Pattern A: Uses SqlSessionTemplate to call SQL in BoardMapper.xml
 * SqlSessionTemplate으로 BoardMapper.xml의 SQL을 직접 호출
 * <p>
 * Call format: sqlSession.method(NS + "xmlId", parameter)
 * NS = namespace from BoardMapper.xml
 * <p>
 * selectOne → returns null if not found (not an error!)
 * selectList → returns empty List if not found (never null)
 */
@Repository              // marks this as a DB access layer component
@RequiredArgsConstructor // injects SqlSessionTemplate via constructor (no @Autowired needed)
public class BoardDAO {

    private final SqlSessionTemplate sqlSession;

    // Namespace defined in BoardMapper.xml — <mapper namespace= "...">
    // Store as constant to avoid repeating the long string 6 times
    private static final String NS = "com.heang.springmybatistest.mapper.BoardMapper.";

    /**
     * SELECT ALL
     * Get all active boards, newest first (활성 게시글 전체 조회)
     * <p>
     * selectList → always returns List, never null
     * if no records → returns empty List []
     */
    public List<BoardVO> findAll() {
        return sqlSession.selectList(NS + "findAll");
    }

    /**
     * SELECT BY ID
     * Get one board by primary key (PK로 단건 조회)
     * <p>
     * selectOne → returns null if not found (check before using!)
     * selectOne → throws TooManyResultsException if 2+ rows match
     */
    public BoardVO findById(Long boardSn) {
        return sqlSession.selectOne(NS + "findById", boardSn);
    }

    /**
     * SELECT BY TITLE KEYWORD
     * Search boards where title contains keyword (제목 키워드 검색)
     * <p>
     * selectList → always returns List, never null
     */
    public List<BoardVO> findByTitle(String keyword) {
        return sqlSession.selectList(NS + "findByTitle", keyword);
    }

    /**
     * INSERT * new board post (게시글 등록)
     * <p>
     * useGeneratedKeys in XML → boardVO.boardSn is filled after insert
     */
    public void insert(BoardVO boardVO) {
        sqlSession.insert(NS + "insert", boardVO);
    }

    /**
     * UPDATE * board title and content (게시글 수정)
     * <p>
     * returns int = number of rows updated
     * if returns 0 → board not found → throw NotFoundException
     */
    public int update(BoardVO boardVO) {
        return sqlSession.update(NS + "update", boardVO);
    }

    /**
     * SOFT DELETE
     * Set use_yn = 'N' instead of physical delete (소프트 삭제)
     * <p>
     * Uses sqlSession.update() — NOT sqlSession.delete()
     * because the XML tag is <update>, not <delete>
     * <p>
     * returns int = number of rows updated
     * if returns 0 → board not found → throw NotFoundException
     */
    public int delete(Long boardSn) {
        return sqlSession.update(NS + "delete", boardSn);
    }

    public List<BoardVO> findBySearch(BoardSearchV0 searchV0) {
        return sqlSession.selectList(NS + "findBySearch", searchV0);
    }

    public int countBySearch(BoardSearchV0 searchV0) {
        return sqlSession.selectOne(NS + "countBySearch", searchV0);
    }
}