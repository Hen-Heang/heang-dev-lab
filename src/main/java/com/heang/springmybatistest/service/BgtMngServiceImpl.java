package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.BgtMngMapper;
import com.heang.springmybatistest.vo.BgtMngInVO;
import com.heang.springmybatistest.vo.BgtMngOutVO;
import com.heang.springmybatistest.vo.BgtMngVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BgtMngServiceImpl implements BgtMngService {

    private final BgtMngMapper bgtMngMapper;

    @Override
    public List<BgtMngOutVO> selectBgtMngList(BgtMngInVO inVO) {
        return bgtMngMapper.selectBgtMngList(inVO);
    }

    @Override
    public BgtMngVO selectBgtMngDetail(BgtMngInVO inVO) {
        return bgtMngMapper.selectBgtMngDetail(inVO);
    }

    @Override
    public void insertBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.insertBgtMng(inVO);
    }

    @Override
    public void updateBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.updateBgtMng(inVO);
    }

    @Override
    public void deleteBgtMng(BgtMngInVO inVO) {
        bgtMngMapper.deleteBgtMng(inVO);
    }
}
