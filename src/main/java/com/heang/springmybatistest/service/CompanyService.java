package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.CompanyInVO;
import com.heang.springmybatistest.vo.CompanyOutVO;

import java.util.List;

public interface CompanyService {

    List<CompanyOutVO> selectList(CompanyInVO inVO);

    int selectListTotCnt(CompanyInVO inVO);

    CompanyOutVO selectDetail(CompanyInVO inVO);

    void insert(CompanyInVO inVO);

    void updateStatus(CompanyInVO inVO);
}
