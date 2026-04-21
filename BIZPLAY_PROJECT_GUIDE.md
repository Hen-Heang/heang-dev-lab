# 점심밥 지원 구축 — Bizplay Project Reference Guide

> This file is your personal reference for understanding and preparing for the Bizplay project.
> Based on: `제안요청서_점심밥지원구축.pdf` + `BENE-DEV-007_(점심밥_UI설계서_관리자)_v0.4.pdf`

---

## 1. Project Overview (프로젝트 개요)

| Item | Detail |
|------|--------|
| **Project Name** | 점심밥 복지 지원 시스템 구축 |
| **Client** | 한국농수산식품유통공사 (aT) |
| **Budget** | 약 600,000,000원 (6억, VAT 포함) |
| **Deadline** | 2026.12.31 |
| **Purpose** | 복지 혜택으로 직원들이 가맹 식당에서 점심을 지원받는 시스템 |

**English summary:** A welfare benefit system — companies apply to join, their workers get subsidized lunches at registered franchise restaurants, and payments are settled via VAN/PG.

---

## 2. Business Flow (업무 흐름) — Most Important

```
① 기업 신청 (Company applies to join the program)
        ↓
② 접수 / 심사 (Admin receives and reviews the application)
        ↓
③ 승인 / 반려 (Admin approves or rejects)
        ↓
④ 가맹점 등록 (Register restaurants that accept the benefit)
        ↓
⑤ 근로자 등록 (Approved company registers its workers)
        ↓
⑥ 근로자 식사 이용 (Worker eats at a registered franchise)
        ↓
⑦ 결제 / VAN 처리 (Payment processed via VAN/PG)
        ↓
⑧ 정산 (Settlement — admin checks and confirms)
```

> This is the **entire lifecycle** of the system. Your practice lab already covers steps ①–③ (company management). You need to build ④–⑧ next.

---

## 3. Functional Requirements (기능 요구사항, SFR)

| Code | Feature (Korean) | Feature (English) | Priority |
|------|-----------------|-------------------|----------|
| SFR-001 | **기업 관리** | Company management — apply, review, approve/reject | ⭐ You're building this now |
| SFR-002 | **근로자 등록** | Register workers per approved company | ⭐ Next to build |
| SFR-003 | **가맹점 관리** | Register and manage franchise restaurants | ⭐ Next to build |
| SFR-004 | **식사 이용 관리** | Track meal usage per worker | Medium |
| SFR-005 | **결제 / 정산** | Payment via VAN, settlement per period | Medium |
| SFR-006 | **UI/UX** | PC + mobile responsive, Non-ActiveX | Ongoing |
| SFR-007 | **외부 시스템 연동** | External API integration | Later |
| SFR-008 | **11번가 연동** | Product/coupon integration with 11번가 | Later |

---

## 4. Non-Functional Requirements (비기능 요구사항)

### Performance (성능 — PER)
- Must support **10,000 concurrent users** (QUR-003)
- **SLA 95% availability** required (QUR-002)
- Response time must be fast under heavy load

### Security (보안 — SER)
| Requirement | Detail |
|-------------|--------|
| Audit log | Every CRUD action logged with user + IP + timestamp |
| Log retention | **3 years** (SER-002) |
| Password policy | Lock after 5 failed attempts, unlock after 2 hours |
| Session timeout | Inactive: 9 hours, Max: 11 hours |
| Input validation | No SQL injection, XSS, CSRF (SER-009) |
| Vulnerability scan | SW 보안약점 진단 required before delivery |

### Data (데이터 — DAR)
- Must comply with **개인정보보호법** (Personal Information Protection Act)
- Data classification: 공개 / 내부 / 대외비 / 비밀
- Database backup and recovery plan required

### Documents required (PMR-006)
SRS (요구사항정의서), SDD (설계서), ERD, API 문서, 테스트 계획서, 사용자 매뉴얼, 운영 매뉴얼

