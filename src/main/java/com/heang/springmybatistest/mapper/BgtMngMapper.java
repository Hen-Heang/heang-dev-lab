package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngOutVO;
import com.heang.springmybatistest.vo.BgtMngVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BgtMngMapper {

    List<BgtMngOutVO> selectBgtMngList(BgtMngInVO inVO);

    BgtMngVO selectBgtMngDetail(BgtMngInVO inVO);

    void insertBgtMng(BgtMngInVO inVO);

    void updateBgtMng(BgtMngInVO inVO);

    void deleteBgtMng(BgtMngInVO inVO);
}
