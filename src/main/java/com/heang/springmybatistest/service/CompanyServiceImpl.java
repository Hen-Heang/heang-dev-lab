package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.CompanyMapper;
import com.heang.springmybatistest.vo.CompanyInVO;
import com.heang.springmybatistest.vo.CompanyOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;

    @Override
    public List<CompanyOutVO> selectList(CompanyInVO inVO) {
        return companyMapper.selectList(inVO);
    }

    @Override
    public int selectListTotCnt(CompanyInVO inVO) {
        return companyMapper.selectListTotCnt(inVO);
    }

    @Override
    public CompanyOutVO selectDetail(CompanyInVO inVO) {
        return companyMapper.selectDetail(inVO);
    }

    @Override
    public void insert(CompanyInVO inVO) {
        companyMapper.insert(inVO);
    }

    @Override
    public void updateStatus(CompanyInVO inVO) {
        companyMapper.updateStatus(inVO);
    }
}