---

## 5. What You Must Practice (연습 우선순위)

### Level 1 — Build these NOW (you are already doing this)

| Topic | What to practice | Where in this project |
|-------|-----------------|----------------------|
| **CRUD with MyBatis** | List / Register / Detail / Approve | `company/` module |
| **Pagination + Search** | `_startrow`, `pageSize`, `<where><if>` | `companyList.html` + MyBatis XML |
| **Status workflow** | 신청 → 승인 / 반려, button with `th:disabled` | `companyDetail.html` |
| **File upload/download** | Attach files, download link | Already in `board/` module |

### Level 2 — Build these NEXT

| Topic | What to practice | Real use case |
|-------|-----------------|---------------|
| **Parent-child data** | 기업(parent) → 근로자 목록(children) | `JOIN`, separate table |
| **Foreign key logic** | Worker belongs to an approved company | `company_id FK` in workers table |
| **REST API design** | `ApiResponse<T>` pattern, `GET/POST/PUT` | Worker / franchise API |
| **Excel export** | Apache POI — download list as `.xlsx` | Government projects always need this |
| **Audit log table** | Write a log row on every save/update/delete | Security requirement SER-002 |

### Level 3 — Build these LATER

| Topic | What to practice |
|-------|-----------------|
| **External API** | `RestTemplate` or `WebClient` for VAN/PG |
| **Personal data masking** | Show `010-****-5678` in list, full in detail |
| **Spring Security** | Role-based: ADMIN vs COMPANY user |
| **Dashboard statistics** | COUNT by status, chart or summary cards |

---

## 6. Key Korean Business Terms (업무 용어)

| Korean | English | Used in |
|--------|---------|---------|
| 기업 | Company / Enterprise | The business that applies |
| 가맹점 | Franchise / Affiliated store | Restaurant that accepts the benefit |
| 근로자 | Worker / Employee | Person who uses the benefit |
| 정산 | Settlement | Financial reconciliation |
| 신청 | Application | First step in workflow |
| 접수 | Receipt / Intake | Admin receives it |
| 승인 | Approval | Admin approves |
| 반려 | Rejection | Admin rejects with reason |
| 대외비 | Confidential | Data classification level |
| 사업자번호 | Business registration number | Korean company ID (123-45-67890) |
| 결제사 | Payment provider | VAN or PG company |
| 복지포인트 | Welfare points | The benefit amount |
| SLA | Service Level Agreement | Uptime/quality contract |
| 보안약점 | Security vulnerability | SW vulnerability diagnosis |

---

## 7. Database Tables You Need to Build

### Currently built (in this practice project)
```sql
-- company (기업) — partially done
CREATE TABLE company (
    id            SERIAL PRIMARY KEY,
    company_name  VARCHAR(100),
    business_no   VARCHAR(20),
    ceo_name      VARCHAR(50),
    phone         VARCHAR(20),
    address       VARCHAR(200),
    address_detail VARCHAR(100),
    apply_channel VARCHAR(20),  -- ONLINE / OFFLINE
    payment_type  VARCHAR(20),
    payment_provider VARCHAR(50),
    worker_count  INT,
    contact_name  VARCHAR(50),
    contact_phone VARCHAR(20),
    status        VARCHAR(10) DEFAULT '신청',  -- 신청/승인/반려
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP
);
```

