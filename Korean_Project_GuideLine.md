## 1. 개발 순서 & 파일 목록

| 순서 | 파일 | 위치 | 설명 |
|:----:|------|------|------|
| smp1 | `SmpBoardInVO.java` | `service/` | 입력 파라미터용 VO (CmmVO 상속) |
| smp2 | `SmpBoardOutVO.java` | `service/` | 조회 결과용 VO (CmmVO 상속) |
| smp3 | `SmpBoardList.html` | `templates/.../smp/` | 목록 화면 |
| smp4 | `SmpBoardRegist.html` | `templates/.../smp/` | 등록 화면 |
| smp5 | `SmpBoardUpdt.html` | `templates/.../smp/` | 수정/상세 화면 |
| smp6 | `SmpBoardController.java` | `web/` | HTTP 요청 수신 → Service 호출 → View 반환 |
| smp7 | `SmpBoardService.java` | `service/` | 비즈니스 로직 계약 (인터페이스) |
| smp8 | `SmpBoardServiceImpl.java` | `service/impl/` | 인터페이스의 실제 구현체 |
| smp9 | `SmpBoardMapper.java` | `mapper/` | MyBatis Mapper 인터페이스 (`@Mapper`) |
| smp10 | `SmpBoard_SQL.xml` | `mapper/.../smp/` | 실제 SQL 쿼리 정의 |
 
---

## 2. 요청 흐름 상세 — "목록 조회" 예시

사용자가 브라우저에서 `/smpBoard/list.do`를 요청했을 때,
데이터가 어떤 경로를 거쳐 화면에 표시되는지 단계별로 설명합니다.
 
---

### STEP 1. 브라우저 → Controller `[smp3 → smp6]`

**[smp3]** `SmpBoardList.html`에서 사용자가 검색 버튼을 클릭합니다:

```html
<form th:action="@{/smpBoard/list.do}" method="get">
  <select name="searchCondition">...</select>
  <input name="searchKeyword" .../>
  <button type="submit">검색</button>
</form>
```

브라우저가 서버로 요청을 보냅니다:
```
GET /smpBoard/list.do?searchCondition=1&searchKeyword=제목
```

**[smp6]** `SmpBoardController`의 메서드가 호출됩니다:

```java
@RequestMapping("/smpBoard/list.do")
public String selectList(
    @ModelAttribute("searchVO") SmpBoardInVO inVO,  // ← 파라미터 자동 바인딩
    ModelMap model) throws Exception {
```

> **`@ModelAttribute`란?**
> HTTP 요청의 파라미터(`name=value`)를 VO의 setter에 자동 매핑해줍니다.
> `name="searchKeyword"` → `inVO.setSearchKeyword("제목")` 자동 호출
 
---

### STEP 2. Controller → Service 인터페이스 `[smp6 → smp7]`

Controller는 **Service "인터페이스" 타입**으로 호출합니다.
구현체를 직접 알지 못합니다 → **느슨한 결합**

```java
// Controller 내부
@Autowired
private SmpBoardService smpBoardService;   // ← 인터페이스 타입!
 
// 호출
List<SmpBoardOutVO> resultList = smpBoardService.selectList(inVO);
```

**[smp7]** `SmpBoardService.java` (인터페이스):

```java
public interface SmpBoardService {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;
    int selectListTotCnt(SmpBoardInVO inVO) throws Exception;
    SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception;
    void insert(SmpBoardInVO inVO) throws Exception;
    void update(SmpBoardInVO inVO) throws Exception;
    void delete(SmpBoardInVO inVO) throws Exception;
}
```

> **왜 인터페이스를 만드는가?**
> - Controller는 **"무엇을 할 수 있는지"(계약)**만 알면 됩니다
> - **"어떻게 하는지"(구현)**는 ServiceImpl이 담당합니다
> - 구현체를 교체해도 Controller 코드를 수정할 필요가 없습니다
> - Spring이 `@Autowired` 시점에 구현체를 자동 연결해줍니다
 
---

