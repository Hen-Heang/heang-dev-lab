package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.CommCdVO;
import com.heang.springmybatistest.vo.FaqInVO;
import com.heang.springmybatistest.vo.FaqOutVO;

import java.util.List;

public interface FaqService {

    List<FaqOutVO> selectList(FaqInVO inVO) throws Exception;

    int selectListTotCnt(FaqInVO inVO) throws Exception;

    FaqOutVO selectDetail(FaqInVO inVO) throws Exception;

    void insert(FaqInVO inVO) throws Exception;

    void update(FaqInVO inVO) throws Exception;

    void delete(FaqInVO inVO) throws Exception;

    List<CommCdVO> selectBbsKiCdList() throws Exception;
}