### Next tables to build (practice target)
```sql
-- worker (근로자) — belongs to a company
CREATE TABLE worker (
    id          SERIAL PRIMARY KEY,
    company_id  INT REFERENCES company(id),  -- FK to company
    name        VARCHAR(50),
    phone       VARCHAR(20),
    department  VARCHAR(50),
    status      VARCHAR(10) DEFAULT 'ACTIVE',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- franchise (가맹점) — registered restaurants
CREATE TABLE franchise (
    id          SERIAL PRIMARY KEY,
    store_name  VARCHAR(100),
    address     VARCHAR(200),
    phone       VARCHAR(20),
    category    VARCHAR(50),   -- 한식/중식/일식/패스트푸드
    status      VARCHAR(10) DEFAULT 'ACTIVE',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- meal_usage (식사 이용 내역)
CREATE TABLE meal_usage (
    id           SERIAL PRIMARY KEY,
    worker_id    INT REFERENCES worker(id),
    franchise_id INT REFERENCES franchise(id),
    use_date     DATE,
    amount       INT,           -- 식사 금액
    status       VARCHAR(10),   -- 처리중/완료/취소
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 8. Your Practice Lab → Real Project Mapping

| This practice lab (`heang-dev-lab`) | Real Bizplay project |
|-------------------------------------|---------------------|
| `company/list` — 신청 접수내역 | ✅ 기업 신청 관리 화면 |
| `company/register` — 기업 등록 | ✅ 기업 신청 등록 |
| `company/detail` — 상세 + 승인/반려 | ✅ 기업 상세 + 심사 처리 |
| `board/` module — CRUD + file attach | ✅ Base pattern for all modules |
| **Next: worker list per company** | 근로자 관리 |
| **Next: franchise management** | 가맹점 관리 |
| **Next: Excel export** | 정산 내역 다운로드 |
| **Next: audit log** | 보안 요구사항 SER-002 |

---

## 9. Practice Checklist (연습 체크리스트)

### Phase 1 — Complete company module
- [ ] Create `company` DB table (run SQL above)
- [ ] Test `/company/register` form → saves to DB
- [ ] Test `/company/list` → shows results with pagination
- [ ] Test `/company/detail` → approve / reject buttons work
- [ ] Add search filter by status + company name + date range

### Phase 2 — Build worker module
- [ ] Create `worker` table with `company_id` FK
- [ ] Build `WorkerController`, `WorkerService`, `WorkerMapper`
- [ ] `/worker/list?companyId=1` — list workers by company
- [ ] Add / edit / deactivate workers
- [ ] Show worker count on company detail page

### Phase 3 — Build franchise module
- [ ] Create `franchise` table
- [ ] CRUD pages for franchise management
- [ ] Search by name / category / status

### Phase 4 — Advanced features
- [ ] Excel download for any list (Apache POI)
- [ ] Audit log: write to `audit_log` table on every important action
- [ ] Personal data masking on phone number display
- [ ] Dashboard: count cards (total companies, approved, pending workers)

---

## 10. Useful Patterns to Remember

### Status badge (Thymeleaf)
```html
<span class="badge"
      th:classappend="${item.status == '승인'} ? 'badge-success' :
                      (${item.status == '반려'} ? 'badge-danger' : 'badge-primary')"
      th:text="${item.status == '승인'} ? '승인 (Approved)' :
               (${item.status == '반려'} ? '반려 (Rejected)' : '신청 (Applied)')">
</span>
```

### Approve/Reject button (disabled when already in that state)
```html
<button type="submit" class="btn btn-success"
        th:disabled="${detail.status == '승인'}">
    승인 (Approve)
</button>
<button type="submit" class="btn btn-danger"
        th:disabled="${detail.status == '반려'}">
    반려 (Reject)
</button>
```

### MyBatis search with date range
```xml
<select id="searchCompany" resultType="CompanyVO">
    SELECT * FROM company
    <where>
        <if test="status != null and status != ''">
            AND status = #{status}
        </if>
        <if test="companyName != null and companyName != ''">
            AND company_name LIKE '%' || #{companyName} || '%'
        </if>
        <if test="dateFrom != null and dateFrom != ''">
            AND DATE(created_at) >= #{dateFrom}::date
        </if>
        <if test="dateTo != null and dateTo != ''">
            AND DATE(created_at) &lt;= #{dateTo}::date
        </if>
    </where>
    ORDER BY created_at DESC
    LIMIT #{pageSize} OFFSET #{startRow}
