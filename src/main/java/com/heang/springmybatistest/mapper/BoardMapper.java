package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.BoardSearchV0;
import com.heang.springmybatistest.vo.BoardVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Board MyBatis Mapper Interface (게시판 매퍼 인터페이스)
 * <p>
 * Each method name must exactly match the id in BoardMapper.xml
 * 메서드 이름은 반드시 BoardMapper.xml의 id와 일치해야 함
 */
@Mapper
public interface BoardMapper {

    // SELECT all active boards (활성 게시글 전체 조회)
    List<BoardVO> findAll();

    // SELECT one board by PK (PK로 단건 조회)
    BoardVO findById(Long boardSn);

    // SELECT boards by title keyword (제목 키워드 검색)
    List<BoardVO> findByTitle(String keyword);

    // INSERT new board (게시글 등록)
    void insert(BoardVO board);

    // UPDATE board title and content (게시글 수정)
    int update(BoardVO board);

    // SOFT DELETE — set use_yn = 'N' (소프트 삭제)
    int delete(Long boardSn);

    List<BoardVO> findBySearch(BoardSearchV0 searchV0);
    int countBySearch(BoardSearchV0 searchV0);
}
