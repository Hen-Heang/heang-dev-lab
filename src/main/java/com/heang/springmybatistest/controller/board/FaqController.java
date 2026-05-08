package com.heang.springmybatistest.controller.board;

import com.heang.springmybatistest.service.FaqService;
import com.heang.springmybatistest.vo.FaqInVO;
import com.heang.springmybatistest.vo.FaqOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @RequestMapping("/faq/list.do")
    public String list(
            @ModelAttribute("searchVO") FaqInVO inVO,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "1") int pageIndex,
            ModelMap model) throws Exception {

        int rowcount = 10;
        inVO.set_rowcount(rowcount);
        inVO.set_startrow((pageIndex - 1) * rowcount);

        int totCnt = faqService.selectListTotCnt(inVO);
        List<FaqOutVO> resultList = faqService.selectList(inVO);

        int totalPages = (int) Math.ceil((double) totCnt / rowcount);

        model.addAttribute("resultList", resultList);
        model.addAttribute("totCnt", totCnt);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("bbsKiCdList", faqService.selectBbsKiCdList());
        model.addAttribute("searchVO", inVO);

        return "faq/faqList";
    }

    @RequestMapping("/faq/regist.do")
    public String registView(
            @ModelAttribute("searchVO") FaqInVO inVO,
            ModelMap model) throws Exception {

        model.addAttribute("searchVO", inVO);
        model.addAttribute("bbsKiCdList", faqService.selectBbsKiCdList());

        return "faq/faqRegist";
    }

    @RequestMapping("/faq/registProc.do")
    public String registProc(
            @ModelAttribute("searchVO") FaqInVO inVO) throws Exception {

        faqService.insert(inVO);

        return "redirect:/faq/list.do";
    }

    @RequestMapping("/faq/detail.do")
    public String detail(
            @ModelAttribute("searchVO") FaqInVO inVO,
            ModelMap model) throws Exception {

        FaqOutVO result = faqService.selectDetail(inVO);

        model.addAttribute("result", result);
        model.addAttribute("searchVO", inVO);
        model.addAttribute("bbsKiCdList", faqService.selectBbsKiCdList());

        return "faq/faqDetail";
    }

    @RequestMapping("/faq/updateProc.do")
    public String updateProc(
            @ModelAttribute("searchVO") FaqInVO inVO) throws Exception {

        faqService.update(inVO);

        return "redirect:/faq/list.do";
    }

    @RequestMapping("/faq/deleteProc.do")
    public String deleteProc(
            @ModelAttribute("searchVO") FaqInVO inVO) throws Exception {

        faqService.delete(inVO);

        return "redirect:/faq/list.do";
    }
}