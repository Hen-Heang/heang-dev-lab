package com.heang.springmybatistest.service;

import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngOutVO;
import com.heang.springmybatistest.vo.BgtMngVO;

import java.util.List;

public interface BgtMngService {

    List<BgtMngOutVO> selectBgtMngList(BgtMngInVO inVO);

    BgtMngVO selectBgtMngDetail(BgtMngInVO inVO);

    void insertBgtMng(BgtMngInVO inVO);

    void updateBgtMng(BgtMngInVO inVO);

    void deleteBgtMng(BgtMngInVO inVO);
}
