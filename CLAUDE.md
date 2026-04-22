# CLAUDE.md — Spring MyBatis Test Project

## Project Overview
Store Admin System built with Korean enterprise stack (Spring Boot + MyBatis + JSP + jQuery + PostgreSQL).
Deployed on Railway.

## Tech Stack
- **Java 17**, **Spring Boot 4.0.0**, **MyBatis 4.0.0**
- **PostgreSQL** (local: `testdb`, Railway: env var)
- **JSP + JSTL** for server-side views
- **jQuery AJAX** for frontend API calls
- **Lombok**, **MapStruct 1.6.0**, **jjwt 0.11.5**
- **SpringDoc (Swagger) 2.6.0**, **Spring Validation**, **Spring Mail**

## Build & Run
```bash
# Run locally
./mvnw spring-boot:run          # Linux/Mac
mvnw.cmd spring-boot:run        # Windows

# Build JAR
./mvnw clean package

# Run JAR
java --enable-native-access=ALL-UNNAMED -jar target/spring-mybatis-test-0.0.1-SNAPSHOT.jar
```

## Local Dev URLs
- http://localhost:8080/ — Create user form
- http://localhost:8080/user-list — User list
- http://localhost:8080/dashboard — Dashboard (links to all modules)
- http://localhost:8080/board/list.do — Board list
- http://localhost:8080/board/insertForm.do — New board post form
- http://localhost:8080/board/detail.do?boardSn={id} — Board detail + edit
- http://localhost:8080/store/category — Category management
- http://localhost:8080/store/product — Product management
- http://localhost:8080/company/list — Company application list
- http://localhost:8080/company/register — New company register form
- http://localhost:8080/company/detail?id={id} — Company detail + approve/reject
- http://localhost:8080/common-code — Common code management
- http://localhost:8080/swagger-ui.html — Swagger API docs

## Package Structure
`com.heang.springmybatistest`
- `controller/` — REST controllers + JSP view controllers (`BoardMvcController`, `BoardApiController`, `UserController`, `ViewController`)
- `dao/` — Pattern A DAO: `BoardDAO` using `SqlSessionTemplate`
- `service/` — Business logic (interface + impl pattern)
- `mapper/` — MyBatis mapper interfaces (`BoardMapper`, `UserMapper`)
- `model/` — Entities (Board, Users, Category, Product)
- `vo/` — View Objects: `BoardVO` (used by DAO + Service + Controller)
- `dto/` — Request/Response DTOs
- `exception/` — Custom exceptions + GlobalExceptionHandler
- `common/` — ApiResponse wrapper, Pagination, utils
- `config/` — CorsConfig

## SQL Mapper Files
Located in `src/main/resources/mapper/`:
- `BoardMapper.xml` — resultMap type = `BoardVO`, CRUD + soft delete
- `UserMapper.xml`
- `CategoryMapper.xml`
- `ProductMapper.xml`
- `CommonCodeMapper.xml`
- `CompanyMapper.xml`
- `DynamicSqlPracticeMapper.xml`
- `BgtMngMapper.xml` — resultMap → `BgtMngOutVO` (list) / `BgtMngVO` (detail), SELECT/INSERT/UPDATE/soft-DELETE

## Database Tables
| Table | Key Columns | Added |
|-------|-------------|-------|
| `users` | id, username, email, password, name, phone, role, status, created_at, updated_at | initial |
| `co_smp_board_m` | board_sn (PK), board_title, board_cn, use_yn, data_reg_dt | initial |
| `category` | id, name, created_at | initial |
| `product` | id, name, price, stock, category_id (FK→category), image_url, created_at | initial |
| `students` | id, name, email, age, major, created_at | initial |
| `common_code` | code_group+code_value (PK), code_name, sort_order, use_yn, created_at | 2026-04-19 |
| `board_file` | file_sn (PK), board_sn (FK→board), orig_name, saved_name, file_size, created_at | 2026-04-19 |
| `company` | id, company_name, ceo_name, business_no, phone, address, apply_channel, status, contact_name, worker_count, created_at, updated_at | 2026-04-20 |
| `budget_mng` | id, sido_cd, sido_nm, sigungu_cd, sigungu_nm, gu_cd, gu_nm, start_yy, start_mm, end_yy, end_mm, ntnl_bgt, lcl_bgt, del_yn, created_at, updated_at | 2026-04-22 |

Run `schema.sql` to recreate tables + sample data.
Set `SPRING_SQL_INIT_MODE=always` to auto-run on startup.

## SQL Changelog Convention
Every new table, migration, or function in `schema.sql` must follow this format:

```sql
-- =====================================================
-- [YYYY-MM-DD] Short description (한국어 설명)
-- Purpose: why this was added
-- =====================================================
CREATE TABLE ... or ALTER TABLE ... or CREATE FUNCTION ...
```

For migrations only (existing DB, not full reset):
- Add to the `-- MIGRATIONS` section at the bottom of `schema.sql`
- Use `CREATE TABLE IF NOT EXISTS` / `ALTER TABLE ... ADD COLUMN IF NOT EXISTS`
- Always include the date in the comment

## API Response Pattern
All REST endpoints return `ApiResponse<T>`:
```java
return ApiResponse.success(data);
return ApiResponse.success(null);  // no body
```

## Exception Pattern
Throw custom exceptions — `GlobalExceptionHandler` handles them automatically:
```java
throw new NotFoundException("...");
throw new BadRequestException("...");
throw new ConflictException("...");
```

## MyBatis Conventions
- Use `<where>` + `<if>` for dynamic filters
- Use `<set>` + `<if>` for partial updates
- Use `<foreach>` for IN clauses and batch operations
- `mybatis.configuration.map-underscore-to-camel-case=true` is enabled
- SQL logging is ON by default (stdout)

## Environment Variables
| Variable | Default | Notes |
|----------|---------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/testdb` | |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | |
| `SPRING_DATASOURCE_PASSWORD` | `123` | Local default |
| `PORT` | `8080` | |
| `JWT_SECRET` | `change-me-in-production` | |
| `SPRING_MAIL_USERNAME` | _(empty)_ | Gmail |
| `SPRING_MAIL_PASSWORD` | _(empty)_ | Gmail app password |
| `SPRING_SQL_INIT_MODE` | `never` | Set `always` for first deploy |

## Coding Preferences
- Use Lombok (`@RequiredArgsConstructor`, `@AllArgsConstructor`, `@Data`, etc.)
- Service layer always has interface + impl
- DTOs separate from model entities
- MapStruct for DTO ↔ entity mapping where needed
- No auto-commit unless explicitly asked
