package com.heang.springmybatistest.service;

import com.heang.springmybatistest.exception.ConflictException;
import com.heang.springmybatistest.mapper.CommonCodeMapper;
import com.heang.springmybatistest.model.CommonCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CommonCodeServiceImpl — 공통코드 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {

    private final CommonCodeMapper commonCodeMapper;

    @Override
    public List<CommonCode> findByGroup(String codeGroup) {
        return commonCodeMapper.findByGroup(codeGroup);
    }

    @Override
    public List<String> findAllGroups() {
        return commonCodeMapper.findAllGroups();
    }

    @Override
    public List<CommonCode> findAll(String codeGroup) {
        return commonCodeMapper.findAll(codeGroup);
    }

    @Override
    public void insert(CommonCode code) {
        // Duplicate check — composite PK (중복 확인: code_group + code_value)
        int count = commonCodeMapper.countByGroupAndValue(code.getCodeGroup(), code.getCodeValue());
        if (count > 0) {
            throw new ConflictException(
                "Code already exists: [" + code.getCodeGroup() + "] " + code.getCodeValue()
            );
        }
        if (code.getUseYn() == null) code.setUseYn("Y");
        commonCodeMapper.insert(code);
    }

    @Override
    public void update(CommonCode code) {
        commonCodeMapper.update(code);
    }

    @Override
    public void delete(String codeGroup, String codeValue) {
        commonCodeMapper.delete(codeGroup, codeValue);
    }
}
