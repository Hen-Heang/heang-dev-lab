package com.heang.springmybatistest.service;

import com.heang.springmybatistest.mapper.FaqMapper;
import com.heang.springmybatistest.vo.CommCdVO;
import com.heang.springmybatistest.vo.FaqInVO;
import com.heang.springmybatistest.vo.FaqOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqMapper faqMapper;

    // ── Static dummy data for UI testing (DB 없이 화면 테스트용) ──────────
    private static final List<FaqOutVO> DUMMY_LIST = new ArrayList<>();
    static {
        String[][] rows = {
            {"1", "02", "자주묻는질문", "지원대상은 누구인가요?",          "사업대상 지방 정부에 위치한 중소기업에 재직중인 근로자입니다.", "admin", "2026-04-01"},
            {"2", "02", "자주묻는질문", "복지관련서비스 질문1",             "복지관련서비스 답변1 입니다.",                              "admin", "2026-04-02"},
            {"3", "02", "자주묻는질문", "복지관련서비스 질문2",             "복지관련서비스 답변2 입니다.",                              "admin", "2026-04-03"},
            {"4", "01", "공지사항",     "공지사항 테스트",                  "공지사항 내용입니다.",                                     "admin", "2026-04-04"},
            {"5", "02", "자주묻는질문", "점심 지원금 한도는 얼마인가요?",   "1인당 월 최대 100,000원 지원됩니다.",                       "admin", "2026-04-05"},
        };
        for (String[] r : rows) {
            FaqOutVO vo = new FaqOutVO();
            vo.setBbsSn(Long.parseLong(r[0]));
            vo.setBbsKiCd(r[1]);
            vo.setBbsKiCdNm(r[2]);
            vo.setBbsTitle(r[3]);
            vo.setBbsCn(r[4]);
            vo.setUseYn("Y");
            vo.setDataRegId(r[5]);
            vo.setDataRegDt(r[6]);
            DUMMY_LIST.add(vo);
        }
    }
    // ─────────────────────────────────────────────────────────────────────

    @Override
    public List<FaqOutVO> selectList(FaqInVO inVO) throws Exception {
        // TODO: switch to faqMapper.selectList(inVO) when DB is ready
        return DUMMY_LIST;
    }

    @Override
    public int selectListTotCnt(FaqInVO inVO) throws Exception {
        // TODO: switch to faqMapper.selectListTotCnt(inVO) when DB is ready
        return DUMMY_LIST.size();
    }

    @Override
    public FaqOutVO selectDetail(FaqInVO inVO) throws Exception {
        // TODO: switch to faqMapper.selectDetail(inVO) when DB is ready
        return DUMMY_LIST.stream()
                .filter(v -> v.getBbsSn().equals(inVO.getBbsSn()))
                .findFirst().orElse(null);
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
        // TODO: switch to faqMapper.selectBbsKiCdList() when DB is ready
        List<CommCdVO> list = new ArrayList<>();
        String[][] codes = {{"01", "공지사항"}, {"02", "자주묻는질문"}, {"03", "이용안내"}};
        for (String[] c : codes) {
            CommCdVO vo = new CommCdVO();
            vo.setCommDtcd(c[0]);
            vo.setCommDtlCdNm(c[1]);
            list.add(vo);
        }
        return list;
    }
}
