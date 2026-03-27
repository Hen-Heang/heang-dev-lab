# Thymeleaf Complete Practice Guide
## From Basic to Advanced (Korean Workplace Ready)

> **Your Goal**: Master Thymeleaf for Spring Boot projects commonly used in Korean companies

---

## 📚 Table of Contents

1. [Level 1: Basic Syntax](#level-1-basic-syntax)
2. [Level 2: Working with Objects](#level-2-working-with-objects)
3. [Level 3: Forms & Validation](#level-3-forms--validation)
4. [Level 4: Lists & Tables](#level-4-lists--tables)
5. [Level 5: Fragments & Layouts](#level-5-fragments--layouts)
6. [Level 6: AJAX Integration](#level-6-ajax-integration)
7. [Level 7: Security Integration](#level-7-security-integration)
8. [Level 8: Advanced Patterns](#level-8-advanced-patterns)
9. [Common Mistakes to Avoid](#common-mistakes-to-avoid)
10. [Quick Reference Cheat Sheet](#quick-reference-cheat-sheet)

---

## Level 1: Basic Syntax

### 1.1 Essential Attributes You Must Know

```html
<!-- MOST COMMONLY USED ATTRIBUTES -->

<!-- 1. th:text - Display text (escapes HTML) -->
<span th:text="${user.name}">Default Name</span>

<!-- 2. th:utext - Display text (allows HTML) -->
<div th:utext="${article.content}">Default Content</div>

<!-- 3. th:value - Set input value -->
<input type="text" th:value="${user.email}" />

<!-- 4. th:href - Create links -->
<a th:href="@{/users/{id}(id=${user.id})}">View User</a>

<!-- 5. th:src - Image source -->
<img th:src="@{/images/logo.png}" />

<!-- 6. th:class - CSS class -->
<div th:class="${isActive} ? 'active' : 'inactive'">Status</div>

<!-- 7. th:classappend - Add CSS class -->
<div class="base" th:classappend="${hasError} ? 'error'">Content</div>

<!-- 8. th:style - Inline style -->
<div th:style="'color: ' + ${textColor}">Styled Text</div>

<!-- 9. th:attr - Set any attribute -->
<input th:attr="placeholder=${placeholderText}" />

<!-- 10. th:id - Set element ID -->
<div th:id="'user-' + ${user.id}">User Card</div>
```

### 1.2 Understanding the 5 Expression Types

```html
<!-- ========================================== -->
<!-- 1. ${...} Variable Expression - MOST USED -->
<!-- ========================================== -->
<!-- Access data from Model -->
<p th:text="${userName}"></p>
<p th:text="${user.address.city}"></p>

<!-- ========================================== -->
<!-- 2. *{...} Selection Expression -->
<!-- ========================================== -->
<!-- Used with th:object to avoid repeating object name -->
<div th:object="${user}">
    <p th:text="*{name}"></p>      <!-- Same as ${user.name} -->
    <p th:text="*{email}"></p>     <!-- Same as ${user.email} -->
    <p th:text="*{phone}"></p>     <!-- Same as ${user.phone} -->
</div>

<!-- ========================================== -->
<!-- 3. #{...} Message Expression (i18n) -->
<!-- ========================================== -->
<!-- Read from messages.properties -->
<p th:text="#{welcome.message}"></p>
<p th:text="#{user.greeting(${user.name})}"></p>  <!-- With parameter -->

<!-- messages.properties -->
<!-- welcome.message=Welcome to our site -->
<!-- user.greeting=Hello, {0}! -->

<!-- ========================================== -->
<!-- 4. @{...} Link Expression - VERY IMPORTANT -->
<!-- ========================================== -->
<!-- Context-path aware URLs -->
<a th:href="@{/users}">User List</a>
<a th:href="@{/users/{id}(id=${user.id})}">View User</a>
<a th:href="@{/search(keyword=${keyword}, page=${page})}">Search</a>

<!-- Output: /contextPath/users/123 -->
<!-- Output: /contextPath/search?keyword=java&page=1 -->

<!-- ========================================== -->
<!-- 5. ~{...} Fragment Expression -->
<!-- ========================================== -->
<div th:replace="~{fragments/header :: navbar}"></div>
<div th:insert="~{fragments/footer :: copyright}"></div>
```

### 1.3 Practice Exercise - Level 1

**Task**: Create a simple user profile page

```java
// User Model (MyBatis - plain POJO, no JPA annotations needed)
public class User {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private int age;
    private boolean active;
    private String nickname;
    private String bio;
    private String role;
    private String firstName;
    private String lastName;
    private Department department;

    // Getters and Setters
}
```

```java
// Controller
@GetMapping("/profile")
public String showProfile(Model model) {
    User user = new User();
    user.setId(1L);
    user.setName("김철수");
    user.setEmail("kim@example.com");
    user.setAge(28);
    user.setActive(true);
    
    model.addAttribute("user", user);
    model.addAttribute("pageTitle", "User Profile");
    return "profile";
}
```

**Your Task**: Create `profile.html` that displays:
- Page title
- User name, email, age
- Show "Active" badge if user is active
- Link to edit page: `/users/1/edit`

---

## Level 2: Working with Objects

### 2.1 Nested Objects

```java
// Domain classes commonly used in Korean companies
public class Employee {
    private Long id;
    private String name;
    private Department department;  // Nested object
    private Address address;        // Nested object
    // getters, setters
}

public class Department {
    private Long id;
    private String name;
    private Employee manager;
}
```

```html
<!-- Accessing nested properties -->
<p th:text="${employee.department.name}">Department</p>
<p th:text="${employee.department.manager.name}">Manager Name</p>
<p th:text="${employee.address.city}">City</p>

<!-- Safe navigation (avoid NullPointerException) -->
<p th:text="${employee.department?.name}">Department</p>
<p th:text="${employee.department?.manager?.name ?: 'No Manager'}">Manager</p>
```

### 2.2 Working with Maps

```java
// Controller
Map<String, Object> userInfo = new HashMap<>();
userInfo.put("name", "홍길동");
userInfo.put("role", "ADMIN");
userInfo.put("permissions", Arrays.asList("READ", "WRITE", "DELETE"));
model.addAttribute("userInfo", userInfo);
```

```html
<!-- Accessing Map values -->
<p th:text="${userInfo['name']}">Name</p>
<p th:text="${userInfo.role}">Role</p>

<!-- Iterating over Map -->
<table>
    <tr th:each="entry : ${userInfo}">
        <td th:text="${entry.key}">Key</td>
        <td th:text="${entry.value}">Value</td>
    </tr>
</table>
```

### 2.3 Elvis Operator & Default Values

```html
<!-- Elvis Operator ?: -->
<p th:text="${user.nickname} ?: 'No Nickname'">Nickname</p>

<!-- Default value with th:default is NOT available, use ?: -->
<input type="text" th:value="${searchKeyword} ?: ''" />

<!-- Conditional default -->
<p th:text="${user.age} != null ? ${user.age} : 'Unknown'">Age</p>

<!-- Using #strings for empty check -->
<p th:text="${#strings.isEmpty(user.bio)} ? 'No bio available' : ${user.bio}">Bio</p>
```

---

## Level 3: Forms & Validation (VERY IMPORTANT for Korean Companies)

### 3.1 Basic Form Binding

```java
// DTO (Data Transfer Object) - 한국 회사에서 많이 사용
public class MemberFormDTO {
    @NotBlank(message = "이름은 필수입니다")
    private String name;
    
    @Email(message = "유효한 이메일을 입력하세요")
    private String email;
    
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    private String password;
    
    @NotNull(message = "부서를 선택하세요")
    private Long departmentId;
    
    // getters, setters
}
```

```java
// Controller - GET (show form)
@GetMapping("/members/new")
public String showCreateForm(Model model) {
    model.addAttribute("memberForm", new MemberFormDTO());
    model.addAttribute("departments", departmentService.findAll());
    return "members/create";
}

// Controller - POST (process form)
@PostMapping("/members/new")
public String createMember(
        @Valid @ModelAttribute("memberForm") MemberFormDTO form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
    
    // Validation error check
    if (bindingResult.hasErrors()) {
        model.addAttribute("departments", departmentService.findAll());
        return "members/create";  // Return to form with errors
    }
    
    memberService.create(form);
    redirectAttributes.addFlashAttribute("message", "회원이 등록되었습니다");
    return "redirect:/members";
}
```

```html
<!-- members/create.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>회원 등록</title>
</head>
<body>
    <h1>회원 등록</h1>
    
    <!-- Display global message -->
    <div th:if="${message}" class="alert alert-success" th:text="${message}"></div>
    
    <!-- Form with object binding -->
    <form th:action="@{/members/new}" th:object="${memberForm}" method="post">
        
        <!-- Name field with validation error -->
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" th:field="*{name}" 
                   th:classappend="${#fields.hasErrors('name')} ? 'is-invalid'" />
            <span th:if="${#fields.hasErrors('name')}" 
                  th:errors="*{name}" class="error-message"></span>
        </div>
        
        <!-- Email field -->
        <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" th:field="*{email}"
                   th:classappend="${#fields.hasErrors('email')} ? 'is-invalid'" />
            <span th:if="${#fields.hasErrors('email')}" 
                  th:errors="*{email}" class="error-message"></span>
        </div>
        
        <!-- Password field -->
        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="password" id="password" th:field="*{password}"
                   th:classappend="${#fields.hasErrors('password')} ? 'is-invalid'" />
            <span th:if="${#fields.hasErrors('password')}" 
                  th:errors="*{password}" class="error-message"></span>
        </div>
        
        <!-- Select dropdown -->
        <div class="form-group">
            <label for="departmentId">부서</label>
            <select id="departmentId" th:field="*{departmentId}"
                    th:classappend="${#fields.hasErrors('departmentId')} ? 'is-invalid'">
                <option value="">-- 부서 선택 --</option>
                <option th:each="dept : ${departments}"
                        th:value="${dept.id}"
                        th:text="${dept.name}">Department</option>
            </select>
            <span th:if="${#fields.hasErrors('departmentId')}" 
                  th:errors="*{departmentId}" class="error-message"></span>
        </div>
        
        <button type="submit">등록</button>
        <a th:href="@{/members}">취소</a>
    </form>
</body>
</html>
```

### 3.2 Different Input Types

```html
<!-- Text Input -->
<input type="text" th:field="*{name}" />

<!-- Number Input -->
<input type="number" th:field="*{age}" min="0" max="150" />

<!-- Date Input (Java 8 LocalDate) -->
<input type="date" th:field="*{birthDate}" />

<!-- Textarea -->
<textarea th:field="*{description}" rows="5"></textarea>

<!-- Checkbox (single) -->
<input type="checkbox" th:field="*{agreeTerms}" />
<label>약관에 동의합니다</label>

<!-- Checkbox (multiple - for List<String>) -->
<div th:each="role : ${allRoles}">
    <input type="checkbox" th:field="*{roles}" th:value="${role}" />
    <label th:text="${role}">Role</label>
</div>

<!-- Radio buttons -->
<div th:each="gender : ${genders}">
    <input type="radio" th:field="*{gender}" th:value="${gender.code}" />
    <label th:text="${gender.name}">Gender</label>
</div>

<!-- Hidden field -->
<input type="hidden" th:field="*{id}" />

<!-- File upload -->
<input type="file" name="profileImage" />
```

### 3.3 Edit Form (Pre-populated)

```java
// Controller - GET edit form
@GetMapping("/members/{id}/edit")
public String showEditForm(@PathVariable Long id, Model model) {
    Member member = memberService.findById(id);
    
    // Convert entity to DTO
    MemberFormDTO form = new MemberFormDTO();
    form.setId(member.getId());
    form.setName(member.getName());
    form.setEmail(member.getEmail());
    form.setDepartmentId(member.getDepartment().getId());
    
    model.addAttribute("memberForm", form);
    model.addAttribute("departments", departmentService.findAll());
    return "members/edit";
}

// Controller - POST update
@PostMapping("/members/{id}/edit")
public String updateMember(
        @PathVariable Long id,
        @Valid @ModelAttribute("memberForm") MemberFormDTO form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
    
    if (bindingResult.hasErrors()) {
        model.addAttribute("departments", departmentService.findAll());
        return "members/edit";
    }
    
    memberService.update(id, form);
    redirectAttributes.addFlashAttribute("message", "수정되었습니다");
    return "redirect:/members/" + id;
}
```

---

## Level 4: Lists & Tables (VERY COMMON in Korean Admin Panels)

### 4.1 Basic Table with Iteration

```java
// Controller
@GetMapping("/members")
public String listMembers(Model model) {
    List<Member> members = memberService.findAll();
    model.addAttribute("members", members);
    return "members/list";
}
```

```html
<table class="table">
    <thead>
        <tr>
            <th>번호</th>
            <th>이름</th>
            <th>이메일</th>
            <th>부서</th>
            <th>등록일</th>
            <th>관리</th>
        </tr>
    </thead>
    <tbody>
        <!-- th:each with status variable -->
        <tr th:each="member, status : ${members}">
            <td th:text="${status.count}">1</td>  <!-- 1-based index -->
            <td th:text="${member.name}">Name</td>
            <td th:text="${member.email}">Email</td>
            <td th:text="${member.department?.name}">Dept</td>
            <td th:text="${#temporals.format(member.createdAt, 'yyyy-MM-dd')}">Date</td>
            <td>
                <a th:href="@{/members/{id}(id=${member.id})}">상세</a>
                <a th:href="@{/members/{id}/edit(id=${member.id})}">수정</a>
                <button type="button" 
                        th:onclick="'deleteMember(' + ${member.id} + ')'"
                        class="btn-delete">삭제</button>
            </td>
        </tr>
        
        <!-- Empty state -->
        <tr th:if="${#lists.isEmpty(members)}">
            <td colspan="6" class="text-center">등록된 회원이 없습니다</td>
        </tr>
    </tbody>
</table>
```

### 4.2 Status Variable Properties

```html
<!-- th:each="item, status : ${items}" -->
<!-- status provides these properties: -->

<tr th:each="item, stat : ${items}">
    <!-- stat.index: 0-based index (0, 1, 2, ...) -->
    <td th:text="${stat.index}">Index</td>
    
    <!-- stat.count: 1-based count (1, 2, 3, ...) -->
    <td th:text="${stat.count}">Count</td>
    
    <!-- stat.size: total number of items -->
    <td th:text="${stat.size}">Total</td>
    
    <!-- stat.current: current item (same as ${item}) -->
    <td th:text="${stat.current.name}">Current</td>
    
    <!-- stat.even: true if even row (0, 2, 4, ...) -->
    <!-- stat.odd: true if odd row (1, 3, 5, ...) -->
    <tr th:class="${stat.odd} ? 'odd-row' : 'even-row'">
    
    <!-- stat.first: true if first item -->
    <!-- stat.last: true if last item -->
    <td th:if="${stat.first}">FIRST ITEM</td>
    <td th:if="${stat.last}">LAST ITEM</td>
</tr>
```

### 4.3 Pagination (Common Pattern in Korean Projects)

```java
// Controller with pagination
@GetMapping("/members")
public String listMembers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword,
        Model model) {
    
    Pageable pageable = PageRequest.of(page, size);
    Page<Member> memberPage;
    
    if (keyword != null && !keyword.isEmpty()) {
        memberPage = memberService.searchByName(keyword, pageable);
    } else {
        memberPage = memberService.findAll(pageable);
    }
    
    model.addAttribute("members", memberPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", memberPage.getTotalPages());
    model.addAttribute("totalItems", memberPage.getTotalElements());
    model.addAttribute("keyword", keyword);
    
    return "members/list";
}
```

```html
<!-- Search form -->
<form th:action="@{/members}" method="get" class="search-form">
    <input type="text" name="keyword" th:value="${keyword}" placeholder="이름 검색" />
    <button type="submit">검색</button>
    <a th:href="@{/members}">초기화</a>
</form>

<!-- Results count -->
<p>총 <strong th:text="${totalItems}">0</strong>건</p>

<!-- Table here... -->

<!-- Pagination -->
<nav class="pagination">
    <!-- Previous button -->
    <a th:if="${currentPage > 0}"
       th:href="@{/members(page=${currentPage - 1}, keyword=${keyword})}"
       class="page-link">이전</a>
    <span th:if="${currentPage == 0}" class="page-link disabled">이전</span>
    
    <!-- Page numbers -->
    <span th:each="i : ${#numbers.sequence(0, totalPages - 1)}">
        <a th:if="${i != currentPage}"
           th:href="@{/members(page=${i}, keyword=${keyword})}"
           th:text="${i + 1}"
           class="page-link">1</a>
        <span th:if="${i == currentPage}" 
              th:text="${i + 1}" 
              class="page-link active">1</span>
    </span>
    
    <!-- Next button -->
    <a th:if="${currentPage < totalPages - 1}"
       th:href="@{/members(page=${currentPage + 1}, keyword=${keyword})}"
       class="page-link">다음</a>
    <span th:if="${currentPage >= totalPages - 1}" class="page-link disabled">다음</span>
</nav>
```

### 4.4 Advanced Pagination (Korean Style - 1 2 3 4 5 ... 10)

```html
<!-- Advanced pagination with page range -->
<nav th:with="
    startPage=${(currentPage / 5) * 5},
    endPage=${startPage + 4 < totalPages ? startPage + 4 : totalPages - 1}
">
    <!-- First page -->
    <a th:if="${currentPage > 0}"
       th:href="@{/members(page=0, keyword=${keyword})}">처음</a>
    
    <!-- Previous 5 pages -->
    <a th:if="${startPage > 0}"
       th:href="@{/members(page=${startPage - 5}, keyword=${keyword})}">«</a>
    
    <!-- Page numbers (show 5 at a time) -->
    <th:block th:each="i : ${#numbers.sequence(startPage, endPage)}">
        <a th:unless="${i == currentPage}"
           th:href="@{/members(page=${i}, keyword=${keyword})}"
           th:text="${i + 1}">1</a>
        <strong th:if="${i == currentPage}" th:text="${i + 1}">1</strong>
    </th:block>
    
    <!-- Next 5 pages -->
    <a th:if="${endPage < totalPages - 1}"
       th:href="@{/members(page=${endPage + 1}, keyword=${keyword})}">»</a>
    
    <!-- Last page -->
    <a th:if="${currentPage < totalPages - 1}"
       th:href="@{/members(page=${totalPages - 1}, keyword=${keyword})}">마지막</a>
</nav>
```

---

## Level 5: Fragments & Layouts

### 5.1 Creating Reusable Fragments

```html
<!-- fragments/common.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<!-- Header fragment -->
<header th:fragment="header">
    <nav class="navbar">
        <a th:href="@{/}">홈</a>
        <a th:href="@{/members}">회원관리</a>
        <a th:href="@{/products}">상품관리</a>
    </nav>
</header>

<!-- Footer fragment -->
<footer th:fragment="footer">
    <p>&copy; 2024 My Company. All rights reserved.</p>
</footer>

<!-- Sidebar fragment with parameter -->
<aside th:fragment="sidebar(menuItems)">
    <ul class="sidebar-menu">
        <li th:each="item : ${menuItems}">
            <a th:href="${item.url}" th:text="${item.name}">Menu</a>
        </li>
    </ul>
</aside>

<!-- Alert fragment with parameter -->
<div th:fragment="alert(type, message)" 
     th:class="'alert alert-' + ${type}">
    <span th:text="${message}">Message</span>
    <button type="button" class="close">&times;</button>
</div>

</html>
```

### 5.2 Using Fragments

```html
<!-- Using th:replace (replaces the entire tag) -->
<div th:replace="~{fragments/common :: header}"></div>

<!-- Using th:insert (inserts inside the tag) -->
<div th:insert="~{fragments/common :: footer}"></div>

<!-- Using fragment with parameters -->
<div th:replace="~{fragments/common :: alert('success', '저장되었습니다')}"></div>
<div th:replace="~{fragments/common :: alert('error', ${errorMessage})}"></div>

<!-- Conditional fragment -->
<div th:replace="${showSidebar} ? ~{fragments/common :: sidebar(${menuItems})}"></div>
```

### 5.3 Layout System (Decorator Pattern)

```html
<!-- layouts/default.html - Base layout -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
    <meta charset="UTF-8">
    <title layout:title-pattern="$CONTENT_TITLE - $LAYOUT_TITLE">My App</title>
    
    <!-- Common CSS -->
    <link rel="stylesheet" th:href="@{/css/common.css}" />
    
    <!-- Page-specific CSS will be inserted here -->
    <th:block layout:fragment="css"></th:block>
</head>
<body>
    <!-- Header -->
    <header th:replace="~{fragments/common :: header}"></header>
    
    <!-- Main content - child pages replace this -->
    <main class="container">
        <div layout:fragment="content">
            Default content
        </div>
    </main>
    
    <!-- Footer -->
    <footer th:replace="~{fragments/common :: footer}"></footer>
    
    <!-- Common JS -->
    <script th:src="@{/js/common.js}"></script>
    
    <!-- Page-specific JS will be inserted here -->
    <th:block layout:fragment="scripts"></th:block>
</body>
</html>
```

```html
<!-- members/list.html - Uses the layout -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
      layout:decorate="~{layouts/default}">
<head>
    <title>회원 목록</title>
    
    <!-- Page-specific CSS -->
    <th:block layout:fragment="css">
        <link rel="stylesheet" th:href="@{/css/members.css}" />
    </th:block>
</head>
<body>
    <!-- Content that replaces layout:fragment="content" -->
    <div layout:fragment="content">
        <h1>회원 목록</h1>
        
        <!-- Table, pagination, etc. -->
        <table>
            <!-- ... -->
        </table>
    </div>
    
    <!-- Page-specific JavaScript -->
    <th:block layout:fragment="scripts">
        <script th:src="@{/js/members.js}"></script>
        <script th:inline="javascript">
            const currentPage = [[${currentPage}]];
        </script>
    </th:block>
</body>
</html>
```

### 5.4 Add Layout Dialect Dependency

```xml
<!-- pom.xml -->
<dependency>
    <groupId>nz.net.ultraq.thymeleaf</groupId>
    <artifactId>thymeleaf-layout-dialect</artifactId>
</dependency>
```

---

## Level 6: AJAX Integration (Modern Korean Projects)

### 6.1 Inline JavaScript

```html
<script th:inline="javascript">
    // Natural JavaScript with Thymeleaf
    
    // Simple variable
    const userName = [[${user.name}]];
    
    // Object (automatically serialized to JSON)
    const user = [[${user}]];
    
    // List
    const items = [[${items}]];
    
    // Escaped text (for strings with special chars)
    const message = /*[[${message}]]*/ "default message";
    
    // URL
    const apiUrl = /*[[@{/api/members}]]*/ "/api/members";
    
    // Context path
    const contextPath = /*[[@{/}]]*/ "/";
</script>
```

### 6.2 AJAX Delete Example

```html
<button type="button" 
        th:data-id="${member.id}"
        th:data-name="${member.name}"
        class="btn-delete"
        onclick="confirmDelete(this)">삭제</button>

<script th:inline="javascript">
    const deleteUrl = /*[[@{/api/members/}]]*/ "/api/members/";
    
    function confirmDelete(button) {
        const id = button.dataset.id;
        const name = button.dataset.name;
        
        if (confirm(name + '님을 삭제하시겠습니까?')) {
            fetch(deleteUrl + id, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (response.ok) {
                    alert('삭제되었습니다');
                    location.reload();
                } else {
                    alert('삭제 실패');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('오류가 발생했습니다');
            });
        }
    }
</script>
```

### 6.3 Form AJAX Submit

```html
<form id="memberForm" th:object="${memberForm}">
    <input type="text" th:field="*{name}" />
    <input type="email" th:field="*{email}" />
    <button type="button" onclick="submitForm()">저장</button>
</form>

<script th:inline="javascript">
    const saveUrl = /*[[@{/api/members}]]*/ "/api/members";
    
    function submitForm() {
        const form = document.getElementById('memberForm');
        const formData = new FormData(form);
        const data = Object.fromEntries(formData);
        
        fetch(saveUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                alert('저장되었습니다');
                window.location.href = /*[[@{/members}]]*/ "/members";
            } else {
                alert(result.message);
            }
        });
    }
</script>
```

---

## Level 7: Security Integration

### 7.1 Spring Security with Thymeleaf

```xml
<!-- Add dependency -->
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>
```

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<body>
    
    <!-- Show only if authenticated -->
    <div sec:authorize="isAuthenticated()">
        <p>환영합니다, <span sec:authentication="name">User</span>님</p>
        <a th:href="@{/logout}">로그아웃</a>
    </div>
    
    <!-- Show only if NOT authenticated -->
    <div sec:authorize="isAnonymous()">
        <a th:href="@{/login}">로그인</a>
        <a th:href="@{/register}">회원가입</a>
    </div>
    
    <!-- Role-based content -->
    <div sec:authorize="hasRole('ADMIN')">
        <a th:href="@{/admin}">관리자 메뉴</a>
    </div>
    
    <div sec:authorize="hasAnyRole('ADMIN', 'MANAGER')">
        <a th:href="@{/reports}">보고서</a>
    </div>
    
    <!-- Authority-based (more specific) -->
    <div sec:authorize="hasAuthority('MEMBER_DELETE')">
        <button class="btn-delete">삭제</button>
    </div>
    
    <!-- Access authentication details -->
    <p>Username: <span sec:authentication="principal.username">user</span></p>
    <p>Authorities: <span sec:authentication="authorities">roles</span></p>
    
</body>
</html>
```

### 7.2 Login Form with CSRF

```html
<form th:action="@{/login}" method="post">
    <!-- CSRF token (automatically added by Thymeleaf) -->
    
    <div class="form-group">
        <label for="username">아이디</label>
        <input type="text" id="username" name="username" required />
    </div>
    
    <div class="form-group">
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" required />
    </div>
    
    <div class="form-group">
        <input type="checkbox" id="remember-me" name="remember-me" />
        <label for="remember-me">로그인 상태 유지</label>
    </div>
    
    <!-- Error message -->
    <div th:if="${param.error}" class="alert alert-danger">
        아이디 또는 비밀번호가 올바르지 않습니다.
    </div>
    
    <!-- Logout message -->
    <div th:if="${param.logout}" class="alert alert-info">
        로그아웃 되었습니다.
    </div>
    
    <button type="submit">로그인</button>
</form>
```

---

## Level 8: Advanced Patterns

### 8.1 Local Variables (th:with)

```html
<!-- Define local variables -->
<div th:with="fullName=${user.firstName + ' ' + user.lastName}">
    <p th:text="${fullName}">Full Name</p>
</div>

<!-- Multiple variables -->
<div th:with="total=${price * quantity}, tax=${total * 0.1}">
    <p>Total: <span th:text="${total}">0</span></p>
    <p>Tax: <span th:text="${tax}">0</span></p>
    <p>Grand Total: <span th:text="${total + tax}">0</span></p>
</div>

<!-- Useful for complex calculations -->
<tr th:each="order : ${orders}"
    th:with="
        itemCount=${#lists.size(order.items)},
        totalPrice=${#aggregates.sum(order.items.![price])},
        discount=${order.coupon != null ? order.coupon.amount : 0}
    ">
    <td th:text="${itemCount}">0</td>
    <td th:text="${totalPrice - discount}">0</td>
</tr>
```

### 8.2 Block Element (th:block)

```html
<!-- th:block is invisible in HTML output -->
<!-- Use for grouping without extra tags -->

<table>
    <th:block th:each="category : ${categories}">
        <!-- Category header row -->
        <tr class="category-header">
            <td colspan="3" th:text="${category.name}">Category</td>
        </tr>
        <!-- Products in category -->
        <tr th:each="product : ${category.products}">
            <td th:text="${product.name}">Product</td>
            <td th:text="${product.price}">0</td>
            <td th:text="${product.stock}">0</td>
        </tr>
    </th:block>
</table>
```

### 8.3 Dynamic Attributes

```html
<!-- Conditional attribute -->
<input type="text" 
       th:attr="disabled=${isReadOnly} ? 'disabled'" />

<!-- Better way: th:disabled -->
<input type="text" th:disabled="${isReadOnly}" />

<!-- Multiple boolean attributes -->
<input type="checkbox" 
       th:checked="${item.selected}"
       th:disabled="${item.locked}" />

<!-- Dynamic data attributes -->
<div th:attr="data-user-id=${user.id}, data-role=${user.role}">
    User Card
</div>

<!-- Or using th:data-* -->
<div th:data-user-id="${user.id}" th:data-role="${user.role}">
    User Card
</div>
```

### 8.4 Date/Time Formatting (Java 8+)

```html
<!-- For LocalDate, LocalDateTime, LocalTime -->
<!-- Use #temporals (not #dates) -->

<!-- Basic formatting -->
<p th:text="${#temporals.format(createdAt, 'yyyy-MM-dd')}">2024-01-01</p>
<p th:text="${#temporals.format(createdAt, 'yyyy-MM-dd HH:mm:ss')}">Date Time</p>

<!-- Korean style -->
<p th:text="${#temporals.format(createdAt, 'yyyy년 MM월 dd일')}">2024년 01월 01일</p>

<!-- Individual components -->
<p th:text="${#temporals.year(createdAt)}">2024</p>
<p th:text="${#temporals.month(createdAt)}">1</p>
<p th:text="${#temporals.day(createdAt)}">15</p>
<p th:text="${#temporals.dayOfWeek(createdAt)}">3</p>
<p th:text="${#temporals.dayOfWeekName(createdAt)}">Wednesday</p>

<!-- For old Date objects, use #dates -->
<p th:text="${#dates.format(oldDate, 'yyyy-MM-dd')}">2024-01-01</p>
```

### 8.5 Number Formatting

```html
<!-- Basic number formatting -->
<p th:text="${#numbers.formatInteger(count, 3)}">001</p>
<p th:text="${#numbers.formatDecimal(price, 3, 2)}">100.00</p>

<!-- With thousands separator -->
<p th:text="${#numbers.formatInteger(price, 1, 'COMMA')}">1,000,000</p>
<p th:text="${#numbers.formatDecimal(price, 1, 'COMMA', 2, 'POINT')}">1,000.00</p>

<!-- Currency (Korean Won) -->
<p th:text="'₩' + ${#numbers.formatInteger(price, 1, 'COMMA')}">₩10,000</p>

<!-- Percentage -->
<p th:text="${#numbers.formatPercent(ratio, 2, 2)}">50.00%</p>

<!-- Array/List operations -->
<p th:text="${#numbers.sequence(1, 10)}">1 to 10</p>
<p th:text="${#numbers.sequence(0, 10, 2)}">0, 2, 4, 6, 8, 10</p>
```

---

## Common Mistakes to Avoid

### ❌ Mistake 1: Using JSP syntax
```html
<!-- WRONG - JSP style -->
<%= user.name %>

<!-- CORRECT - Thymeleaf style -->
<span th:text="${user.name}">Name</span>
```

### ❌ Mistake 2: Forgetting namespace
```html
<!-- WRONG - th: won't work -->
<html>

<!-- CORRECT - add namespace -->
<html xmlns:th="http://www.thymeleaf.org">
```

### ❌ Mistake 3: Wrong form binding
```html
<!-- WRONG - using name attribute -->
<input type="text" name="userName" />

<!-- CORRECT - using th:field -->
<input type="text" th:field="*{userName}" />
```

### ❌ Mistake 4: Not handling null
```html
<!-- WRONG - will throw NullPointerException -->
<p th:text="${user.department.name}">Dept</p>

<!-- CORRECT - safe navigation -->
<p th:text="${user.department?.name}">Dept</p>
<p th:text="${user.department?.name ?: 'No Department'}">Dept</p>
```

### ❌ Mistake 5: Wrong link syntax
```html
<!-- WRONG -->
<a href="/users/1">View</a>

<!-- CORRECT - context-path aware -->
<a th:href="@{/users/{id}(id=${user.id})}">View</a>
```

### ❌ Mistake 6: Using #dates for LocalDate
```html
<!-- WRONG - for Java 8+ dates -->
<p th:text="${#dates.format(localDate, 'yyyy-MM-dd')}">Date</p>

<!-- CORRECT - use #temporals -->
<p th:text="${#temporals.format(localDate, 'yyyy-MM-dd')}">Date</p>
```

---

## Quick Reference Cheat Sheet

### Expression Types
| Expression | Syntax | Usage |
|------------|--------|-------|
| Variable | `${...}` | Access model data |
| Selection | `*{...}` | Access field of th:object |
| Message | `#{...}` | i18n messages |
| Link/URL | `@{...}` | Context-aware URLs |
| Fragment | `~{...}` | Include fragments |

### Most Used Attributes
| Attribute | Purpose | Example |
|-----------|---------|---------|
| `th:text` | Set text content | `th:text="${name}"` |
| `th:utext` | Set HTML content | `th:utext="${html}"` |
| `th:value` | Set input value | `th:value="${val}"` |
| `th:field` | Form field binding | `th:field="*{name}"` |
| `th:href` | Set URL | `th:href="@{/path}"` |
| `th:src` | Set image source | `th:src="@{/img.png}"` |
| `th:each` | Loop | `th:each="i : ${list}"` |
| `th:if` | Conditional | `th:if="${condition}"` |
| `th:unless` | Negative conditional | `th:unless="${cond}"` |
| `th:switch` | Switch statement | `th:switch="${val}"` |
| `th:case` | Case in switch | `th:case="'value'"` |
| `th:object` | Select object | `th:object="${user}"` |
| `th:with` | Local variable | `th:with="x=${val}"` |
| `th:class` | Set CSS class | `th:class="${cls}"` |
| `th:classappend` | Append CSS class | `th:classappend="active"` |
| `th:replace` | Replace with fragment | `th:replace="~{...}"` |
| `th:insert` | Insert fragment | `th:insert="~{...}"` |
| `th:inline` | Inline mode | `th:inline="javascript"` |

### Utility Objects
| Object | Purpose |
|--------|---------|
| `#strings` | String operations |
| `#numbers` | Number formatting |
| `#dates` | java.util.Date |
| `#temporals` | Java 8 date/time |
| `#lists` | List operations |
| `#arrays` | Array operations |
| `#maps` | Map operations |
| `#objects` | Object utilities |
| `#bools` | Boolean utilities |
| `#aggregates` | sum, avg |

### URL Patterns
```html
<!-- Basic -->
@{/path}                          → /context/path

<!-- With path variable -->
@{/users/{id}(id=${userId})}      → /context/users/123

<!-- With query params -->
@{/search(q=${keyword},page=1)}   → /context/search?q=java&page=1

<!-- Absolute URL -->
@{http://example.com}             → http://example.com
```

---

## Practice Projects

### Project 1: Employee Management System (Easy)
- Employee CRUD
- Department dropdown
- Search by name
- Pagination

### Project 2: Board/Notice System (Medium)
- Post CRUD with categories
- File attachment
- Comments
- View count
- Admin/User roles

### Project 3: E-commerce Admin Panel (Advanced)
- Product management
- Order management
- Dashboard with charts
- Role-based access
- Multi-language support

---

**Happy Coding! 화이팅! 🚀**
