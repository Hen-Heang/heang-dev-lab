package com.heang.springmybatistest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/egov")
public class EgovController {

    @GetMapping("/entNewReg")
    public String entNewReg() {
        return "egov/entNewReg";
    }

    @GetMapping("/entAplRe")
    public String entAplRe() {
        return "egov/entAplRe";
    }
}