### STEP 3. Service 인터페이스 → ServiceImpl (구현체) `[smp7 → smp8]`

Spring이 인터페이스 호출을 **구현체로 자동 연결**해줍니다.
(개발자가 직접 `new` 하지 않습니다!)

#### 연결 원리

```
@Service("smpBoardService")              ← ① 이 이름으로 Spring에 빈 등록
public class SmpBoardServiceImpl
    implements SmpBoardService           ← ② 인터페이스 구현 선언
 
    ↓ Spring 컨테이너가 서버 시작 시 자동으로:
 
Controller의  @Autowired SmpBoardService smpBoardService  필드에
              SmpBoardServiceImpl 인스턴스를 주입
```

**[smp8]** `SmpBoardServiceImpl.selectList()` 실행:

```java
@Service("smpBoardService")
public class SmpBoardServiceImpl implements SmpBoardService {
 
    @Autowired
    private SmpBoardMapper smpBoardMapper;    // ← Mapper 인터페이스 주입
 
    @Override
    public List<SmpBoardOutVO> selectList(
            SmpBoardInVO inVO) throws Exception {
 
        // ★ 비즈니스 로직이 필요하면 여기에 작성
        // 예: 권한 체크, 데이터 가공, 여러 Mapper 조합, 트랜잭션 처리 등
 
        return smpBoardMapper.selectList(inVO);    // → Mapper 호출
    }
}
```
 
---

### STEP 4. ServiceImpl → Mapper 인터페이스 `[smp8 → smp9]`

ServiceImpl이 **Mapper 인터페이스**를 호출합니다.
Mapper 인터페이스는 `@Mapper` 어노테이션이 선언된 인터페이스이며,
**MyBatis가 자동으로 구현체를 생성**합니다. 개발자가 직접 구현 클래스를 작성할 필요가 없습니다.

**[smp9]** `SmpBoardMapper.java`:

```java
@Mapper
public interface SmpBoardMapper {
    List<SmpBoardOutVO> selectList(SmpBoardInVO inVO) throws Exception;
    int selectListTotCnt(SmpBoardInVO inVO) throws Exception;
    SmpBoardOutVO selectDetail(SmpBoardInVO inVO) throws Exception;
    void insert(SmpBoardInVO inVO) throws Exception;
    void update(SmpBoardInVO inVO) throws Exception;
    void delete(SmpBoardInVO inVO) throws Exception;
}
```

> **Mapper 인터페이스 방식의 장점:**
> - `@Mapper`만 선언하면 MyBatis가 **자동으로 구현체를 생성**합니다
> - SQL XML의 `namespace`가 Mapper 인터페이스의 **FQCN(패키지 포함 전체 클래스명)**과 일치해야 합니다
> - 메서드 이름이 SQL XML의 `id`와 자동 매칭됩니다
> - 기존 DAO 방식처럼 `SqlSession`을 직접 호출할 필요가 없습니다
 
---

### STEP 5. Mapper 인터페이스 → SQL Mapper XML `[smp9 → smp10]`

MyBatis가 XML에서 **namespace + id가 일치하는 쿼리**를 찾아 실행합니다.

**[smp10]** `SmpBoard_SQL.xml`:

```xml
<mapper namespace="egovframework.com.smp.mapper.SmpBoardMapper">  <!-- Mapper 인터페이스 FQCN -->
 
  <select id="selectList"                  <!-- Mapper 메서드명과 일치! -->
    parameterType="egovframework.com.smp.service.SmpBoardInVO"
    resultMap="smpBoard">
 
    SELECT board_sn, board_title, use_yn, ...
      FROM co_smp_board_m
     WHERE 1=1
    <if test="searchKeyword != null and searchKeyword != ''">
      <if test="searchCondition == '1'">
        AND board_title LIKE '%' || #{searchKeyword} || '%'
      </if>
    </if>
     ORDER BY board_sn DESC
     LIMIT #{recordCountPerPage} OFFSET #{firstIndex}
 
  </select>
</mapper>
```

