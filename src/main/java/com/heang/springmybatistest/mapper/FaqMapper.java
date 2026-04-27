package com.heang.springmybatistest.mapper;

import com.heang.springmybatistest.vo.CommCdVO;
import com.heang.springmybatistest.vo.FaqInVO;
import com.heang.springmybatistest.vo.FaqOutVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqMapper {

    List<FaqOutVO> selectList(FaqInVO inVO) throws Exception;

    int selectListTotCnt(FaqInVO inVO) throws Exception;

    FaqOutVO selectDetail(FaqInVO inVO) throws Exception;

    void insert(FaqInVO inVO) throws Exception;

    void update(FaqInVO inVO) throws Exception;

    void delete(FaqInVO inVO) throws Exception;

    List<CommCdVO> selectBbsKiCdList() throws Exception;
}
