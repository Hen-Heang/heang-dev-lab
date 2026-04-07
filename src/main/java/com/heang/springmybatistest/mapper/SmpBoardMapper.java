package com.heang.springmybatistest.mapper;


import com.heang.springmybatistest.vo.SmpBoardInVO;
import com.heang.springmybatistest.vo.SmpBoardOutVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmpBoardMapper {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;

    int selectListToCnt (SmpBoardInVO inVO) throws Exception;

    SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception;

    void insert(SmpBoardInVO inVO) throws Exception;

    void update(SmpBoardInVO inVO) throws  Exception;

    void delete (SmpBoardInVO inVO ) throws  Exception;





}