> **매핑 원리:**
> - `namespace="egovframework.com.smp.mapper.SmpBoardMapper"` → Mapper 인터페이스의 FQCN과 매칭
> - `id="selectList"` → Mapper 인터페이스의 `selectList()` 메서드와 매칭
> - `#{필드명}` → 파라미터 VO(InVO)의 **getter를 호출**하여 값을 바인딩
    >   - `#{searchKeyword}` → `inVO.getSearchKeyword()` 값
>   - `#{firstIndex}` → `inVO.getFirstIndex()` 값
> - `resultMap` → DB 컬럼(snake_case)을 OutVO 필드(camelCase)로 변환
    >   - `board_title` → `boardTitle`
>   - `data_reg_dt` → `dataRegDt`
 
---

### STEP 6. DB → 결과 반환 (역방향) `[smp10 → smp8 → smp6]`

DB 결과가 **역순으로** 올라옵니다:

```
DB 결과 (ResultSet)
    │
    ▼  resultMap이 컬럼 → OutVO 필드로 변환
[smp10]  List<SmpBoardOutVO>  (MyBatis가 자동 생성)
    │
    ▼  Mapper 인터페이스가 MyBatis를 통해 자동 반환
[smp9]   (MyBatis가 구현체를 자동 생성하여 반환)
    │
    ▼  ServiceImpl이 그대로 반환 (필요 시 여기서 가공)
[smp8]   return smpBoardMapper.selectList(inVO);
    │
    ▼  Controller가 Model에 담음
[smp6]   model.addAttribute("resultList", resultList);
         model.addAttribute("paginationInfo", paginationInfo);
         return "egovframework/com/smp/SmpBoardList";
                 └─ Thymeleaf가 이 경로의 HTML을 렌더링
```
 
---

### STEP 7. Controller → HTML 렌더링 `[smp6 → smp3]`

Controller가 return한 문자열 → **Thymeleaf 템플릿 경로**
Model에 담은 데이터 → HTML에서 **`${변수명}`**으로 접근

**[smp3]** `SmpBoardList.html`에서 결과 표시:

```html
<tr th:each="item, stat : ${resultList}">
  <td th:text="${...번호계산...}">1</td>
  <td>
    <a th:href="@{/smpBoard/detail.do(boardSn=${item.boardSn})}"
       th:text="${item.boardTitle}">제목</a>
  </td>
  <td th:text="${item.dataRegDt}">2026-01-01</td>
</tr>
```

→ 브라우저에 완성된 HTML이 표시됩니다.
 
---

## 3. 전체 흐름 한눈에 보기

```
브라우저       Controller       Service(I/F)     ServiceImpl       Mapper(I/F)      SQL XML          DB
  │              │                 │                │               │                │              │
  │  GET 요청    │                 │                │               │                │              │
  │─────────────→│                 │                │               │                │              │
  │              │  selectList()   │                │               │                │              │
  │              │────────────────→│                │               │                │              │
  │              │                 │  (Spring이     │               │                │              │
  │              │                 │   구현체 연결) │               │                │              │
  │              │                 │───────────────→│               │                │              │
  │              │                 │                │ selectList()  │                │              │
  │              │                 │                │──────────────→│                │              │
  │              │                 │                │               │  selectList    │              │
  │              │                 │                │               │───────────────→│              │
  │              │                 │                │               │                │  SELECT ...  │
  │              │                 │                │               │                │─────────────→│
  │              │                 │                │               │                │              │
  │              │                 │                │               │                │  ResultSet   │
  │              │                 │                │               │   List<VO>     │←─────────────│
  │              │                 │                │  List<VO>     │←───────────────│              │
  │              │                 │   List<VO>     │←──────────────│                │              │
  │              │   List<VO>      │←───────────────│               │                │              │
  │              │←────────────────│                │               │                │              │
  │   HTML       │                 │                │               │                │              │
  │←─────────────│                 │                │               │                │              │
```
 
---

## 4. 의존성 주입(DI) — Spring이 자동으로 연결해주는 것들

