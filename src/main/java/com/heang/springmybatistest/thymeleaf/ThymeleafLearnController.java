package com.heang.springmybatistest.thymeleaf;

import com.heang.springmybatistest.model.Users;
import com.heang.springmybatistest.thymeleaf.domain.Address;
import com.heang.springmybatistest.thymeleaf.domain.Department;
import com.heang.springmybatistest.thymeleaf.domain.Employee;
import com.heang.springmybatistest.thymeleaf.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    // PRACTICE INDEX — Dashboard of all lesson pages
    // URL: http://localhost:8080/learn
    // ============================================================

    @GetMapping("")
    public String index() {
        return "learn/index";
    }

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
        model.addAttribute("hasError", false);                     // boolean
        model.addAttribute("userId", 7);

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
        // SECTION 1 — th:if / th:unless (basic)
        // HTML uses: th:if="${userRole == 'ADMIN'}"
        //            th:unless="${userRole == 'ADMIN'}"
        //
        // Try: change to "USER" → the "regular user" line appears instead
        // --------------------------------------------------------
        model.addAttribute("userRole", "ADMIN");

        // --------------------------------------------------------
        // SECTION 2 — th:switch / th:case
        // HTML uses: th:switch="${loginStatus}"
        //            th:case="'ACTIVE'" / th:case="'INACTIVE'" / th:case="*"
        //
        // Try: "ACTIVE" / "INACTIVE" / "SUSPENDED" / "DELETED" (hits default)
        // --------------------------------------------------------
        model.addAttribute("loginStatus", "ACTIVE");

        // --------------------------------------------------------
        // SECTION 3 — Number comparison
        // HTML uses: th:if="${point >= 1000}"  → VIP member
        //            th:if="${point < 1000}"   → Regular member
        //
        // Try: change to 500 → "Regular member" appears
        // --------------------------------------------------------
        model.addAttribute("point", 1500);

        // --------------------------------------------------------
        // PRACTICE A — Login / Logout button
        // HTML uses: th:if="${isLoggedIn}"     → shows logout button
        //            th:unless="${isLoggedIn}" → shows login button
        //
        // Try: change to false → Login button appears instead
        // --------------------------------------------------------
        model.addAttribute("isLoggedIn", true);

        // --------------------------------------------------------
        // PRACTICE B — Nested conditions (cart inside login check)
        // HTML uses: outer th:if="${isLoggedIn}"
        //            inner th:if="${cartCount == 0}" → empty message
        //            inner th:if="${cartCount > 0}"  → item count
        //
        // Try: change to 3 → "Items in cart: 3" appears
        // Try: change isLoggedIn to false → entire cart block disappears
        // --------------------------------------------------------
        model.addAttribute("cartCount", 0);

        // --------------------------------------------------------
        // PRACTICE C — Grade badge (th:switch)
        // HTML uses: th:switch="${userGrade}"
        //            th:case="'VIP'" / th:case="'GOLD'" / th:case="*"
        //
        // Try: "VIP" / "GOLD" / "SILVER" / "BRONZE" (hits default → Regular)
        // --------------------------------------------------------
        model.addAttribute("userGrade", "GOLD");

        // --------------------------------------------------------
        // PRACTICE D — AND / OR / NOT operators
        // HTML uses: th:if="${isLoggedIn and hasNotification}"
        //            th:if="${userGrade == 'VIP' or userGrade == 'GOLD'}"
        //            th:if="${not isLoggedIn}"
        //
        // Try: change hasNotification to false → notification message disappears
        // Try: change userGrade to "SILVER" → premium message disappears
        // --------------------------------------------------------
        model.addAttribute("hasNotification", true);
        model.addAttribute("notificationCount", 3);

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
        long adminCount = userList.stream().filter(u -> "ADMIN".equals(u.getRole())).count();

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

    @GetMapping("/profile")
    public String showProfile(Model model) {
        // --------------------------------------------------------
        // Example of passing a complex object (User) to HTML
        // In a real app, this would come from the database:
        //   User user = userService.findById(123);
        // --------------------------------------------------------
        User user = new User();
        user.setId(1L);
        user.setName("히엉");
        user.setEmail("heang@gmail");
        user.setAge(23);
        user.setPhone("010-1234-5678");
        user.setBio("Spring Boot를 좋아하는 개발자입니다.");
        user.setActive(true);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "User Profile Information");
        return "profile";
    }

    @GetMapping("/users/{id}/edit")
    public String editProfile(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "learn/hello"; // reuse any existing template for now
    }


    @GetMapping("/inputs")
    public String inputs(Model model) {

        model.addAttribute("username", "Heang");
        model.addAttribute("email", "heang@gmail.com");
        model.addAttribute("isReadOnly", true);
        model.addAttribute("hint", "이금을 입력하세요.");


        return "learn/inputs";
    }


    @GetMapping("/attr")
    public String attr(Model model) {

        model.addAttribute("productId", 42);
        model.addAttribute("productName", "무선 마우스");
        model.addAttribute("imageUrl", "https://placehold.co/200x150");
        model.addAttribute("tooltipText", "클릭하면 상품 상세페이지로 이동합니다");

        return "learn/attr";
    }

    //    For display data in thymeleaf
    @GetMapping("/text")
    public String text(Model model) {
        model.addAttribute("username", "Hen Heang12345");
        model.addAttribute("age", 24);
        model.addAttribute("isActive", true);
        return "learn/text";
    }

    // ============================================================
    // LEVEL 2.1: Nested Objects
    // URL: http://localhost:8080/learn/employee
    // ============================================================
    @GetMapping("/employee")
    public String employee(Model model) {

        // --------------------------------------------------------
        // Build the nested objects from the inside out.
        // Address and Department are built first,
        // then passed INTO the Employee object.
        // --------------------------------------------------------

        // Step 1 — build Address (innermost)
        Address address = Address.builder()
                .city("Seoul")
                .district("Gangnam-gu")
                .zipCode("06000")
                .build();

        // Step 2 — build Department (innermost)
        Department department = Department.builder()
                .id(1L)
                .name("Engineering")
                .location("Seoul HQ")
                .headCount(12)
                .build();

        // Step 3 — build Employee, passing department and address into it
        // Now employee.department and employee.address are NOT null
        Employee employee = Employee.builder()
                .id(1L)
                .name("Hen Heang")
                .position("Backend Developer")
                .salary(4500000)
                .active(true)
                .department(department)    // nested object
                .address(address)          // nested object
                .build();

        // --------------------------------------------------------
        // Send to HTML.
        // In HTML: ${employee.name}              → "Hen Heang"
        //          ${employee.department.name}   → "Engineering"
        //          ${employee.address.city}      → "Seoul"
        // --------------------------------------------------------
        model.addAttribute("employee", employee);

        // Also add a second employee with NULL department — to practice safe navigation
        Employee newEmployee = Employee.builder()
                .id(2L)
                .name("Kim Chulsoo")
                .position("Intern")
                .salary(null)
                .active(false)
                .department(null)    // intentionally null — to practice ?.
                .address(null)       // intentionally null — to practice ?:
                .build();

        model.addAttribute("newEmployee", newEmployee);

        return "learn/employee";
    }

    // ============================================================
    // LEVEL 2.2: Maps
    // URL: http://localhost:8080/learn/map
    // ============================================================
    @GetMapping("/map")
    public String map(Model model) {

        // --------------------------------------------------------
        // MAP 1 — Simple Map<String, Object>
        //
        // Map = a collection of KEY → VALUE pairs.
        // Key   = always a String (the label)
        // Value = can be anything: String, Integer, Boolean, etc.
        //
        // LinkedHashMap is used instead of HashMap
        // because LinkedHashMap KEEPS the order you inserted.
        // HashMap does NOT guarantee order — your rows would appear randomly.
        // --------------------------------------------------------
        Map<String, Object> userStats = new LinkedHashMap<>();
        userStats.put("Total Users",    120);   // String key → Integer value
        userStats.put("Active",          95);
        userStats.put("Inactive",        25);
        userStats.put("Admin",            3);
        userStats.put("Last Updated", "2026-04-14");  // String key → String value

        model.addAttribute("userStats", userStats);

        // --------------------------------------------------------
        // MAP 2 — Map<String, String>
        // A simple label → description map.
        // Used for showing a "legend" or "info panel" in Korean admin pages.
        // --------------------------------------------------------
        Map<String, String> systemInfo = new LinkedHashMap<>();
        systemInfo.put("Server",      "Railway Cloud");
        systemInfo.put("Database",    "PostgreSQL 15");
        systemInfo.put("Framework",   "Spring Boot 4.0");
        systemInfo.put("View Engine", "Thymeleaf");
        systemInfo.put("Status",      "Running");

        model.addAttribute("systemInfo", systemInfo);

        // --------------------------------------------------------
        // MAP 3 — Map<String, Object> with mixed value types
        // In real Korean projects, a summary card often uses a Map like this.
        // --------------------------------------------------------
        Map<String, Object> employeeSummary = new LinkedHashMap<>();
        employeeSummary.put("Name",          "Hen Heang");
        employeeSummary.put("Department",    "Engineering");
        employeeSummary.put("Position",      "Backend Developer");
        employeeSummary.put("Salary",        4500000);
        employeeSummary.put("Active",        true);
        employeeSummary.put("Years",         2);

        model.addAttribute("employeeSummary", employeeSummary);

        // → templates/learn/map.html
        return "learn/map";
    }
}