</select>
```

### Parent-child query (company + worker count)
```xml
<select id="getCompanyDetail" resultType="CompanyDetailVO">
    SELECT c.*,
           COUNT(w.id) AS worker_count
    FROM company c
    LEFT JOIN worker w ON w.company_id = c.id AND w.status = 'ACTIVE'
    WHERE c.id = #{id}
    GROUP BY c.id
</select>
```

---

## 11. Full Step-by-Step Practice Order (단계별 연습 순서)

> Follow this order from top to bottom.
> Each step builds on the previous one — do not skip ahead.
> Tick the checkbox when you finish each step.

---

### STAGE 1 — Complete the Company Module (기업 관리 완성)
> Goal: Make the company module fully working end-to-end with real DB.

#### Step 1 — Create the company table in DB
```sql
CREATE TABLE company (
    id               SERIAL PRIMARY KEY,
    company_name     VARCHAR(100) NOT NULL,
    business_no      VARCHAR(20),
    ceo_name         VARCHAR(50),
    phone            VARCHAR(20),
    address          VARCHAR(200),
    address_detail   VARCHAR(100),
    apply_channel    VARCHAR(20)  DEFAULT 'ONLINE',
    payment_type     VARCHAR(20),
    payment_provider VARCHAR(50),
    worker_count     INT          DEFAULT 0,
    contact_name     VARCHAR(50),
    contact_phone    VARCHAR(20),
    status           VARCHAR(10)  DEFAULT '신청',
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP
);
```
- [ ] Run the SQL in pgAdmin or psql
- [ ] Verify table exists: `SELECT * FROM company;`

#### Step 2 — Test company register form
- [ ] Go to `/company/register`
- [ ] Fill in all fields and click 등록 (Register)
- [ ] Check that the row appears in the DB: `SELECT * FROM company;`
- [ ] Fix any errors (check controller → service → mapper → XML)

#### Step 3 — Test company list with real data
- [ ] Insert 5+ rows via the register form (mix of 신청/승인/반려 statuses)
- [ ] Go to `/company/list` and verify they appear
- [ ] Test search by status filter
- [ ] Test search by company name
- [ ] Test search by date range
- [ ] Test pagination (insert 15+ rows, check pages work)

#### Step 4 — Test company detail + approve/reject
- [ ] Click a row in the list → goes to `/company/detail?id=X`
- [ ] Click 승인 → status changes to 승인 in DB
- [ ] Click 반려 → status changes to 반려 in DB
- [ ] Verify 승인 button is disabled when status is already 승인
- [ ] Verify 반려 button is disabled when status is already 반려

#### Step 5 — Add updated_at tracking
When approve/reject is clicked, save the current timestamp to `updated_at`.
```java
// In your service or mapper
company.setUpdatedAt(LocalDateTime.now());
```
- [ ] After approving, check that `updated_at` is set in DB
- [ ] In companyDetail page, display 처리일시 (updated_at)

**STAGE 1 COMPLETE — you have a fully working company module.**

---

### STAGE 2 — Build the Worker Module (근로자 관리)
> Goal: Each approved company can have a list of workers.
> New concept: **parent-child relationship** — company (parent) → worker (child)

#### Step 6 — Create the worker table
```sql
CREATE TABLE worker (
    id          SERIAL PRIMARY KEY,
    company_id  INT          NOT NULL REFERENCES company(id),
    name        VARCHAR(50)  NOT NULL,
    phone       VARCHAR(20),
    department  VARCHAR(50),
    position    VARCHAR(50),
    email       VARCHAR(100),
    status      VARCHAR(10)  DEFAULT 'ACTIVE',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);
