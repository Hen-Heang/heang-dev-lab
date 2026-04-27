package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.FaqMapper;
import com.heang.springmybatistest.vo.CommCdVO;
import com.heang.springmybatistest.vo.FaqInVO;
import com.heang.springmybatistest.vo.FaqOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqMapper faqMapper;

    @Override
    public List<FaqOutVO> selectList(FaqInVO inVO) throws Exception {
        return faqMapper.selectList(inVO);
    }

    @Override
    public int selectListTotCnt(FaqInVO inVO) throws Exception {
        return faqMapper.selectListTotCnt(inVO);
    }

    @Override
    public FaqOutVO selectDetail(FaqInVO inVO) throws Exception {
        return faqMapper.selectDetail(inVO);
    }

    @Override
    public void insert(FaqInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");
        faqMapper.insert(inVO);
    }

    @Override
    public void update(FaqInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");
        faqMapper.update(inVO);
    }

    @Override
    public void delete(FaqInVO inVO) throws Exception {
        inVO.set_ssuserId("admin");
        faqMapper.delete(inVO);
    }

    @Override
    public List<CommCdVO> selectBbsKiCdList() throws Exception {
        return faqMapper.selectBbsKiCdList();
    }
}
