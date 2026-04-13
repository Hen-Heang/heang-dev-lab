# Thymeleaf Practice Guide

---

## 1. What Is Thymeleaf?

Thymeleaf is a **Java template engine** — it takes an HTML file and fills it with real data from your Java code before sending it to the browser.

Think of it like a letter template:

```
Dear [NAME],                     <- template (HTML with placeholders)
Dear 김철수,                      <- result (filled by Thymeleaf)
```

In code:
```html
<!-- HTML template -->
<p th:text="${username}">placeholder</p>

<!-- What the browser sees after Thymeleaf fills it -->
<p>김철수</p>
```

The `th:` prefix = Thymeleaf's instruction. It reads data from Java (via `Model`) and injects it into HTML.

---

## 2. What Can Thymeleaf Do?

| Feature | Syntax | What it does |
|---|---|---|
| Display text | `th:text="${name}"` | Put a variable value inside a tag |
| Raw HTML | `th:utext="${html}"` | Render HTML tags (not escaped) |
| Condition | `th:if="${isAdmin}"` | Show element only if true |
| Condition (opposite) | `th:unless="${isAdmin}"` | Show element only if false |
| Switch | `th:switch` / `th:case` | Like Java switch statement |
| Loop | `th:each="user : ${users}"` | Repeat element for each item in a list |
| Link/URL | `th:href="@{/users/{id}(id=${userId})}"` | Build URLs with variables |
| CSS class | `th:class`, `th:classappend` | Add/change CSS classes dynamically |
| Style | `th:style` | Add inline CSS dynamically |
| Attribute | `th:attr`, `th:id`, `th:name` | Set any HTML attribute dynamically |
| Reusable parts | `th:fragment` / `th:replace` | Define and reuse navbar, footer, etc. |
| Default value | `${value} ?: 'default'` | Elvis operator — show fallback if null |
| Safe null access | `${obj?.field}` | Don't crash if object is null |
| String utils | `${#strings.isEmpty(v)}` | Utility methods for strings |
| Date utils | `${#dates.format(date, 'yyyy-MM-dd')}` | Format dates |

---

## 3. Why Use and Learn Thymeleaf?

### Reason 1 — Standard in Korean Enterprise Java
Most Korean companies (especially government, finance, SI) use:
- Spring Boot + MyBatis + Thymeleaf (modern)
- Spring MVC + JSP (legacy)

Knowing Thymeleaf is a **job requirement** in many Korean backend positions.

### Reason 2 — Replaces JSP (the old way)
JSP mixes Java code inside HTML — messy and hard to read.
Thymeleaf keeps HTML clean and readable.

```jsp
<!-- JSP — Java mixed into HTML (old way) -->
<% if (user.isAdmin()) { %>
  <span>관리자</span>
<% } %>
```

```html
<!-- Thymeleaf — clean HTML attributes -->
<span th:if="${isAdmin}">관리자</span>
```

### Reason 3 — Works as Normal HTML
Thymeleaf files are valid HTML. You can open them in a browser without a server and they still look correct (placeholders just show their default text).

### Reason 4 — Built into Spring Boot
No extra configuration. Add the dependency → it works. Spring Boot automatically finds `templates/` folder and uses Thymeleaf.

---

## 4. What Should You Know Before Starting?

### Must Know
| Topic | Why you need it |
|---|---|
| Basic Java | You write Java in the controller — variables, lists, if/else, classes |
| Spring `@Controller` | Different from `@RestController` — returns a view name (HTML), not JSON |
| `Model` object | This is how Java sends data to HTML — `model.addAttribute("key", value)` |
| Basic HTML | Tags like `<p>`, `<span>`, `<ul>`, `<li>`, `<table>`, `<form>` |

### Good to Know (not required at the start)
| Topic | When you need it |
|---|---|
| Lombok (`@Data`, `@Builder`) | When creating objects to pass to HTML |
| Java Lists / Maps | When doing `th:each` loops |
| Java `null` | When practicing safe navigation `?.` |
| Spring Boot project structure | Where to put templates, controllers |

### Mental Model to Build First

```
Browser request
      |
      v
@Controller method runs
      |
      v
Java puts data into Model  (model.addAttribute("key", value))
      |
      v
Thymeleaf reads HTML template + Model data
      |
      v
Thymeleaf fills in all the th:* attributes
      |
      v
Browser receives complete HTML with real data
```

The key difference from REST API:
- `@RestController` → returns JSON (for frontend JS / mobile)
- `@Controller` + Thymeleaf → returns complete HTML page (server-side rendering)

---

## 5. File Locations (This Project)

```
src/
  main/
    java/.../thymeleaf/
      ThymeleafLearnController.java   <- controller (Java side)
      domain/
        User.java
        Employee.java
        Department.java
        Address.java
    resources/
      templates/                      <- Thymeleaf looks here automatically
        learn/
          hello.html                  <- Step 1
          users.html                  <- Step 2
          conditions.html             <- Step 3
          urls.html                   <- Step 4
          layout.html                 <- Step 5
          layout-page.html            <- Step 6
        profile.html
```

Controller returns `"learn/hello"` → Thymeleaf finds `templates/learn/hello.html` automatically.

---

## 6. Level Map

```
Level 1 — Basics (DONE)
  1.1  th:text, th:utext, th:class, th:style, th:id          -> /learn/hello
  1.2  th:each, stat (index, count, first, last, size)        -> /learn/users
  1.3  th:if, th:unless, th:switch, th:case                   -> /learn/condition
       Covers: basic if/unless, number compare, nested conditions,
               grade badge (switch), AND / OR / NOT operators
  1.4  @{} URL expressions, path variables, query params      -> /learn/urls
  1.5  th:fragment, th:replace (reusable layout)              -> /learn/layout
  1.6  Shared navbar + footer applied to real page            -> /learn/layout-page
  1.7  Object access, Elvis ?:, active badge                  -> /learn/profile

Level 2 — Objects & Data (NEXT)
  2.1  Nested objects: ${emp.department.name}, safe nav ?.    -> /learn/employee
  2.2  Maps: Map<String, Object>, th:each on map entries      -> /learn/map
  2.3  #strings, #numbers, #dates utility methods             -> /learn/utils

Level 3 — Forms & Validation (Important for Korean companies)
  3.1  th:object, th:field, form binding
  3.2  @Valid, BindingResult, th:errors
  3.3  Select box, checkbox, radio button binding

Level 4 — Real CRUD (Connects to Database)
  4.1  List page with real DB data
  4.2  Create form -> POST -> redirect
  4.3  Edit form -> PUT/PATCH -> redirect
  4.4  Delete button -> soft delete
```