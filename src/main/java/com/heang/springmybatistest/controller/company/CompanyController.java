package com.heang.springmybatistest.controller.company;

import com.heang.springmybatistest.service.CompanyService;
import com.heang.springmybatistest.vo.CompanyInVO;
import com.heang.springmybatistest.vo.CompanyOutVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    // ──────────────────────────────────────────────
    // LIST  — 신청 접수내역 (Page 18 of UI doc)
    // ──────────────────────────────────────────────
    @GetMapping("/list")
    public String list(@ModelAttribute("searchVO") CompanyInVO inVO, Model model) {

        // 1. Calculate pagination offset
        int pageIndex = inVO.get_startrow() == 0 ? 1 :
                        (inVO.get_startrow() / inVO.get_rowcount()) + 1;
        inVO.set_startrow((pageIndex - 1) * inVO.get_rowcount());
        inVO.set_endrow(pageIndex * inVO.get_rowcount());

        // 2. Get total count for pagination display
        int totalCount = companyService.selectListTotCnt(inVO);

        // 3. Get current page data
        List<CompanyOutVO> resultList = companyService.selectList(inVO);

        // 4. Calculate total pages
        int totalPages = (int) Math.ceil((double) totalCount / inVO.get_rowcount());

        model.addAttribute("resultList", resultList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageIndex);

        return "company/companyList";
    }

    // ──────────────────────────────────────────────
    // DETAIL POPUP — 신청 기업 상세조회 (Page 19)
    // ──────────────────────────────────────────────
    @GetMapping("/detail")
    public String detail(@RequestParam Long id, Model model) {

        CompanyInVO inVO = new CompanyInVO();
        inVO.setId(id);

        CompanyOutVO detail = companyService.selectDetail(inVO);
        model.addAttribute("detail", detail);

        return "company/companyDetail";
    }

    // ──────────────────────────────────────────────
    // REGISTER FORM — 대상 기업 신규등록 (Page 16~17)
    // ──────────────────────────────────────────────
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("companyInVO", new CompanyInVO());
        return "company/companyRegister";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute CompanyInVO inVO) {
        companyService.insert(inVO);
        return "redirect:/company/list";
    }

    // ──────────────────────────────────────────────
    // COUNT API — for dashboard stat card
    // GET /api/company/count → { "data": 5 }
    // ──────────────────────────────────────────────
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> count() {
        int total = companyService.selectListTotCnt(new CompanyInVO());
        return ResponseEntity.ok(Map.of("data", total));
    }

    // ──────────────────────────────────────────────
    // UPDATE STATUS — 승인 / 반려 (Page 19)
    // ──────────────────────────────────────────────
    @PostMapping("/status")
    public String updateStatus(@RequestParam Long id,
                               @RequestParam String status) {
        CompanyInVO inVO = new CompanyInVO();
        inVO.setId(id);
        inVO.setStatus(status);
        companyService.updateStatus(inVO);
        return "redirect:/company/list";
    }
}