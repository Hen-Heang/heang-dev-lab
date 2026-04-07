package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.SmpBoardMapper;
import com.heang.springmybatistest.vo.SmpBoardInVO;
import com.heang.springmybatistest.vo.SmpBoardOutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("smpBoardService")

public class SmpBoardServiceImpl implements SmpBoardService {

    @Autowired
    private SmpBoardMapper smpBoardMapper;

    @Override
    public List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception {
        return smpBoardMapper.selectList(inVO);
    }

    @Override
    public int selectListTotCnt(SmpBoardInVO inVO) throws Exception {
        return smpBoardMapper.selectListToCnt(inVO);
    }

    @Override
    public SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception {
        return smpBoardMapper.selectDetail(inVO);
    }

    @Override
    public void insert(SmpBoardInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");  // practice only: real projects inject from Session
        smpBoardMapper.insert(inVO);
    }

    @Override
    public void update(SmpBoardInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");  // practice only
        smpBoardMapper.update(inVO);

    }

    @Override
    public void delete(SmpBoardInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");  // practice only
        smpBoardMapper.delete(inVO);

    }
}
