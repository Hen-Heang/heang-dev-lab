package com.heang.springmybatistest.controller;

import com.heang.springmybatistest.model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// ================================================================
// @Controller
//   - This class handles HTTP requests and returns HTML pages.
//   - Different from @RestController which returns JSON.
//   - With @Controller, the return value is a VIEW NAME (HTML file).
// ================================================================
@Controller

// ================================================================
// @RequestMapping("/learn")
//   - All URLs in this controller start with /learn
//   - Example: @GetMapping("/hello") → full URL = /learn/hello
// ================================================================
@RequestMapping("/learn")
public class ThymeleafLearnController {

    // ============================================================
    // STEP 1: Basic Variables
    // URL: http://localhost:8080/learn/hello
    // ============================================================
    @GetMapping("/hello")
    public String hello(Model model) {
        // --------------------------------------------------------
        // Model = a container that carries data from Java → HTML
        // Think of it like a bag:
        //   - Java puts items INTO the bag (addAttribute)
        //   - HTML takes items OUT of the bag (${variableName})
        //
        // model.addAttribute("KEY", VALUE)
        //   KEY   = the name you use in HTML  → ${KEY}
        //   VALUE = the actual data to display
        // --------------------------------------------------------

        model.addAttribute("pageTitle", "Thymeleaf 연습 - 기본");  // String
        model.addAttribute("greeting", "안녕하세요! Thymeleaf 첫 번째 페이지입니다.");  // String
        model.addAttribute("score", 98);                           // int
        model.addAttribute("isAdmin", true);                       // boolean
        model.addAttribute("currentDate", "2024-06-01");           // String (date as text)
        model.addAttribute("username", "김철수");                   // String

        // --------------------------------------------------------
        // return "learn/hello"
        //   → Spring looks for: templates/learn/hello.html
        //   → It fills the HTML with the model data above
        //   → Then sends the completed HTML to the browser
        // --------------------------------------------------------
        return "learn/hello";
    }

    // ============================================================
    // STEP 2: Loops (th:each)
    // URL: http://localhost:8080/learn/users
    // ============================================================
    @GetMapping("/users")
    public String users(Model model) {
        // --------------------------------------------------------
        // List.of(...) = creates a fixed list with fake data
        // In a real project, this would come from the database:
        //   List<Users> userList = userService.findAll();
        //
        // Users.builder()... = Lombok @Builder pattern
        //   Creates a Users object field by field
        // --------------------------------------------------------
        List<Users> userList = List.of(
                Users.builder().id(1L).name("김철수").email("kim@test.com").role("ADMIN").status("ACTIVE").build(),
                Users.builder().id(2L).name("이영희").email("lee@test.com").role("USER").status("ACTIVE").build(),
                Users.builder().id(3L).name("박민준").email("park@test.com").role("USER").status("INACTIVE").build(),
                Users.builder().id(4L).name("최수아").email("choi@test.com").role("USER").status("ACTIVE").build(),
                Users.builder().id(5L).name("홍길동").email("hong@test.com").role("USER").status("ACTIVE").build()
        );

        // Send the whole list to HTML
        // In HTML, use th:each="${users}" to loop over it
        model.addAttribute("users", userList);

        // → templates/learn/users.html
        return "learn/users";
    }

    // ============================================================
    // STEP 3: Conditions (th:if / th:switch)
    // URL: http://localhost:8080/learn/condition
    // ============================================================
    @GetMapping("/condition")
    public String condition(Model model) {
        // --------------------------------------------------------
        // Try changing these values to see different results:
        //   userRole  → "ADMIN" or "USER"
        //   loginStatus → "ACTIVE", "INACTIVE", "SUSPENDED"
        //   point     → any number (above/below 1000)
        // --------------------------------------------------------
        model.addAttribute("userRole", "ADMIN");       // controls th:if in HTML
        model.addAttribute("loginStatus", "ACTIVE");   // controls th:switch in HTML
        model.addAttribute("point", 1500);             // controls number comparison

        // → templates/learn/conditions.html
        return "learn/conditions";
    }

    // ============================================================
    // STEP 5: Fragments concept page
    // URL: http://localhost:8080/learn/layout
    // ============================================================
    @GetMapping("/layout")
    public String layout() {
        // No model data needed — this page just demonstrates fragment syntax.
        // The navbar and footer are pulled in via th:replace.
        return "learn/layout";
    }

    // ============================================================
    // STEP 6: Real page using shared navbar + footer fragments
    // URL: http://localhost:8080/learn/layout-page
    // ============================================================
    @GetMapping("/layout-page")
    public String layoutPage(Model model) {
        // Same user list from Step 2
        List<Users> userList = List.of(
                Users.builder().id(1L).name("김철수").email("kim@test.com").role("ADMIN").status("ACTIVE").build(),
                Users.builder().id(2L).name("이영희").email("lee@test.com").role("USER").status("ACTIVE").build(),
                Users.builder().id(3L).name("박민준").email("park@test.com").role("USER").status("INACTIVE").build(),
                Users.builder().id(4L).name("최수아").email("choi@test.com").role("USER").status("ACTIVE").build(),
                Users.builder().id(5L).name("홍길동").email("hong@test.com").role("USER").status("ACTIVE").build()
        );

        model.addAttribute("pageTitle", "Step 6 - Layout Page (All Concepts Combined)");
        model.addAttribute("users", userList);

        // Compute some summary stats in Java — show them as stat cards in HTML
        long activeCount = userList.stream().filter(u -> "ACTIVE".equals(u.getStatus())).count();
        long adminCount  = userList.stream().filter(u -> "ADMIN".equals(u.getRole())).count();

        model.addAttribute("totalUsers", userList.size());
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("adminCount", adminCount);

        return "learn/layout-page";
    }

    // ============================================================
    // STEP 4: URL Expressions (@{})
    // URL: http://localhost:8080/learn/urls
    // ============================================================
    @GetMapping("/urls")
    public String url(Model model) {
        // --------------------------------------------------------
        // These values are used inside @{} URL expressions in HTML
        // Example: th:href="@{/users/{id}(id=${userId})}"
        //   → generates: /users/123
        // --------------------------------------------------------
        model.addAttribute("userId", 123);                 // used in path variable
        model.addAttribute("searchQuery", "Spring Boot");  // used in query param

        // → templates/learn/urls.html
        return "learn/urls";
    }
}