```
- [ ] Run the SQL
- [ ] Understand why `company_id` is a FK — worker must belong to a company

#### Step 7 — Build the worker list page
Create: `WorkerController`, `WorkerService`, `WorkerServiceImpl`, `WorkerMapper`, `WorkerMapper.xml`

URL: `GET /worker/list?companyId=1`

What the page shows:
- The company name at the top (join with company table)
- A table of workers (name, phone, department, status)
- A "Add Worker" button
- Pagination + search by name

```xml
<!-- WorkerMapper.xml -->
<select id="selectWorkerList" resultType="WorkerVO">
    SELECT w.*, c.company_name
    FROM worker w
    JOIN company c ON c.id = w.company_id
    <where>
        AND w.company_id = #{companyId}
        <if test="name != null and name != ''">
            AND w.name LIKE '%' || #{name} || '%'
        </if>
        <if test="status != null and status != ''">
            AND w.status = #{status}
        </if>
    </where>
    ORDER BY w.created_at DESC
    LIMIT #{pageSize} OFFSET #{startRow}
</select>
```
- [ ] Build the controller and service
- [ ] Build the HTML page (copy company list layout, change columns)
- [ ] Test: `/worker/list?companyId=1` shows workers

#### Step 8 — Link from company detail to worker list
In `companyDetail.html`, add a button:
```html
<a th:href="@{/worker/list(companyId=${detail.id})}" class="btn btn-primary">
    근로자 목록 보기 (View Workers)
