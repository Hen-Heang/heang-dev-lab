package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.CompanyInVO;
import com.heang.springmybatistest.vo.CompanyOutVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper {

    List<CompanyOutVO> selectList(CompanyInVO inVO);

    int selectListTotCnt(CompanyInVO inVO);

    CompanyOutVO selectDetail(CompanyInVO inVO);

    void insert(CompanyInVO inVO);

    void updateStatus(CompanyInVO inVO);
}
