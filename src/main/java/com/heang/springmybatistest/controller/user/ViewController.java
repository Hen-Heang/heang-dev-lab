package com.heang.springmybatistest.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    /**
     * GET /login — show login page
     * Spring Security handles POST /login automatically (no controller method needed)
     *
     * @param error   → ?error param set by Security on bad credentials
     * @param logout  → ?logout param set by Security after logout
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            org.springframework.ui.Model model
    ) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid username or password. (아이디 또는 비밀번호가 잘못되었습니다)");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "You have been logged out. (로그아웃 되었습니다)");
        }
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "users";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/user-list")
    public String userList() {
        return "user-list";
    }
}
