package com.heang.springmybatistest.controller.common;

import com.heang.springmybatistest.model.CommonCode;
import com.heang.springmybatistest.service.CommonCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CommonCodeController — 공통코드 관리 컨트롤러
 *
 * URL: /common-code
 *
 * Demonstrates the core pattern:
 *   GET  /common-code          → list page (with optional group filter)
 *   POST /common-code/insert   → register new code
 *   POST /common-code/update   → update code name / sort / use_yn
 *   POST /common-code/delete   → delete one code
 */
@Controller
@RequestMapping("/common-code")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    /**
     * GET /common-code
     * List all codes, optionally filtered by codeGroup (전체 코드 목록)
     *
     * ?codeGroup=USER_STATUS → shows only that group
     * no param              → shows all groups
     */
    @GetMapping
    public String list(
            @RequestParam(required = false, defaultValue = "") String codeGroup,
            Model model
    ) {
        model.addAttribute("codes",      commonCodeService.findAll(codeGroup));
        model.addAttribute("groups",     commonCodeService.findAllGroups());
        model.addAttribute("codeGroup",  codeGroup); // keep filter selected
        model.addAttribute("newCode",    new CommonCode()); // empty form binding
        return "common-code/list";
    }

    /**
     * POST /common-code/insert
     * Register a new code (신규 코드 등록)
     *
     * @ModelAttribute maps all form fields to CommonCode automatically
     * RedirectAttributes.addFlashAttribute → shows success/error message after redirect
     */
    @PostMapping("/insert")
    public String insert(
            @ModelAttribute CommonCode code,
            RedirectAttributes ra
    ) {
        try {
            commonCodeService.insert(code);
            ra.addFlashAttribute("successMsg", "Code registered. (" + code.getCodeGroup() + " / " + code.getCodeValue() + ")");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
        }
        // Redirect back to same group filter (등록 후 같은 그룹으로 이동)
        return "redirect:/common-code?codeGroup=" + code.getCodeGroup();
    }

    /**
     * POST /common-code/update
     * Update code name / sort order / use_yn (코드 수정)
     */
    @PostMapping("/update")
    public String update(
            @ModelAttribute CommonCode code,
            RedirectAttributes ra
    ) {
        commonCodeService.update(code);
        ra.addFlashAttribute("successMsg", "Code updated.");
        return "redirect:/common-code?codeGroup=" + code.getCodeGroup();
    }

    /**
     * POST /common-code/delete
     * Delete one code by composite PK (코드 삭제)
     */
    @PostMapping("/delete")
    public String delete(
            @RequestParam String codeGroup,
            @RequestParam String codeValue,
            RedirectAttributes ra
    ) {
        commonCodeService.delete(codeGroup, codeValue);
        ra.addFlashAttribute("successMsg", "Code deleted.");
        return "redirect:/common-code?codeGroup=" + codeGroup;
    }
}