| 선언하는 곳 (어노테이션) | 주입받는 곳 (`@Autowired`) |
|--------------------------|----------------------------|
| `@Service("smpBoardService")` <br> SmpBoardServiceImpl | Controller의 `SmpBoardService` 필드 <br> (인터페이스 타입으로 선언) |
| `@Mapper` <br> SmpBoardMapper | ServiceImpl의 `SmpBoardMapper` 필드 <br> (MyBatis가 구현체를 자동 생성하여 빈 등록) |
| `@Controller` <br> SmpBoardController | Spring MVC가 URL 매핑 자동 등록 |

> **개발자는 `new` 키워드를 사용하지 않습니다!**
> Spring 컨테이너가 서버 시작 시 모든 빈을 생성하고 연결합니다.
 
---

## 5. 파일 위치 규칙

### Java 소스

```
olv-oper/src/main/java/egovframework/com/{도메인}/
├── web/
│   └── {도메인}Controller.java       @Controller
├── mapper/
│   └── {도메인}Mapper.java           @Mapper (인터페이스)
├── service/
│   ├── {도메인}InVO.java             CmmVO 상속 (입력 파라미터)
│   ├── {도메인}OutVO.java            CmmVO 상속 (조회 결과)
│   └── {도메인}Service.java          interface
└── service/impl/
    └── {도메인}ServiceImpl.java      @Service
```

### HTML 템플릿

```
olv-oper/src/main/resources/templates/egovframework/com/{도메인}/
├── {도메인}List.html         목록
├── {도메인}Regist.html       등록
└── {도메인}Updt.html         수정/상세
```

### SQL Mapper

```
olv-oper/src/main/resources/egovframework/mapper/com/{도메인}/
└── {도메인}_SQL.xml
    namespace = Mapper 인터페이스의 FQCN
    (예: egovframework.com.smp.mapper.SmpBoardMapper)
```
 
---

## 6. 새 화면 만들기 체크리스트

> 아래 순서대로 만들면 **컴파일 에러 없이** 진행됩니다.
> (하위 계층부터 만들어야 상위에서 import 가능)

- [ ] **1. DDL 확인** — 테이블, 시퀀스, PK 구조 파악
- [ ] **2. InVO 작성** — `CmmVO` 상속, 입력 파라미터(검색조건/폼 입력값) 매핑 → `[smp1]` 참고
- [ ] **3. OutVO 작성** — `CmmVO` 상속, 조회 결과 매핑 → `[smp2]` 참고
- [ ] **4. SQL Mapper XML 작성** — namespace(Mapper FQCN), resultMap, CRUD 쿼리 → `[smp10]` 참고
- [ ] **5. Mapper 인터페이스 작성** — `@Mapper`, CRUD 메서드 선언 → `[smp9]` 참고
- [ ] **6. Service 인터페이스 작성** — CRUD 메서드 선언 → `[smp7]` 참고
- [ ] **7. ServiceImpl 작성** — `@Service`, Mapper 호출 → `[smp8]` 참고
- [ ] **8. Controller 작성** — `@Controller`, URL 매핑, Service 호출 → `[smp6]` 참고
- [ ] **9. HTML 화면 작성** — 목록/등록/수정 → `[smp3~5]` 참고
- [ ] **10. 컴파일 & 서버 기동 테스트**

---

## 7. 샘플 테이블 DDL (참고)

```sql
CREATE SEQUENCE seq_co_smp_board_m START WITH 1 INCREMENT BY 1;
 
CREATE TABLE co_smp_board_m (
    board_sn       BIGINT         NOT NULL DEFAULT nextval('seq_co_smp_board_m'),
    board_title    VARCHAR(200)   NOT NULL,
    board_cn       TEXT,
    use_yn         CHAR(1)        DEFAULT 'Y',
    data_reg_id    VARCHAR(20),
    data_reg_dt    TIMESTAMP      DEFAULT NOW(),
    data_chg_id    VARCHAR(20),
    data_chg_dt    TIMESTAMP,
    CONSTRAINT pk_co_smp_board_m PRIMARY KEY (board_sn)
);
```