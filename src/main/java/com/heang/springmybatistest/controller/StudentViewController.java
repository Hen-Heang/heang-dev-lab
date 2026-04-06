package com.heang.springmybatistest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentViewController {

    @GetMapping("/list")
    public String listPage() {
        return "students/list"; // Templates location: templates/students/list.html
    }
}
