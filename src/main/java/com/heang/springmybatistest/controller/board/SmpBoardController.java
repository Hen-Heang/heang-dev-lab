package com.heang.springmybatistest.controller.board;

import com.heang.springmybatistest.service.SmpBoardService;
import com.heang.springmybatistest.vo.SmpBoardInVO;
import com.heang.springmybatistest.vo.SmpBoardOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SmpBoardController {


    private final SmpBoardService smpBoardService;

    @RequestMapping("/smpBoard/list.do")
    public String selectList(
            @ModelAttribute("searchVO") SmpBoardInVO inVO,
            ModelMap modelMap) throws Exception {

//        set pagination
        if (inVO.get_startrow() == null) inVO.set_startrow(0);
        if (inVO.get_rowcount() == null) inVO.set_rowcount(10);

//        get text
        int toCnt = smpBoardService.selectListTotCnt(inVO);

        List<SmpBoardOutVO> resultSmpBoardList = smpBoardService.selectList(inVO);
        modelMap.addAttribute("resultList", resultSmpBoardList);
        modelMap.addAttribute("toCnt", toCnt);
        modelMap.addAttribute("searchVO");

        return "smp/SmpBoardList";
    }


    @RequestMapping("/smpBoard/detail.do")
    public String selectDetail(
            @ModelAttribute ("searchVO") SmpBoardInVO inVO,
            ModelMap modelMap) throws Exception{
        SmpBoardOutVO  result = smpBoardService.selectDetail(inVO);

        modelMap.addAttribute("result", result);
        modelMap.addAttribute("searchVO", inVO);

        return "smp/SmpBoardDetail";
    }

    @RequestMapping("/smpBoard/regist.do")
    public String registView(
            @ModelAttribute("searchVO") SmpBoardInVO inVO,
            ModelMap model) throws Exception {

        model.addAttribute("searchVO", inVO);

        return "smp/SmpBoardRegist";  // → templates/smp/SmpBoardRegist.html
    }

    @RequestMapping("/smpBoard/registProc.do")
    public String registProc(
            @ModelAttribute("searchVO") SmpBoardInVO inVO) throws Exception {

        smpBoardService.insert(inVO);

        return "redirect:/smpBoard/list.do";  // PRG 패턴
    }

    @RequestMapping("/smpBoard/updateProc.do")
    public String updateProc(
            @ModelAttribute("searchVO") SmpBoardInVO inVO) throws Exception {

        smpBoardService.update(inVO);

        return "redirect:/smpBoard/list.do";  // PRG 패턴
    }

    @RequestMapping("/smpBoard/deleteProc.do")
    public String deleteProc(
            @ModelAttribute("searchVO") SmpBoardInVO inVO) throws Exception {

        smpBoardService.delete(inVO);

        return "redirect:/smpBoard/list.do";  // PRG 패턴
    }



}