</a>
```
- [ ] Click the button from company detail → goes to worker list for that company

#### Step 9 — Add worker registration
URL: `GET /worker/register?companyId=1` → form
URL: `POST /worker/register` → save

Fields: name, phone, department, position, email
Hidden field: `companyId`

- [ ] Build the register form page
- [ ] After saving, redirect to `/worker/list?companyId=X`
- [ ] Test: add 3 workers to one company

#### Step 10 — Add worker detail + edit + deactivate
URL: `GET /worker/detail?id=1`

- Show all worker info
- Edit button → update name, phone, department
- Deactivate button → sets `status = 'INACTIVE'`

- [ ] Build detail page
- [ ] Test edit and deactivate

**STAGE 2 COMPLETE — you understand parent-child relationships.**

---

### STAGE 3 — Build the Franchise Module (가맹점 관리)
> Goal: Register restaurants that workers can eat at.
> This is simpler than workers — no FK to another table.

#### Step 11 — Create the franchise table
```sql
CREATE TABLE franchise (
    id          SERIAL PRIMARY KEY,
    store_name  VARCHAR(100) NOT NULL,
    category    VARCHAR(50),
    address     VARCHAR(200),
    phone       VARCHAR(20),
    open_time   VARCHAR(20),
    close_time  VARCHAR(20),
    status      VARCHAR(10) DEFAULT 'ACTIVE',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
- [ ] Run the SQL

#### Step 12 — Build franchise list + register + detail
URL: `/franchise/list`, `/franchise/register`, `/franchise/detail?id=X`

Same pattern as company module — just different fields.

- [ ] Build all 3 pages
- [ ] Test CRUD (create, read, update, deactivate)

**STAGE 3 COMPLETE — you have 3 modules all following the same pattern.**

---

### STAGE 4 — Meal Usage (식사 이용 내역)
> Goal: Record when a worker eats at a franchise.
> New concept: **two FK columns** — references both worker and franchise.

#### Step 13 — Create the meal_usage table
```sql
CREATE TABLE meal_usage (
    id           SERIAL PRIMARY KEY,
    worker_id    INT  NOT NULL REFERENCES worker(id),
    franchise_id INT  NOT NULL REFERENCES franchise(id),
    use_date     DATE NOT NULL DEFAULT CURRENT_DATE,
    amount       INT  NOT NULL,
    status       VARCHAR(10) DEFAULT '처리중',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
- [ ] Run the SQL

#### Step 14 — Build meal usage list page
URL: `/meal/list`

This is an admin view — show all meal records with:
- 근로자명 (worker name) — from JOIN worker
- 기업명 (company name) — from JOIN company
- 가맹점명 (franchise name) — from JOIN franchise
- 이용일자 (use_date)
- 금액 (amount)
- 상태 (status)

```xml
<select id="selectMealList" resultType="MealUsageVO">
    SELECT m.*,
           w.name    AS worker_name,
           c.company_name,
           f.store_name AS franchise_name
    FROM meal_usage m
    JOIN worker    w ON w.id = m.worker_id
    JOIN company   c ON c.id = w.company_id
    JOIN franchise f ON f.id = m.franchise_id
    <where>
        <if test="companyName != null and companyName != ''">
            AND c.company_name LIKE '%' || #{companyName} || '%'
        </if>
        <if test="dateFrom != null and dateFrom != ''">
            AND m.use_date >= #{dateFrom}::date
        </if>
        <if test="dateTo != null and dateTo != ''">
            AND m.use_date &lt;= #{dateTo}::date
        </if>
    </where>
    ORDER BY m.created_at DESC
    LIMIT #{pageSize} OFFSET #{startRow}
</select>
```
- [ ] Build the list page with multi-table JOIN
- [ ] Add search filters (company name, date range, status)

**STAGE 4 COMPLETE — you can query across 4 tables.**

---

### STAGE 5 — Excel Export (엑셀 다운로드)
> Goal: Any list page can be downloaded as Excel.
> This is required in almost ALL Korean enterprise projects.

#### Step 15 — Add Apache POI dependency
In `pom.xml`:
```xml
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

#### Step 16 — Create an Excel download endpoint
Example: download company list as Excel.

```java
// CompanyController.java
@GetMapping("/company/excel")
public void downloadExcel(CompanySearchVO searchVO, HttpServletResponse response) throws IOException {
    List<CompanyVO> list = companyService.selectAll(searchVO);  // no paging — get all

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet("기업목록");

    // Header row
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("기업명");
    header.createCell(1).setCellValue("사업자번호");
    header.createCell(2).setCellValue("신청상태");
    header.createCell(3).setCellValue("신청일");

    // Data rows
    int rowNum = 1;
    for (CompanyVO item : list) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(item.getCompanyName());
        row.createCell(1).setCellValue(item.getBusinessNo());
        row.createCell(2).setCellValue(item.getStatus());
        row.createCell(3).setCellValue(item.getCreatedAt().toString().substring(0, 10));
    }

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=company_list.xlsx");
    wb.write(response.getOutputStream());
    wb.close();
}
```

In the list page, add a download button:
```html
<a th:href="@{/company/excel(status=${searchVO.status},companyName=${searchVO.companyName})}"
   class="btn btn-secondary">
    엑셀 다운로드 (Excel)
</a>
```
- [ ] Add POI dependency, restart app
- [ ] Build the Excel endpoint
- [ ] Click download button → `.xlsx` file opens in Excel
- [ ] Apply same pattern to worker list and franchise list

**STAGE 5 COMPLETE — a skill you will use in every Korean project.**

---

### STAGE 6 — Dashboard Statistics (대시보드 통계)
> Goal: Show counts and summaries on the dashboard home page.

#### Step 17 — Add stats queries
```xml
<!-- DashboardMapper.xml -->
<select id="selectDashboardStats" resultType="DashboardStatsVO">
    SELECT
        (SELECT COUNT(*) FROM company)                        AS total_company,
        (SELECT COUNT(*) FROM company WHERE status = '신청')  AS pending_company,
        (SELECT COUNT(*) FROM company WHERE status = '승인')  AS approved_company,
        (SELECT COUNT(*) FROM company WHERE status = '반려')  AS rejected_company,
        (SELECT COUNT(*) FROM worker  WHERE status = 'ACTIVE') AS total_worker,
        (SELECT COUNT(*) FROM franchise WHERE status = 'ACTIVE') AS total_franchise
</select>
```

#### Step 18 — Show stats on dashboard.html
Add stat cards to the dashboard:
```html
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-number" th:text="${stats.totalCompany}">0</div>
        <div class="stat-label">전체 기업 (Total Companies)</div>
    </div>
    <div class="stat-card">
        <div class="stat-number" th:text="${stats.pendingCompany}">0</div>
        <div class="stat-label">신청 대기 (Pending)</div>
    </div>
    <div class="stat-card">
        <div class="stat-number" th:text="${stats.approvedCompany}">0</div>
        <div class="stat-label">승인 완료 (Approved)</div>
    </div>
    <div class="stat-card">
        <div class="stat-number" th:text="${stats.totalWorker}">0</div>
        <div class="stat-label">등록 근로자 (Workers)</div>
    </div>
</div>
```
- [ ] Build DashboardMapper + DashboardService
- [ ] Pass `stats` model to dashboard controller
- [ ] See the numbers update as you add more data

**STAGE 6 COMPLETE — your dashboard shows real data.**

---

### STAGE 7 — Audit Log (감사 로그)
> Goal: Record every important action — who did what, when, from which IP.
> This is a **security requirement (SER-002)** in the real project.

#### Step 19 — Create the audit_log table
```sql
CREATE TABLE audit_log (
    id          SERIAL PRIMARY KEY,
    action      VARCHAR(50),    -- INSERT / UPDATE / DELETE / APPROVE / REJECT
    target      VARCHAR(50),    -- company / worker / franchise
    target_id   INT,            -- ID of the affected record
    description VARCHAR(500),   -- human-readable message
    user_name   VARCHAR(100),   -- who did it
    ip_address  VARCHAR(50),    -- from where
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Step 20 — Write log on key actions
In your service layer, after each important action:
```java
// CompanyServiceImpl.java — after approving a company
public void approveCompany(int id) {
    companyMapper.updateStatus(id, "승인");

    // Write audit log
    AuditLog log = new AuditLog();
    log.setAction("APPROVE");
    log.setTarget("company");
    log.setTargetId(id);
    log.setDescription("기업 승인 처리: ID=" + id);
    log.setUserName("admin");  // later: get from Spring Security session
    auditLogMapper.insert(log);
}
```
- [ ] Create `AuditLog` model, `AuditLogMapper`, mapper XML
- [ ] Write log after: company approve, company reject, worker register, worker deactivate
- [ ] Build `/audit/list` page — admin can see all recent actions

**STAGE 7 COMPLETE — you understand security logging.**

---

### STAGE 8 — Spring Security (로그인 + 권한)
> Goal: Add login and protect pages by role.
> New concept: **authentication** (who are you?) + **authorization** (what can you do?)

#### Step 21 — Add Spring Security dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>
```

#### Step 22 — Configure security rules
```java
// SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/login").permitAll()
            .requestMatchers("/company/**").hasRole("ADMIN")
            .requestMatchers("/worker/**").hasAnyRole("ADMIN", "COMPANY")
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/dashboard"))
        .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login"));
        return http.build();
    }
}
```

#### Step 23 — Use the logged-in user in audit logs
```java
// Get current user from Security context
String username = SecurityContextHolder.getContext()
    .getAuthentication().getName();
log.setUserName(username);
```
- [ ] Add Security dependency, configure basic rules
- [ ] Test: `/company/list` redirects to login if not logged in
- [ ] After login, audit log records the correct username

**STAGE 8 COMPLETE — your app has real login and access control.**

---

### STAGE 9 — Input Validation (입력값 검증)
> Goal: Validate all form inputs — server-side and client-side.
> Required by SER-009 in the real project.

#### Step 24 — Add Bean Validation to DTOs
```java
// CompanyInVO.java
@NotBlank(message = "기업명을 입력하세요 (Company name is required)")
private String companyName;

@Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}",
         message = "사업자번호 형식이 올바르지 않습니다 (Format: 000-00-00000)")
private String businessNo;

@NotBlank(message = "대표자명을 입력하세요")
private String ceoName;
```

#### Step 25 — Validate in controller
```java
// CompanyController.java
@PostMapping("/company/register")
public String register(@Valid @ModelAttribute CompanyInVO vo,
                       BindingResult result, Model model) {
    if (result.hasErrors()) {
        model.addAttribute("errors", result.getAllErrors());
        return "company/companyRegister";  // back to form with error messages
    }
    companyService.insertCompany(vo);
    return "redirect:/company/list";
}
```

#### Step 26 — Show errors in the form
```html
<!-- companyRegister.html -->
<input type="text" name="companyName" class="form-control"
       th:classappend="${#fields.hasErrors('companyName')} ? 'is-invalid' : ''"
       th:value="*{companyName}"/>
<div th:if="${#fields.hasErrors('companyName')}"
     class="invalid-feedback"
     th:errors="*{companyName}">Error</div>
```
- [ ] Add `@NotBlank` to required fields in VO
- [ ] Add `@Valid` to controller POST method
- [ ] Test: submit empty form → errors shown on form (not crash)

**STAGE 9 COMPLETE — your forms are safe and user-friendly.**

---

### STAGE 10 — Personal Data Masking (개인정보 마스킹)
> Goal: Show partial phone numbers in list views — required by 개인정보보호법.

#### Step 27 — Add a masking utility
```java
// MaskingUtil.java
public class MaskingUtil {
    // 010-1234-5678 → 010-****-5678
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 9) return phone;
        return phone.replaceAll("(\\d{3})-(\\d{3,4})-(\\d{4})", "$1-****-$3");
    }

    // 홍길동 → 홍*동
    public static String maskName(String name) {
        if (name == null || name.length() <= 1) return name;
        if (name.length() == 2) return name.charAt(0) + "*";
        return name.charAt(0) + "*".repeat(name.length() - 2) + name.charAt(name.length() - 1);
    }
}
```

#### Step 28 — Apply masking in list pages (not detail)
```java
// In controller or service — before passing to model
for (WorkerVO worker : list) {
    worker.setPhone(MaskingUtil.maskPhone(worker.getPhone()));
    worker.setName(MaskingUtil.maskName(worker.getName()));
}
```
- [ ] Build `MaskingUtil`
- [ ] Apply to worker list: phone and name are masked
- [ ] Worker detail page shows full data (no masking)
- [ ] Test: list shows `010-****-5678`, detail shows `010-1234-5678`

**STAGE 10 COMPLETE — you handle personal data correctly.**

---

### Summary Table — All 10 Stages

| Stage | Topic | Key Skill Learned | Status |
|-------|-------|------------------|--------|
| 1 | Company module complete | CRUD + search + pagination + status workflow | [ ] |
| 2 | Worker module | Parent-child FK, JOIN query | [ ] |
| 3 | Franchise module | Independent CRUD, same pattern repeat | [ ] |
| 4 | Meal usage | Multi-table JOIN (4 tables) | [ ] |
| 5 | Excel export | Apache POI, file download response | [ ] |
| 6 | Dashboard stats | Aggregate queries, model to view | [ ] |
| 7 | Audit log | Security logging, tracking who did what | [ ] |
| 8 | Spring Security | Login, roles, protect URLs | [ ] |
| 9 | Input validation | `@Valid`, `BindingResult`, form error display | [ ] |
| 10 | Data masking | Personal info protection, utility class | [ ] |

> **After Stage 10**, you will have practiced every major skill needed for the Bizplay project. The real project adds VAN/PG payment integration and 11번가 API, but those come after the core system is built — and your team seniors will guide those parts.

---

*Last updated: 2026-04-21*
*Source documents: 제안요청서_점심밥지원구축.pdf, BENE-DEV-007_(점심밥_UI설계서_관리자)_v0.4.pdf*
