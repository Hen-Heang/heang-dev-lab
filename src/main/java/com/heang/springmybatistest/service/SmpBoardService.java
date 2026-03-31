package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.SmpBoardInVO;
import com.heang.springmybatistest.vo.SmpBoardOutVO;

import java.util.List;

public interface SmpBoardService {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;

    int selectListTotCnt(SmpBoardInVO inVO) throws Exception;

    SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception;

    void insert(SmpBoardInVO inVO) throws Exception;

    void update(SmpBoardInVO inVO) throws Exception;

    void delete(SmpBoardInVO inVO) throws Exception;

}
