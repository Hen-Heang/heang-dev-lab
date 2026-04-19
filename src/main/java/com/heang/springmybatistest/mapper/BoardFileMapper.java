package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.BoardFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BoardFileMapper — MyBatis mapper for board file attachments (첨부파일 매퍼)
 *
 * Each method name must match the id in BoardFileMapper.xml
 */
@Mapper
public interface BoardFileMapper {

    // INSERT one file record (첨부파일 한 건 등록)
    void insert(BoardFileVO boardFile);

    // SELECT all files for a board post (게시글에 속한 파일 목록 조회)
    List<BoardFileVO> findByBoardSn(Long boardSn);

    // SELECT one file by PK — used for download (단건 조회, 다운로드용)
    BoardFileVO findById(Long fileSn);
}
