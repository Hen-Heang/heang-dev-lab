# Spring MyBatis Test Project

> 한국 기업 프로젝트 표준 스택으로 배우는 웹 개발  
> Learning Web Development with Korean Enterprise Standard Stack

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-green)
![MyBatis](https://img.shields.io/badge/MyBatis-4.0.0-blue)
![jQuery](https://img.shields.io/badge/jQuery-3.7.1-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)

---

## 📋 Table of Contents
- [About Project](#-about-project)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Features](#-features)
- [API Endpoints](#-api-endpoints)
- [SQL Fundamentals Reference](#-sql-fundamentals-reference)
- [MyBatis Dynamic SQL Reference](#-mybatis-dynamic-sql-reference)
- [Learning Roadmap](#-learning-roadmap)
- [Study Resources](#-study-resources)

---

## 📌 About Project

This project is a **Spring Boot + MyBatis practice project** built with the standard Korean enterprise stack.
It currently includes:
- **User Management System** - REST API + JSP/jQuery screens
- **Board MVC Module** - traditional `.do` style board flow for Korean enterprise practice

Main technologies:
- **Spring Boot** - Backend framework
- **MyBatis** - SQL Mapper
- **JSP** - View template
- **jQuery AJAX** - Frontend interaction
- **PostgreSQL** - Database

This stack is commonly used in:
- 🏛️ Korean Government Projects (전자정부 표준프레임워크)
- 🏢 Large Enterprise SI Projects (삼성SDS, LG CNS, SK C&C)
- 🏦 Financial Systems (Banks, Insurance)

---

## 🗓 Updates

### 2026-03-10
- Fixed `ClassCastException`: `BoardMapper.xml` resultMap type changed from `Board` → `BoardVO`; `BoardMapper.java` updated to return `BoardVO`; `BoardMapperTest` updated to use `BoardVO`
- Fixed `detail.jsp` date display: replaced `fmt:formatDate` (requires `java.util.Date`) with `fn:substring(board.dataRegDt, 0, 19)` for `LocalDateTime`
- Added `← Dashboard` button to board list page for easy navigation back
- Fixed dashboard URL in board list: `/dashboard.do` → `/dashboard`
- Fixed `BoardMvcControllerTest` — removed `@AutoConfigureMockMvc` (not available in Spring Boot 4.0.0)
- Replaced with `@ExtendWith(MockitoExtension.class)` + `MockMvcBuilders.standaloneSetup(controller)`
- Fixed `BoardMapperTest` — removed `@AutoConfigureTestDatabase`, replaced with `@SpringBootTest` + `@Transactional`

### 2026-03-09
- Added **Board MVC module** — Korean enterprise `.do` URL style
- Implemented full layer stack: `Board` model, `BoardVO`, `BoardMapper`, `BoardDAO`, `BoardService`, `BoardServiceImpl`, `BoardMvcController`
- Added MyBatis XML mapper for board: SELECT all, SELECT by ID, search by title, INSERT, UPDATE, soft DELETE
- Added JSP pages: `board/list.jsp`, `board/detail.jsp`
- Added 3 test files: `BoardMapperTest`, `BoardServiceTest`, `BoardMvcControllerTest`
- Removed duplicate `BoardController` (REST style) — kept `BoardMvcController` (Korean MVC style)

### IDE Note
- Set `JAVA_HOME` to JDK root directory, not the `bin` folder
- Example: `C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot`

---

## 🛠 Tech Stack

### Backend
| Technology | Version | Description |
|------------|---------|-------------|
| Java | 17 | Programming Language |
| Spring Boot | 4.0.0 | Web Framework |
| MyBatis | 4.0.0 | SQL Mapper |
| PostgreSQL | Latest | Database |
| Lombok | Latest | Boilerplate Reduction |

### Frontend
| Technology | Version | Description |
|------------|---------|-------------|
| JSP | 3.0 | View Template |
| JSTL | Jakarta | JSP Tag Library |
| jQuery | 3.7.1 | JavaScript Library |
| CSS3 | - | Styling |

---

## 📁 Project Structure

```
spring-mybatis-test/
├── src/main/java/com/heang/springmybatistest/
│   ├── SpringMybatisTestApplication.java    # Main Application
│   ├── common/
│   │   └── ApiResponse.java                 # API Response Wrapper
│   ├── config/
│   │   └── CorsConfig.java                  # CORS Configuration
│   ├── controller/
│   │   ├── BoardMvcController.java          # Traditional Board MVC Controller
│   │   ├── UserController.java              # REST API Controller
│   │   └── ViewController.java              # View Controller (JSP)
│   ├── dao/
│   │   └── BoardDAO.java                    # Board DAO using SqlSessionTemplate
│   ├── dto/
│   │   ├── UserRequest.java                 # Create User DTO
│   │   ├── UserUpdateRequest.java           # Update User DTO
│   │   └── UserResponse.java                # Response DTO
│   ├── mapper/
│   │   ├── BoardMapper.java                 # Board MyBatis Mapper Interface
│   │   └── UserMapper.java                  # MyBatis Mapper Interface
│   ├── model/
│   │   ├── Board.java                       # Board Entity
│   │   └── Users.java                       # Entity
│   ├── vo/
│   │   └── BoardVO.java                     # Board View Object
│   └── service/
│       ├── BoardService.java                # Board Service Interface
│       ├── BoardServiceImpl.java            # Board Service Implementation
│       ├── UserService.java                 # Service Interface
│       └── UserServiceImpl.java             # Service Implementation
│
├── src/main/resources/
│   ├── application.properties               # App Configuration
│   └── mapper/
│       ├── BoardMapper.xml                  # Board MyBatis SQL Mapper
│       └── UserMapper.xml                   # MyBatis SQL Mapper
│
├── src/main/webapp/
│   ├── css/
│   │   └── style.css                        # Common Styles
│   ├── js/
│   │   ├── user-api.js                      # jQuery AJAX API Service
│   │   └── common.js                        # Common JS Utilities
│   └── WEB-INF/views/
│       ├── board/
│       │   ├── list.jsp                     # Board List + Search + Delete
│       │   ├── detail.jsp                   # Board Detail + Edit + Delete
│       │   └── insertForm.jsp               # New Board Post Form
│       ├── users.jsp                        # Create User Page
│       └── user-list.jsp                    # User List Page
│
├── src/test/java/com/heang/springmybatistest/
│   ├── controller/
│   │   └── BoardMvcControllerTest.java      # Board MVC Controller Test
│   ├── mapper/
│   │   └── BoardMapperTest.java             # Board Mapper Test
│   └── service/
│       └── BoardServiceTest.java            # Board Service Test
│
└── pom.xml                                  # Maven Dependencies
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL

### Database Setup
```sql
CREATE DATABASE postgres;

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Run Application
```bash
# Using Maven Wrapper
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### JVM Native Access
If you see a warning about `java.lang.System::load` and restricted native access (for example from `cool-request-agent.jar`),
this project enables the required flag for Maven runs via `.mvn/jvm.config`.

If you run outside Maven, add the flag manually:

```bash
java --enable-native-access=ALL-UNNAMED -jar target/spring-mybatis-test-0.0.1-SNAPSHOT.jar
```

For IntelliJ, add `--enable-native-access=ALL-UNNAMED` to the Run Configuration VM options.

### Access Application
- **Create User Page**: http://localhost:8080/
- **User List Page**: http://localhost:8080/user-list
- **Dashboard**: http://localhost:8080/dashboard
- **Board List**: http://localhost:8080/board/list.do
- **Swagger API Docs**: http://localhost:8080/swagger-ui.html

---

## ✨ Features

### User Management
| Feature | Description | URL |
|---------|-------------|-----|
| Create User | Add new user | `GET /` |
| View All Users | List with statistics | `GET /user-list` |
| View User | User detail modal | Click "View" button |
| Edit User | Update user info | Click "Edit" button |
| Delete User | Remove user | Click "Delete" button |

### Board MVC Practice
| Feature | Description | URL |
|---------|-------------|-----|
| Board List | Show active board posts | `GET /board/list.do` |
| Board Detail | Show one board post | `GET /board/detail.do?boardSn={id}` |
| Board Insert | Save a new board post | `POST /board/insert.do` |
| Board Update | Update title/content | `POST /board/update.do` |
| Board Delete | Soft delete with `use_yn = 'N'` | `POST /board/delete.do` |

### UI Features
- ✅ Responsive design
- ✅ Modal dialogs
- ✅ Form validation
- ✅ Status badges
- ✅ Statistics cards
- ✅ AJAX (no page reload)
- ✅ Traditional JSP MVC board flow

---

## 🔌 API Endpoints

### User APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users` | Get all users |
| `GET` | `/users/{id}` | Get user by ID |
| `POST` | `/users` | Create new user |
| `PUT` | `/users/{id}` | Update user |
| `DELETE` | `/users/{id}` | Delete user |

### Request/Response Examples

#### Create User
```javascript
// Request
POST /users
Content-Type: application/json

{
    "username": "john",
    "email": "john@example.com",
    "status": "active"
}

// Response
{
    "resultCd": "M0000",
    "resultMsg": "User created successfully",
    "data": null
}
```

#### Get All Users
```javascript
// Request
GET /users

// Response
{
    "resultCd": "M0000",
    "resultMsg": "Success",
    "data": [
        {
            "id": 1,
            "username": "john",
            "email": "john@example.com",
            "status": "active",
            "createdAt": "2024-01-15T10:30:00"
        }
    ]
}
```

---

## 📖 SQL Fundamentals Reference

> Master SQL basics first, then Dynamic SQL becomes easy!

### 🎯 SQL Statement Types

| Type | Purpose | Example |
|------|---------|---------|
| **SELECT** | Read data | `SELECT * FROM users` |
| **INSERT** | Create data | `INSERT INTO users VALUES (...)` |
| **UPDATE** | Modify data | `UPDATE users SET name = 'new'` |
| **DELETE** | Remove data | `DELETE FROM users WHERE id = 1` |

---

### 1️⃣ SELECT - Reading Data

**Basic Structure:**
```sql
SELECT columns      -- What columns to get
FROM table          -- From which table
WHERE condition     -- Filter rows
ORDER BY column     -- Sort results
LIMIT number        -- Limit rows returned
```

**Examples:**
```sql
-- Get all columns, all rows
SELECT * FROM users;

-- Get specific columns
SELECT id, username, email FROM users;

-- Get with alias (rename column in result)
SELECT username AS name, email AS mail FROM users;
```

**When to use SELECT:**
- Display data on screen
- Check if data exists
- Get data for processing

---

### 2️⃣ WHERE - Filtering Data

**Why use WHERE:**  
Without WHERE, you get ALL rows. WHERE filters to get only what you need.

**Comparison Operators:**
| Operator | Meaning | Example |
|----------|---------|---------|
| `=` | Equal | `WHERE status = 'ACTIVE'` |
| `!=` or `<>` | Not equal | `WHERE status != 'DELETED'` |
| `>` | Greater than | `WHERE age > 18` |
| `<` | Less than | `WHERE price < 100` |
| `>=` | Greater or equal | `WHERE age >= 18` |
| `<=` | Less or equal | `WHERE price <= 100` |

**Examples:**
```sql
-- Exact match
SELECT * FROM users WHERE status = 'ACTIVE';

-- Multiple conditions.html with AND (both must be true)
SELECT * FROM users WHERE status = 'ACTIVE' AND age >= 18;

-- Multiple conditions.html with OR (either can be true)
SELECT * FROM users WHERE status = 'ACTIVE' OR status = 'PENDING';

-- Combine AND and OR (use parentheses!)
SELECT * FROM users 
WHERE status = 'ACTIVE' AND (age >= 18 OR role = 'ADMIN');
```

---

### 3️⃣ LIKE - Pattern Matching

**Why use LIKE:**  
When you need partial matching (search), not exact match.

**Wildcards:**
| Wildcard | Meaning | Example |
|----------|---------|---------|
| `%` | Any characters (0 or more) | `'%john%'` matches "john", "johnny", "big john" |
| `_` | Exactly one character | `'j_hn'` matches "john", "jahn" |

**Examples:**
```sql
-- Starts with 'john'
SELECT * FROM users WHERE username LIKE 'john%';
-- Matches: john, johnny, john_doe

-- Ends with '@gmail.com'
SELECT * FROM users WHERE email LIKE '%@gmail.com';
-- Matches: test@gmail.com, user@gmail.com

-- Contains 'admin'
SELECT * FROM users WHERE username LIKE '%admin%';
-- Matches: admin, superadmin, admin123

-- Exactly 4 characters starting with 'j'
SELECT * FROM users WHERE username LIKE 'j___';
-- Matches: john, jack, jane
```

**When to use LIKE:**
- Search functionality
- Email domain filtering
- Username pattern matching

---

### 4️⃣ IN - Multiple Values

**Why use IN:**  
Cleaner than multiple OR conditions.

```sql
-- Instead of this (ugly):
SELECT * FROM users 
WHERE status = 'ACTIVE' OR status = 'PENDING' OR status = 'REVIEW';

-- Use IN (clean):
SELECT * FROM users 
WHERE status IN ('ACTIVE', 'PENDING', 'REVIEW');
```

**More Examples:**
```sql
-- Find users by multiple IDs
SELECT * FROM users WHERE id IN (1, 2, 3, 4, 5);

-- NOT IN - exclude these values
SELECT * FROM users WHERE status NOT IN ('DELETED', 'BANNED');
```

**When to use IN:**
- Filter by list of IDs
- Filter by multiple status values
- When you have 3+ OR conditions for same column

---

### 5️⃣ BETWEEN - Range

**Why use BETWEEN:**  
Cleaner for range conditions.

```sql
-- Instead of this:
SELECT * FROM users WHERE age >= 18 AND age <= 30;

-- Use BETWEEN:
SELECT * FROM users WHERE age BETWEEN 18 AND 30;
```

**Date Range Example:**
```sql
-- Users created in 2024
SELECT * FROM users 
WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31';
```

**When to use BETWEEN:**
- Age ranges
- Date ranges
- Price ranges

---

### 6️⃣ ORDER BY - Sorting

**Why use ORDER BY:**  
Control the order of results (newest first, alphabetical, etc.)

**Direction:**
| Direction | Meaning | Example |
|-----------|---------|---------|
| `ASC` | Ascending (A→Z, 1→9) | Default |
| `DESC` | Descending (Z→A, 9→1) | Newest first |

**Examples:**
```sql
-- Sort by username A-Z
SELECT * FROM users ORDER BY username ASC;

-- Sort by created date (newest first)
SELECT * FROM users ORDER BY created_at DESC;

-- Multiple sort columns
SELECT * FROM users ORDER BY status ASC, created_at DESC;
-- First sorts by status, then by date within each status
```

**When to use ORDER BY:**
- Show newest items first
- Alphabetical listing
- Ranking/leaderboard

---

### 7️⃣ LIMIT & OFFSET - Pagination

**Why use LIMIT/OFFSET:**  
Don't load all 10,000 rows at once! Load page by page.

```sql
-- Get first 10 rows
SELECT * FROM users LIMIT 10;

-- Get rows 11-20 (page 2, 10 per page)
SELECT * FROM users LIMIT 10 OFFSET 10;

-- Get rows 21-30 (page 3)
SELECT * FROM users LIMIT 10 OFFSET 20;
```

**Pagination Formula:**
```
OFFSET = (page_number - 1) * page_size

Page 1: LIMIT 10 OFFSET 0   (rows 1-10)
Page 2: LIMIT 10 OFFSET 10  (rows 11-20)
Page 3: LIMIT 10 OFFSET 20  (rows 21-30)
```

**When to use LIMIT/OFFSET:**
- Pagination (page 1, 2, 3...)
- "Load more" button
- Top N results (Top 10 users)

---

### 8️⃣ INSERT - Creating Data

**Basic Structure:**
```sql
INSERT INTO table (column1, column2, column3)
VALUES (value1, value2, value3);
```

**Examples:**
```sql
-- Insert single row
INSERT INTO users (username, email, status)
VALUES ('john', 'john@test.com', 'ACTIVE');

-- Insert multiple rows (batch insert)
INSERT INTO users (username, email, status)
VALUES 
    ('user1', 'user1@test.com', 'ACTIVE'),
    ('user2', 'user2@test.com', 'ACTIVE'),
    ('user3', 'user3@test.com', 'PENDING');
```

**When to use INSERT:**
- User registration
- Creating new records
- Bulk data import

---

### 9️⃣ UPDATE - Modifying Data

**Basic Structure:**
```sql
UPDATE table
SET column1 = value1, column2 = value2
WHERE condition;
```

**⚠️ ALWAYS use WHERE with UPDATE!** Without WHERE, ALL rows get updated!

**Examples:**
```sql
-- Update single field
UPDATE users SET status = 'INACTIVE' WHERE id = 1;

-- Update multiple fields
UPDATE users 
SET username = 'new_name', email = 'new@test.com'
WHERE id = 1;

-- Update multiple rows
UPDATE users SET status = 'INACTIVE' WHERE status = 'PENDING';

-- ❌ DANGEROUS - updates ALL rows!
UPDATE users SET status = 'DELETED';  -- Don't do this!
```

**When to use UPDATE:**
- Edit user profile
- Change status
- Bulk status change

---

### 🔟 DELETE - Removing Data

**Basic Structure:**
```sql
DELETE FROM table WHERE condition;
```

**⚠️ ALWAYS use WHERE with DELETE!** Without WHERE, ALL rows get deleted!

**Examples:**
```sql
-- Delete single row
DELETE FROM users WHERE id = 1;

-- Delete multiple rows
DELETE FROM users WHERE status = 'DELETED';

-- Delete by list of IDs
DELETE FROM users WHERE id IN (1, 2, 3);

-- ❌ DANGEROUS - deletes ALL rows!
DELETE FROM users;  -- Don't do this!
```

**When to use DELETE:**
- Remove user account
- Clean up old data
- Remove invalid records

---

### 📊 JOIN - Combining Tables

**Why use JOIN:**  
Get data from multiple related tables.

**Example Tables:**
```
users table:          orders table:
+----+----------+     +----+---------+--------+
| id | username |     | id | user_id | amount |
+----+----------+     +----+---------+--------+
| 1  | john     |     | 1  | 1       | 100    |
| 2  | jane     |     | 2  | 1       | 200    |
+----+----------+     | 3  | 2       | 150    |
                      +----+---------+--------+
```

**INNER JOIN - Only matching rows:**
```sql
SELECT users.username, orders.amount
FROM users
INNER JOIN orders ON users.id = orders.user_id;

-- Result: john-100, john-200, jane-150
```

**LEFT JOIN - All from left table + matching from right:**
```sql
SELECT users.username, orders.amount
FROM users
LEFT JOIN orders ON users.id = orders.user_id;

-- If user has no orders, amount will be NULL
```

**When to use JOIN:**
- User with their orders
- Product with category name
- Any related data from multiple tables

---

### 📈 Aggregate Functions

**Why use Aggregates:**  
Calculate summaries (count, total, average).

| Function | Purpose | Example |
|----------|---------|---------|
| `COUNT()` | Count rows | `COUNT(*)` |
| `SUM()` | Total | `SUM(amount)` |
| `AVG()` | Average | `AVG(price)` |
| `MAX()` | Maximum | `MAX(salary)` |
| `MIN()` | Minimum | `MIN(age)` |

**Examples:**
```sql
-- Count all users
SELECT COUNT(*) FROM users;

-- Count active users
SELECT COUNT(*) FROM users WHERE status = 'ACTIVE';

-- Sum of all order amounts
SELECT SUM(amount) FROM orders;

-- Average order amount
SELECT AVG(amount) FROM orders;
```

---

### 📊 GROUP BY - Grouping Results

**Why use GROUP BY:**  
Get aggregates per group (count per status, sum per user).

```sql
-- Count users by status
SELECT status, COUNT(*) as count
FROM users
GROUP BY status;

-- Result:
-- ACTIVE   | 50
-- INACTIVE | 20
-- PENDING  | 10
```

**With HAVING (filter groups):**
```sql
-- Statuses with more than 10 users
SELECT status, COUNT(*) as count
FROM users
GROUP BY status
HAVING COUNT(*) > 10;
```

**WHERE vs HAVING:**
- `WHERE` - filters rows BEFORE grouping
- `HAVING` - filters groups AFTER grouping

---

### 🔄 Complete Query Order

```sql
SELECT columns                    -- 5. What to show
FROM table                        -- 1. Which table
JOIN other_table ON condition     -- 2. Combine tables
WHERE condition                   -- 3. Filter rows
GROUP BY column                   -- 4. Group rows
HAVING condition                  -- 6. Filter groups
ORDER BY column                   -- 7. Sort results
LIMIT number OFFSET number        -- 8. Pagination
```

**Execution Order (how database processes):**
1. FROM / JOIN
2. WHERE
3. GROUP BY
4. HAVING
5. SELECT
6. ORDER BY
7. LIMIT / OFFSET

---

### 📋 SQL Cheat Sheet

```sql
-- 🔍 SELECT (Read)
SELECT * FROM users;
SELECT id, name FROM users WHERE status = 'ACTIVE';
SELECT * FROM users WHERE name LIKE '%john%';
SELECT * FROM users WHERE id IN (1, 2, 3);
SELECT * FROM users WHERE age BETWEEN 18 AND 30;
SELECT * FROM users ORDER BY created_at DESC;
SELECT * FROM users LIMIT 10 OFFSET 20;

-- ➕ INSERT (Create)
INSERT INTO users (name, email) VALUES ('john', 'j@test.com');

-- ✏️ UPDATE (Modify) - Always use WHERE!
UPDATE users SET status = 'INACTIVE' WHERE id = 1;

-- ❌ DELETE (Remove) - Always use WHERE!
DELETE FROM users WHERE id = 1;

-- 📊 Aggregates
SELECT COUNT(*) FROM users;
SELECT status, COUNT(*) FROM users GROUP BY status;

-- 🔗 JOIN
SELECT u.name, o.amount 
FROM users u 
JOIN orders o ON u.id = o.user_id;
```

---

## 🔥 MyBatis Dynamic SQL Reference

> Quick reference for all Dynamic SQL features - copy & use!

### Overview Table

| Tag | Purpose | When to Use |
|-----|---------|-------------|
| `<if>` | Conditional SQL | Optional WHERE conditions |
| `<choose><when><otherwise>` | Switch logic | Mutually exclusive conditions |
| `<where>` | Smart WHERE | Auto-handle AND/OR prefix |
| `<set>` | Smart SET | Dynamic UPDATE fields |
| `<foreach>` | Loop | IN clause, batch operations |
| `<trim>` | Custom trimming | Advanced prefix/suffix control |
| `<sql>` + `<include>` | Reusable | Shared column lists |
| `<bind>` | Variables | LIKE patterns |

---

### 1️⃣ `<if>` - Conditional Filter

**Use Case:** Add WHERE condition only if value exists

```xml
<select id="searchUsers" resultMap="UserMap">
    SELECT * FROM users
    <where>
        <if test="username != null and username != ''">
            AND username = #{username}
        </if>
        <if test="email != null and email != ''">
            AND email = #{email}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>
```

**Generated SQL (if username="john", email=null, status="ACTIVE"):**
```sql
SELECT * FROM users WHERE username = 'john' AND status = 'ACTIVE'
```

**Key Points:**
- `<where>` automatically removes leading `AND`/`OR`
- Only adds `WHERE` if at least one condition is true
- Use `and` for null check: `test="value != null and value != ''"`

---

### 2️⃣ `<choose><when><otherwise>` - Switch Logic

**Use Case:** Only ONE condition should apply (mutually exclusive)

```xml
<select id="findUsers" resultMap="UserMap">
    SELECT * FROM users
    WHERE status =
    <choose>
        <when test="status == 'ACTIVE'">
            'ACTIVE'
        </when>
        <when test="status == 'INACTIVE'">
            'INACTIVE'
        </when>
        <otherwise>
            'PENDING'
        </otherwise>
    </choose>
</select>
```

**Common Pattern - Dynamic ORDER BY:**
```xml
ORDER BY
<choose>
    <when test="sortBy == 'username'">username</when>
    <when test="sortBy == 'email'">email</when>
    <when test="sortBy == 'createdAt'">created_at</when>
    <otherwise>id</otherwise>
</choose>
<choose>
    <when test="sortOrder == 'ASC'">ASC</when>
    <otherwise>DESC</otherwise>
</choose>
```

---

### 3️⃣ `<foreach>` - Loop Collections

**Use Case 1: IN Clause**
```xml
<select id="findByIds" resultMap="UserMap">
    SELECT * FROM users
    WHERE id IN
    <foreach collection="ids" item="id" open="(" close=")" separator=",">
        #{id}
    </foreach>
</select>
```

**Generated SQL:** `SELECT * FROM users WHERE id IN (1, 2, 3, 4, 5)`

**Use Case 2: Batch INSERT**
```xml
<insert id="batchInsert">
    INSERT INTO users (username, email, status)
    VALUES
    <foreach collection="users" item="user" separator=",">
        (#{user.username}, #{user.email}, #{user.status})
    </foreach>
</insert>
```

**Generated SQL:**
```sql
INSERT INTO users (username, email, status)
VALUES ('user1', 'a@test.com', 'ACTIVE'),
       ('user2', 'b@test.com', 'ACTIVE'),
       ('user3', 'c@test.com', 'INACTIVE')
```

**Use Case 3: Batch UPDATE**
```xml
<update id="batchUpdateStatus">
    UPDATE users
    SET status = #{status}
    WHERE id IN
    <foreach collection="ids" item="id" open="(" close=")" separator=",">
        #{id}
    </foreach>
</update>
```

**Use Case 4: Batch DELETE**
```xml
<delete id="batchDelete">
    DELETE FROM users
    WHERE id IN
    <foreach collection="ids" item="id" open="(" close=")" separator=",">
        #{id}
    </foreach>
</delete>
```

**`<foreach>` Attributes:**
| Attribute | Description | Example |
|-----------|-------------|---------|
| `collection` | Parameter name | `"ids"`, `"users"`, `"list"` |
| `item` | Current element variable | `"id"`, `"user"` |
| `index` | Current index (optional) | `"idx"` |
| `open` | Opening character | `"("` |
| `close` | Closing character | `")"` |
| `separator` | Between elements | `","` |

---

### 4️⃣ `<set>` - Dynamic UPDATE

**Use Case:** Update only non-null fields

```xml
<update id="dynamicUpdate">
    UPDATE users
    <set>
        <if test="username != null and username != ''">
            username = #{username},
        </if>
        <if test="email != null and email != ''">
            email = #{email},
        </if>
        <if test="status != null">
            status = #{status},
        </if>
    </set>
    WHERE id = #{id}
</update>
```

**If username="john", email=null, status="ACTIVE":**
```sql
UPDATE users SET username = 'john', status = 'ACTIVE' WHERE id = 1
```

**Key Points:**
- `<set>` automatically removes trailing commas
- Only adds `SET` if at least one field is present

---

### 5️⃣ `<trim>` - Custom Trimming

**Use Case:** More control than `<where>` or `<set>`

**Alternative to `<where>`:**
```xml
<trim prefix="WHERE" prefixOverrides="AND |OR ">
    <if test="username != null">
        AND username = #{username}
    </if>
    <if test="email != null">
        AND email = #{email}
    </if>
</trim>
```

**Alternative to `<set>`:**
```xml
<trim prefix="SET" suffixOverrides=",">
    <if test="username != null">
        username = #{username},
    </if>
    <if test="email != null">
        email = #{email},
    </if>
</trim>
```

**`<trim>` Attributes:**
| Attribute | Description |
|-----------|-------------|
| `prefix` | Add at beginning if content exists |
| `suffix` | Add at end if content exists |
| `prefixOverrides` | Remove from beginning (pipe `\|` separated) |
| `suffixOverrides` | Remove from end |

---

### 6️⃣ `<sql>` + `<include>` - Reusable Fragments

**Use Case:** Avoid repeating column lists

```xml
<!-- Define reusable fragment -->
<sql id="userColumns">
    id, username, email, status, created_at
</sql>

<sql id="activeCondition">
    AND status = 'ACTIVE'
</sql>

<!-- Use fragment -->
<select id="findAll" resultMap="UserMap">
    SELECT <include refid="userColumns"/>
    FROM users
    ORDER BY id DESC
</select>

<select id="findActiveUsers" resultMap="UserMap">
    SELECT <include refid="userColumns"/>
    FROM users
    WHERE 1=1
    <include refid="activeCondition"/>
</select>
```

---

### 7️⃣ `<bind>` - Create Variables

**Use Case:** LIKE patterns, computed values

```xml
<select id="searchByKeyword" resultMap="UserMap">
    <bind name="pattern" value="'%' + keyword + '%'"/>
    
    SELECT * FROM users
    WHERE username LIKE #{pattern}
       OR email LIKE #{pattern}
</select>
```

**Alternative (without bind):**
```xml
WHERE username LIKE CONCAT('%', #{keyword}, '%')
```

---

### 📋 Complete Example - Advanced Search

```xml
<select id="advancedSearch" resultMap="UserMap">
    SELECT <include refid="userColumns"/>
    FROM users
    <where>
        <!-- Simple conditions.html -->
        <if test="username != null and username != ''">
            AND username LIKE CONCAT('%', #{username}, '%')
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
        
        <!-- Date range -->
        <if test="startDate != null">
            AND created_at &gt;= #{startDate}
        </if>
        <if test="endDate != null">
            AND created_at &lt;= #{endDate}
        </if>
        
        <!-- Multiple values (IN clause) -->
        <if test="ids != null and ids.size() > 0">
            AND id IN
            <foreach collection="ids" item="id" open="(" close=")" separator=",">
                #{id}
            </foreach>
        </if>
        
        <if test="statuses != null and statuses.size() > 0">
            AND status IN
            <foreach collection="statuses" item="st" open="(" close=")" separator=",">
                #{st}
            </foreach>
        </if>
    </where>
    
    <!-- Dynamic sorting -->
    ORDER BY
    <choose>
        <when test="sortBy == 'username'">username</when>
        <when test="sortBy == 'email'">email</when>
        <otherwise>id</otherwise>
    </choose>
    <choose>
        <when test="sortOrder == 'ASC'">ASC</when>
        <otherwise>DESC</otherwise>
    </choose>
    
    <!-- Pagination -->
    <if test="size != null and offset != null">
        LIMIT #{size} OFFSET #{offset}
    </if>
</select>
```

---

### ⚠️ Special Characters in XML

| Character | XML Escape | Description |
|-----------|------------|-------------|
| `<` | `&lt;` | Less than |
| `>` | `&gt;` | Greater than |
| `&` | `&amp;` | Ampersand |
| `"` | `&quot;` | Double quote |
| `'` | `&apos;` | Single quote |

**Example:**
```xml
<!-- Wrong -->
<if test="age < 18">

<!-- Correct -->
<if test="age &lt; 18">
```

---

### 🔧 Enable SQL Logging

Add to `application.properties`:
```properties
# Show SQL statements
logging.level.com.heang.springmybatistest.mapper=DEBUG

# Show SQL with parameter values
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

---

### 📁 Practice Files

| File | Description |
|------|-------------|
| `mapper/DynamicSqlPracticeMapper.xml` | All examples with comments |
| `mapper/DynamicSqlPracticeMapper.java` | Mapper interface |
| `service/DynamicSqlPracticeServiceImpl.java` | Service with explanations |
| `controller/DynamicSqlPracticeController.java` | REST endpoints to test |
| `dto/UserSearchRequest.java` | Search request DTO |

---

### 🧪 Practice Endpoints & Checklist

Start the app (`./mvnw spring-boot:run`) then test with Postman or curl:

| Exercise | Tag | Endpoint |
|----------|-----|----------|
| Conditional filter | `<if>` | `GET /api/practice/search/if?username=john&status=ACTIVE` |
| Dynamic sorting | `<choose>` | `GET /api/practice/search/choose?sortBy=username&sortOrder=ASC` |
| IN clause | `<foreach>` | `GET /api/practice/users/by-ids?ids=1,2,3` |
| Multiple statuses | `<foreach>` | `GET /api/practice/users/by-statuses?statuses=ACTIVE,INACTIVE` |
| Batch INSERT | `<foreach>` | `POST /api/practice/users/batch` |
| Batch UPDATE | `<foreach>` | `PUT /api/practice/users/batch-status` |
| Batch DELETE | `<foreach>` | `DELETE /api/practice/users/batch?ids=10,11,12` |
| Dynamic UPDATE | `<set>` | `PATCH /api/practice/users/{id}` |
| Advanced search | All combined | `POST /api/practice/search/advanced` |

**Practice Checklist:**
- [ ] Test `<if>` with different filter combinations
- [ ] Test `<choose>` with different sort options
- [ ] Test `<foreach>` with IN clause
- [ ] Test batch INSERT with JSON array
- [ ] Test batch UPDATE status
- [ ] Test batch DELETE
- [ ] Test `<set>` with partial updates
- [ ] Test advanced search with pagination
- [ ] Enable SQL logging and observe generated SQL

---

## 📚 Learning Roadmap

### Why This Stack? (왜 이 기술 스택인가?)

**Spring + MyBatis + JSP + jQuery** is the standard in Korean enterprise because:

| Reason | Explanation |
|--------|-------------|
| 🏛️ **Government Standard** | eGovFrame (전자정부 표준프레임워크) is based on Spring |
| 🏢 **SI Companies** | Samsung SDS, LG CNS standardized on this |
| 👨‍💻 **Developer Pool** | Most Korean developers trained on this stack |
| 🔒 **Stability** | Proven over 15+ years |
| 📊 **Oracle Compatible** | MyBatis works well with Oracle DB |

---

### 🇰🇷 Deep Dive: Why Korean Enterprise Uses This Stack

#### 1. Historical Background (역사적 배경)

**Early 2000s - Java Dominance**
- Korean government strongly promoted **Java** for public sector projects
- **전자정부 표준프레임워크 (eGovFrame)** was built on Spring
- All government projects were required to use this stack
- This became the de facto standard for enterprise development

**The Stack Evolution:**
```
1990s: Pure Servlets + JSP
2000s: Struts + JSP + JDBC
2005+: Spring + MyBatis + JSP + jQuery  ← Current Standard
2015+: Spring Boot + JPA (slowly adopting)
```

---

#### 2. Why Each Technology? (각 기술 선택 이유)

##### 🌱 Spring Framework
| Reason | Explanation |
|--------|-------------|
| **Government Standard** | eGovFrame is based on Spring |
| **Enterprise Features** | Transaction, Security, AOP |
| **Large Community** | Many Korean developers know it |
| **Stability** | Proven over 15+ years |

##### 🗄️ MyBatis (vs JPA/Hibernate)
| Reason | Explanation |
|--------|-------------|
| **SQL Control** | Korean developers prefer writing raw SQL |
| **Complex Queries** | Many legacy systems have complex joins |
| **Oracle Compatibility** | Most Korean enterprises use Oracle DB |
| **Learning Curve** | Easier to learn than JPA |
| **Performance Tuning** | Direct SQL optimization possible |

```xml
<!-- MyBatis allows direct SQL control -->
<select id="getUsers" resultType="User">
    SELECT * FROM users 
    WHERE status = #{status}
    AND created_at > SYSDATE - 30  <!-- Oracle specific -->
</select>
```

##### 📄 JSP (vs Thymeleaf, React)
| Reason | Explanation |
|--------|-------------|
| **Server-Side Rendering** | Traditional approach, well understood |
| **JSTL Tags** | Easy HTML generation |
| **Legacy Systems** | Many existing systems use JSP |
| **No Build Process** | No npm, webpack needed |
| **Government Compliance** | eGovFrame uses JSP |

##### 💻 jQuery (vs React, Vue)
| Reason | Explanation |
|--------|-------------|
| **DOM Manipulation** | Simple and direct |
| **AJAX Support** | Easy $.ajax() calls |
| **Plugin Ecosystem** | Many UI plugins available |
| **No Build Tools** | Just include via CDN |
| **Wide Browser Support** | Works on old browsers (IE) |
| **Fast Development** | Quick to implement features |

---

#### 3. Korean IT Industry Characteristics (한국 IT 산업 특성)

##### 🏢 Large Enterprise Culture
- **SI Companies** (삼성SDS, LG CNS, SK C&C) dominate the market
- Standardized tech stacks across projects
- Risk-averse: prefer proven technologies
- Long-term maintenance contracts (5-10 years)

##### 👨‍💼 Developer Workforce
- Most developers trained on this stack in academies (학원)
- Job postings require Spring + MyBatis experience
- Easier to find replacement developers
- Consistent code style across teams

##### 📋 Typical Project Requirements
```
Korean Enterprise Project Requirements:
✅ Must use eGovFrame (Spring-based)
✅ Must support Internet Explorer (legacy)
✅ Must connect to Oracle Database
✅ Must integrate with 공인인증서 (digital certificates)
✅ Must follow 행정안전부 guidelines
```

---

#### 4. Comparison: Traditional vs Modern Stack

| Aspect | Traditional (Korea) | Modern (Global) |
|--------|---------------------|-----------------|
| Backend | Spring MVC | Spring Boot |
| ORM | MyBatis (SQL) | JPA/Hibernate |
| View | JSP | React/Vue/Thymeleaf |
| Frontend | jQuery | React/Vue |
| Database | Oracle | PostgreSQL/MySQL |
| Build | Maven/Ant | Gradle |
| API Style | Form Submit | REST API |

---

#### 5. Why It's Still Used in 2024-2026

##### ✅ Advantages
1. **Stability** - Proven over many years
2. **Maintainability** - Easy to find developers
3. **Government Compliance** - Required for public projects
4. **Legacy Integration** - Works with old systems
5. **No Build Complexity** - No npm, webpack issues

##### ❌ Disadvantages
1. **Outdated** - Not following modern trends
2. **Poor UX** - Full page reloads
3. **Hard to Test** - Tightly coupled code
4. **Security Risks** - XSS vulnerabilities with JSP
5. **Talent Shortage** - Young developers prefer React/Vue

---

#### 6. The Future (미래 전망)

##### Gradual Transition
```
Current: Spring + MyBatis + JSP + jQuery
    ↓
Phase 1: Spring Boot + MyBatis + JSP + jQuery
    ↓
Phase 2: Spring Boot + MyBatis/JPA + Thymeleaf + Vue
    ↓
Future:  Spring Boot + JPA + React/Vue (SPA)
```

##### New Government Standard (2024+)
- 전자정부 표준프레임워크 4.0 now supports:
  - Spring Boot
  - JPA (optional)
  - Vue.js (optional)
  - REST API

---

#### 7. Summary (요약)

**Spring + MyBatis + JSP + jQuery** is popular in Korea because:

| # | Reason |
|---|--------|
| 1 | 🏛️ **Government mandated** it through eGovFrame |
| 2 | 🏢 **Large SI companies** standardized on it |
| 3 | 👨‍💻 **Developer availability** - many trained on this stack |
| 4 | 🔒 **Risk aversion** - enterprises prefer proven tech |
| 5 | 🔄 **Legacy systems** - need to maintain old code |
| 6 | 💰 **Cost efficiency** - no need for specialized React/Vue developers |

> **For your career:** This stack is highly valuable for working in Korean enterprise/government projects. Understanding it well will give you many job opportunities! 🚀

---

### Phase 1: Master Fundamentals (1-2 months)

#### Java Core
```
Must Know:
├── OOP - Class, Interface, Inheritance
├── Collections - List, Map, Set
├── Exception Handling - try/catch
├── Stream API - filter, map, collect
└── Lambda - () -> {}
```

#### SQL Mastery
```sql
-- Practice these patterns!
SELECT u.*, COUNT(o.id) as order_count
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE u.status = 'active'
GROUP BY u.id
HAVING COUNT(o.id) > 5
ORDER BY order_count DESC;
```

---

### Phase 2: Deep Dive (2-3 months)

#### Spring Framework
| Concept | Annotation | Purpose |
|---------|------------|---------|
| IoC/DI | `@Autowired`, `@Component` | Object management |
| MVC | `@Controller`, `@RequestMapping` | Web layer |
| REST | `@RestController`, `@RequestBody` | API development |
| Transaction | `@Transactional` | Data integrity |

#### MyBatis Dynamic SQL
```xml
<!-- Dynamic WHERE clause -->
<select id="searchUsers" resultType="User">
    SELECT * FROM users
    <where>
        <if test="username != null">
            AND username LIKE '%' || #{username} || '%'
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>

<!-- IN clause with foreach -->
<select id="getUsersByIds" resultType="User">
    SELECT * FROM users
    WHERE id IN
    <foreach collection="ids" item="id" open="(" separator="," close=")">
        #{id}
    </foreach>
</select>
```

#### JSP & JSTL
```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!-- Loop with status -->
<c:forEach var="user" items="${users}" varStatus="status">
    <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
        <td>${status.count}</td>
        <td><c:out value="${user.username}"/></td>
    </tr>
</c:forEach>

<!-- Date formatting -->
<fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm"/>

<!-- Number formatting -->
<fmt:formatNumber value="${amount}" pattern="#,###"/>원
```

#### jQuery AJAX
```javascript
// Standard AJAX pattern
$.ajax({
    url: '/users',
    type: 'GET',
    dataType: 'json',
    success: function(result) {
        if (result.data) {
            renderTable(result.data);
        }
    },
    error: function(xhr, status, error) {
        alert('Error: ' + error);
    }
});

// POST with JSON
$.ajax({
    url: '/users',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
        username: $('#username').val(),
        email: $('#email').val()
    }),
    success: function(result) {
        alert('Success!');
    }
});
```

---

### Phase 3: Enterprise Patterns (2-3 months)

#### Practice Projects
```
1. 게시판 (Board System)
   ├── 글 목록 (페이징, 검색)
   ├── 글 작성/수정/삭제
   ├── 댓글 기능
   └── 파일 첨부

2. 직원 관리 (Employee Management)
   ├── 직원 CRUD
   ├── 부서 관리
   ├── 로그인/권한
   └── 엑셀 다운로드

3. 주문 관리 (Order Management)
   ├── 상품 관리
   ├── 주문 처리
   └── 매출 통계
```

---

### Skills Checklist

#### Junior Level (1년차)
- [ ] CRUD 구현
- [ ] MyBatis XML Mapper
- [ ] JSP + JSTL
- [ ] jQuery AJAX
- [ ] 페이징 처리
- [ ] 기본 검색

#### Mid Level (2-3년차)
- [ ] 복잡한 JOIN 쿼리
- [ ] Spring Security
- [ ] Transaction 관리
- [ ] Exception Handling
- [ ] 파일 업로드/다운로드
- [ ] 엑셀 처리 (Apache POI)

#### Senior Level (4년차+)
- [ ] 시스템 설계
- [ ] 성능 최적화
- [ ] 코드 리뷰
- [ ] 기술 선택/의사결정

---

## 📖 Study Resources

### Korean Resources (한국어)
| Type | Resource | Link |
|------|----------|------|
| Framework | 전자정부 표준프레임워크 | https://www.egovframe.go.kr |
| Courses | 인프런 | https://www.inflearn.com |
| Community | OKKY | https://okky.kr |
| YouTube | 뉴렉처 | Search on YouTube |

### English Resources
| Type | Resource | Link |
|------|----------|------|
| Spring | Baeldung | https://www.baeldung.com |
| MyBatis | Official Docs | https://mybatis.org/mybatis-3 |
| jQuery | API Docs | https://api.jquery.com |

---

## 💡 Tips for Learning

### Daily Practice Routine
```
Morning (1-2 hours):
├── Read documentation/tutorial
└── Learn new concept

Afternoon (2-3 hours):
├── Code practice
├── Build features
└── Debug issues

Evening (1 hour):
├── Review what you learned
└── Plan tomorrow
```

### Key Advice
```
1. 매일 코딩하세요 (Code every day)
2. 에러를 두려워하지 마세요 (Don't fear errors)
3. 프로젝트에 기능 추가하세요 (Keep adding features)
4. GitHub에 올리세요 (Build your portfolio)
```

---

## 🏛️ eGovFrame Simple Homepage — Backend Reference Guide

> This is a translated and explained reference from the official eGovFrame Simple Homepage Backend project.
> Original source: [egovframe-template-simple-backend](https://github.com/eGovFramework/egovframe-template-simple-backend)
> Understanding this project is essential for working in Korean government/enterprise projects.

---

### What Is This Project?

The **eGovFrame Simple Homepage Backend** is an official example project by the Korean government.
It shows how to build a backend API server using the eGovFrame standard framework.

**Key point:** This project separates Backend (Spring Boot API) and Frontend (React) — unlike the old style where JSP was served directly from the backend.

```
Old style (monolithic):
  Spring Boot → JSP → Browser

New eGovFrame style (separated):
  Spring Boot API → JSON → React Frontend → Browser
```

This is the modern direction Korean government projects are moving toward — but you will still see the old JSP style in existing legacy systems.

---

### Environment

| Item | Version |
|------|---------|
| JDK | 17 |
| Jakarta EE | 10 |
| Servlet | 6.0 |
| Spring Framework | 6.2.x |
| Spring Boot | 3.x |

> Note: This project uses **Spring Boot 3.x** with **Jakarta EE 10**. The package namespace changed from `javax.*` → `jakarta.*`. Be careful when reading older eGovFrame code that still uses `javax.*`.

---

### How to Run the Backend

```bash
# Run with Maven
mvn spring-boot:run

# Run a specific profile (dev, prod, etc.)
java -jar <jar-file> --spring.profiles.active=<profile-name>
```

In IDE: Right-click project → Run As → Spring Boot App

After starting, access at: `http://localhost:8080/`

---

### Swagger API Docs

| Version | URL |
|---------|-----|
| Swagger 3.x (current) | `http://localhost:8080/swagger-ui/index.html` |
| Swagger 2.x (old reference) | `http://localhost:8080/swagger-ui.html` |

**Authentication rules:**
- `GET` requests → no JWT token needed (public)
- `POST`, `PUT`, `DELETE` → JWT token required
- Without token → `401 / 403` error: "인가된 사용자가 아닙니다" (Unauthorized user)

**How to get JWT token in Swagger:**
1. Find `/auth/login-jwt` endpoint → click **Try it out**
2. Enter credentials: `admin` / `1` → click **Execute**
3. Copy the token value from `"jToken": "..."` in the response
4. Click **Authorize** button at top of Swagger page
5. Paste the token → click **Authorize**
6. Now all secured endpoints are accessible

---

### Frontend (React)

The frontend is a separate React project:
```
Backend:  egovframe-template-simple-backend  (Spring Boot API)
Frontend: egovframe-template-simple-react    (React)
```
They communicate via REST API + JWT — not JSP.

---

### Key Architecture Changes from Old eGovFrame

#### 1. Configuration: XML → Java Config

Old eGovFrame used XML files for everything. This project converts them to Java `@Configuration` classes:

| Old XML File | New Java Config | What It Does |
|-------------|-----------------|--------------|
| `web.xml` | `WebApplicationInitializer` impl | App startup, servlet setup |
| `context-common.xml` | `@Configuration` class | Component scan, message source |
| `context-datasource.xml` | `@Configuration` class | Database connection (HSQL/DBCP) |
| `context-mapper.xml` | `@Configuration` class | MyBatis SqlSessionFactory |
| `context-transaction.xml` | `@Configuration` class | Transaction manager (AOP) |
| `context-aspect.xml` | `@EnableAspectJAutoProxy` | Exception handling AOP |
| `context-idgen.xml` | `@Configuration` class | ID generator (table-based) |
| `context-properties.xml` | `@Configuration` class | Global properties (pageSize, filePath) |
| `context-validator.xml` | `@Configuration` class | Commons Validator rules |
| `context-whitelist.xml` | `@Configuration` class | Page link whitelist |

> **Why this matters for you:** When you join a Korean company, you will see both styles — XML config (old legacy) and Java config (newer). Knowing both is important.

#### 2. View → API

Old style: Controller returns a view name → JSP renders HTML

New style: Controller returns JSON → React renders UI

```java
// Old eGovFrame style (returns JSP view)
@RequestMapping("/board/list.do")
public String list(ModelMap model) {
    model.addAttribute("boards", service.selectList());
    return "board/list";  // → list.jsp
}

// New eGovFrame API style (returns JSON)
@GetMapping("/board/list")
public ResponseEntity<List<BoardVO>> list() {
    return ResponseEntity.ok(service.selectList());
}
```

API controllers are named with `~ApiController.java` suffix — easy to identify.

---

### eGovFrame Key Concepts Explained

#### Context Hierarchy (Root vs Servlet WebApplicationContext)

```
Root WebApplicationContext          ← loaded by ContextLoaderListener
  ├── Service beans
  ├── DAO beans
  ├── DataSource beans
  └── Transaction beans

Servlet WebApplicationContext       ← loaded by DispatcherServlet
  ├── Controller beans
  ├── ViewResolver
  └── HandlerMapping
      (can access Root context beans)
```

Old Spring MVC needed this split. Spring Boot handles it automatically — but legacy eGovFrame code uses this structure.

#### ID Generator (eGovFrame특유 기능)

eGovFrame has a built-in table-based ID generator — instead of DB sequences or auto-increment:

```java
// ID is generated from a management table (not DB SERIAL/SEQUENCE)
EgovTableIdGnrServiceImpl idGenerator;

String newId = idGenerator.getNextStringId(); // e.g., "BOARD_000001"
```

You'll see this in Korean government systems where IDs must follow a specific format.

#### EgovAbstractDAO (The DAO Base Class)

All DAO classes in eGovFrame extend this base class:

```java
public class BoardDAO extends EgovAbstractDAO {

    // selectList, selectOne, insert, update, delete
    // — all inherited from EgovAbstractDAO
    // — calls MyBatis XML by namespace + id string

    public List<BoardVO> selectBoardList(BoardVO vo) {
        return selectList("BoardMapper.selectBoardList", vo);  // calls XML
    }

    public BoardVO selectBoard(BoardVO vo) {
        return (BoardVO) selectOne("BoardMapper.selectBoard", vo);
    }

    public void insertBoard(BoardVO vo) {
        insert("BoardMapper.insertBoard", vo);
    }
}
```

This is **Pattern A** (SqlSessionTemplate style) that you built in this project's `BoardDAO.java` — same concept.

#### AOP Transaction Management

eGovFrame uses AOP to apply `@Transactional` via config — not annotation on each method:

```xml
<!-- Old XML style — transaction applied to all select*/insert*/update*/delete* methods -->
<tx:advice id="txAdvice">
    <tx:attributes>
        <tx:method name="select*" read-only="true"/>
        <tx:method name="insert*"/>
        <tx:method name="update*"/>
        <tx:method name="delete*"/>
    </tx:attributes>
</tx:advice>
```

This is why you see strict method naming (`selectXxx`, `insertXxx`) — the AOP transaction config targets methods by name prefix.

---

### eGovFrame Project Folder Structure (Typical)

```
src/main/java/
  egovframework/
    com/
      cmm/          ← common utilities
      cop/          ← cooperation (board, community)
        bbs/        ← bulletin board system (게시판)
          service/  ← BoardService.java + BoardVO.java
          web/      ← BoardController.java
      uat/          ← user access (login, auth)

src/main/resources/
  egovframework/
    mapper/         ← MyBatis XML files (not just /mapper/)
      com/
        cop/bbs/    ← BoardMapper.xml
  globals.properties ← eGovFrame global properties

src/main/webapp/
  WEB-INF/
    config/
      egovframework/
        spring/     ← context-*.xml files (old style)
```

> This is different from standard Spring Boot structure. When you see this folder layout, you know it's eGovFrame.

---

### globals.properties — eGovFrame Global Config

eGovFrame uses its own properties file on top of `application.properties`:

```properties
# Pagination
pageUnit=10         # rows per page
pageSize=10         # page block size (e.g. 1 2 3 ... 10)

# File upload
Globals.fileStorePath=/upload/

# DB type (used for pagination SQL generation)
Globals.DbType=oracle   # or mysql, altibase, tibero

# Default user
Globals.adminUser=admin
```

---

### Summary — What You Need to Know for Korean Enterprise Work

| Concept | Old eGovFrame | This Project (New) | Your Project Here |
|---------|--------------|-------------------|-------------------|
| View | JSP (server-side) | React (client-side) | JSP |
| Config | XML files | Java @Configuration | application.properties |
| DAO | EgovAbstractDAO | Spring Data / custom | SqlSessionTemplate (BoardDAO) |
| URL style | `/board/list.do` | `/board/list` (REST) | `/board/list.do` |
| Auth | Session-based | JWT token | JWT token |
| SQL | MyBatis XML | MyBatis XML | MyBatis XML |

> Both styles exist in real Korean workplaces. You may maintain old JSP projects AND build new API projects at the same time.

---

## 🧩 eGovFrame Common Components (공통컴포넌트) — Reference Guide

> Translated and explained from the official eGovFrame Common Components 4.3.0 project.
> Original source: [egovframe-common-components](https://github.com/eGovFramework/egovframe-common-components)
> This is the **pre-built library** that all Korean government projects reuse — you will inherit and customize these components at work.

---

### What Are Common Components? (공통컴포넌트란?)

Common Components are a **collection of reusable application modules** built for Korean e-government projects.
Instead of building login, user management, or board features from scratch — Korean government projects plug in these pre-built components and customize them.

Think of it like a **starter kit** that every government project uses:
- Login + Authentication already built
- Board (게시판) already built
- User management already built
- Role/Permission system already built
- Utilities already built

You just configure and customize — not build from zero.

They follow **MVC architecture** on top of the eGovFrame runtime environment (Spring-based).

---

### Package Structure — What Each Folder Does

```
egovframe-common-components/
  ├─ script/                        ← SQL scripts for all 8 supported DBs
  └─ src/main/java/egovframework/com/
       ├─ cmm/                      ← Common shared classes (used by all modules)
       ├─ cop/                      ← Collaboration (board, community, schedule)
       ├─ dam/                      ← Digital Asset Management (knowledge map)
       ├─ ext/                      ← External integrations (LDAP, OAuth)
       ├─ sec/                      ← Security (role, group, permission management)
       ├─ ssi/ syi/                 ← System Integration / Interface management
       ├─ sts/                      ← Statistics (post stats, user stats)
       ├─ sym/                      ← System Management (codes, logs, menus)
       ├─ uat/                      ← Unified Authentication (login, certificate)
       ├─ uss/                      ← User Support (member, terms, notifications)
       └─ utl/                      ← Utilities (calendar, format, validation)
```

#### Detailed Explanation of Each Package

| Package | Korean Name | What It Contains |
|---------|-------------|-----------------|
| `cmm` | 공통 | Shared base classes, common utilities used across all modules |
| `cop` | 협업 | Board (게시판), community, schedule — the board you built follows this pattern |
| `dam` | 디지털자산관리 | Personal knowledge management, knowledge map |
| `ext` | 외부연동 | LDAP integration, OAuth external login |
| `sec` | 보안 | Role management, group management, permission (권한) control |
| `ssi/syi` | 시스템연계 | System interface, integration status management |
| `sts` | 통계 | Post statistics, user statistics dashboards |
| `sym` | 시스템관리 | Common code management, log management, menu management |
| `uat` | 통합인증 | Login, certificate-based authentication |
| `uss` | 사용자지원 | Member management, terms of service, notifications/alerts |
| `utl` | 유틸리티 | Calendar, format/calculation/conversion, validation helpers |

> **For you at Bizplay:** The most important packages are `cop` (board), `uat` (login), `sec` (permissions), `sym` (common codes), `uss` (member management). These are the ones you'll touch most.

---

### SQL Scripts — 8 Supported Databases

The `script/` folder contains DDL + DML for all 8 databases Korean government systems use:

| Database | Notes |
|----------|-------|
| **Oracle** | Most common in large Korean enterprise |
| **MySQL** | Common in smaller projects |
| **MariaDB** | Open-source Oracle alternative |
| **PostgreSQL** | What you use in this practice project |
| **Altibase** | Korean-made DB — used in some government systems |
| **Tibero** | Korean-made DB — used in military/government |
| **CUBRID** | Korean open-source DB — some government projects |
| **Goldilocks** | Korean-made DB |

> You will likely encounter **Oracle** or **Tibero** at Korean companies. The SQL syntax differs slightly — especially pagination (`ROWNUM` in Oracle vs `LIMIT` in PostgreSQL).

---

### How to Set Up and Run Common Components

1. Open **Eclipse IDE** (eGovFrame development environment uses Eclipse — not IntelliJ)
2. `File > Import` → import the project
3. Right-click project → `Maven > Update Project > Force Update of Snapshots/Releases`
4. Configure database in `globals.properties`:
   ```
   Location: src/main/resources/egovframework/egovProps/globals.properties
   ```
5. Set authentication/authorization method in same `globals.properties`
6. Right-click project → `Run As > Run on Server` (deploys to local Tomcat)
7. Open browser to check the running service

> **Key difference from Spring Boot:** eGovFrame projects run on **external Tomcat**, not embedded Tomcat. You deploy to a server — not just run `mvn spring-boot:run`.

---

### globals.properties — The Master Config File

This is the most important config file in any eGovFrame project. Location:
```
src/main/resources/egovframework/egovProps/globals.properties
```

```properties
# Database type — determines which SQL dialect to use
Globals.DbType = oracle          # oracle | mysql | altibase | tibero | cubrid | mariadb | postgres

# Database connection
Globals.url     = jdbc:oracle:thin:@localhost:1521:orcl
Globals.userName = scott
Globals.password = tiger

# File upload path
Globals.fileStorePath = /upload/

# Pagination settings
pageUnit = 10        # rows per page
pageSize = 10        # number of page buttons shown (1 2 3 ... 10)

# Authentication mode
Globals.Auth = session            # session | certificate

# Default admin user
Globals.adminUser = admin
```

> Every eGovFrame project has this file. When you join a new project, this is the **first file you read** to understand how the system is configured.

---

### Default Login Credentials (for local dev/testing)

| Username | Password | Role |
|----------|----------|------|
| `admin` | `1` | Administrator |

> Reference: [eGovFrame Login Info Guide](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:com:v4.3:init_table)

---

### How Board (cop/bbs) Relates to What You Built

The `cop/bbs` package is the **official eGovFrame board implementation**. Your `BoardMvcController` + `BoardDAO` + `BoardMapper.xml` in this project follows the exact same pattern:

```
eGovFrame cop/bbs/               Your project
─────────────────────────────────────────────
BoardController.java         →   BoardMvcController.java
BoardService.java            →   BoardService.java
BoardServiceImpl.java        →   BoardServiceImpl.java
BoardDAO.java                →   BoardDAO.java (SqlSessionTemplate)
BoardMapper.xml              →   BoardMapper.xml
BoardVO.java                 →   BoardVO.java
```

The difference: eGovFrame's board has more features (file attach, reply, category, pagination with `ComDefaultVO`). Your project is a simplified version of the same pattern.

---

### ComDefaultVO — eGovFrame's Base Search VO

In real eGovFrame projects, every search VO extends `ComDefaultVO`:

```java
// Your BoardVO is simple:
public class BoardVO {
    private Long boardSn;
    private String boardTitle;
    // ...
}

// eGovFrame style — extends ComDefaultVO for pagination/search:
public class BoardVO extends ComDefaultVO {
    private String bbsId;
    private String nttId;
    private String nttSj;    // subject (제목)
    private String nttCn;    // content (내용)
    // ... many more fields
}

// ComDefaultVO provides:
public class ComDefaultVO {
    private int pageIndex;       // current page number
    private int pageUnit;        // rows per page (from globals.properties)
    private int pageSize;        // page block size
    private String searchCondition;  // search type (title, content, writer)
    private String searchKeyword;    // search keyword
    // ...
}
```

When you see `extends ComDefaultVO` in Korean enterprise code — that's eGovFrame pagination built in.

---

### References

| Resource | Description |
|----------|-------------|
| [Common Components Wiki Guide](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:com:v4.3:init) | Full usage guide (Korean) |
| [Dev Environment Download](https://www.egovframe.go.kr/home/sub.do?menuNo=94) | Eclipse-based eGovFrame IDE |
| [Common Components Download](https://www.egovframe.go.kr/home/sub.do?menuNo=49) | Download the full component package |
| [Default Login Info](https://www.egovframe.go.kr/wiki/doku.php?id=egovframework:com:v4.3:init_table) | DB setup + default admin credentials |

---

## 📚 Best Resources to Learn by Yourself

> Ordered by priority — start from top, go down as you progress.

---

### 1. MyBatis (Most Important for Korean Enterprise)

| Resource | Type | Why |
|----------|------|-----|
| [MyBatis Official Docs](https://mybatis.org/mybatis-3/) | Docs | Best source — read XML Mapper + Dynamic SQL sections |
| [MyBatis Spring Docs](https://mybatis.org/spring/) | Docs | How MyBatis integrates with Spring |
| [MyBatis Tutorial - Baeldung](https://www.baeldung.com/mybatis) | Article | Short and practical, good for beginners |

**Focus chapters:**
- `sqlSessionTemplate` — how DAO calls SQL
- `resultMap` — how DB columns map to Java fields
- Dynamic SQL: `<if>`, `<where>`, `<set>`, `<foreach>` — used everywhere in Korean projects

---

### 2. eGovFrame (Korean Government Standard Framework)

| Resource | Type | Why |
|----------|------|-----|
| [eGovFrame Official](https://www.egovframe.go.kr/) | Docs | Official Korean government framework site |
| [eGovFrame Dev Guide (Korean)](https://www.egovframe.go.kr/wiki/doku.php) | Wiki | Full guide — Persistence Layer, Service Layer, Web Layer |
| [eGovFrame GitHub](https://github.com/eGovFramework) | Code | Read real project source code |

**Focus sections:**
- Persistence Layer (DAO pattern using `EgovAbstractDAO`)
- `EgovDefaultAbstractDAO` — base class you'll extend at work
- `ComDefaultVO` — base VO with `pageIndex`, `pageSize`, `searchKeyword`

---

### 3. Oracle SQL (Most Korean Companies Use Oracle)

| Resource | Type | Why |
|----------|------|-----|
| [Oracle Live SQL](https://livesql.oracle.com/) | Practice | Free Oracle playground in browser — no install needed |
| [Oracle SQL Tutorial - w3schools](https://www.w3schools.com/sql/) | Article | Start here if SQL is not your strength |
| [Oracle Docs — SQL Language Reference](https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/) | Docs | Official reference |
| [HackerRank SQL](https://www.hackerrank.com/domains/sql) | Practice | Practice SQL problems — good for interviews |

**Focus topics:**
- `ROWNUM` pagination (no `LIMIT` in Oracle)
- `NVL()`, `DECODE()`, `CASE WHEN`
- Sequences (`CREATE SEQUENCE`, `seq.NEXTVAL`)
- `JOIN` types — Korean systems have heavy joins
- `TO_DATE()`, `TO_CHAR()` — date formatting

---

### 4. JSP + JSTL

| Resource | Type | Why |
|----------|------|-----|
| [JSTL Tutorial - JavaTPoint](https://www.javatpoint.com/jstl) | Article | Clear explanation of all JSTL tags |
| [Jakarta EE JSTL Docs](https://jakarta.ee/specifications/tags/) | Docs | Official spec |

**Focus tags:**
- `c:forEach`, `c:if`, `c:choose` — loops and conditions
- `c:out` — XSS-safe output (always use this)
- `fn:substring`, `fn:length` — string functions
- `fmt:formatDate` — only works with `java.util.Date` (NOT `LocalDateTime`)

---

### 5. Spring MVC (Not Spring Boot — the older style)

| Resource | Type | Why |
|----------|------|-----|
| [Spring MVC Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html) | Docs | Controller, ModelMap, @RequestParam, @ModelAttribute |
| [Baeldung Spring MVC](https://www.baeldung.com/spring-mvc-tutorial) | Article | Practical examples |

**Focus topics:**
- `@Controller` vs `@RestController`
- `ModelMap` / `ModelAndView`
- `@RequestParam` vs `@ModelAttribute` vs `@PathVariable`
- PRG Pattern (Post-Redirect-Get)

---

### 6. SVN (Version Control Used in Many Korean Companies)

| Resource | Type | Why |
|----------|------|-----|
| [SVN Book (free)](https://svnbook.red-bean.com/) | Book | The definitive SVN guide |
| [SVN Cheat Sheet](https://www.cheatography.com/davechild/cheat-sheets/subversion/) | Cheatsheet | Quick command reference |

**Must-know commands:**
```bash
svn checkout [url]        # clone a project (like git clone)
svn update                # pull latest changes (like git pull)
svn add [file]            # stage new file (like git add)
svn commit -m "message"   # commit changes (like git commit + push)
svn status                # see changed files (like git status)
svn log                   # see commit history (like git log)
svn diff                  # see changes (like git diff)
svn revert [file]         # undo local changes (like git checkout --)
```

---

### 7. Korean Developer Communities (Find Korean-specific answers)

| Resource | Type | Why |
|----------|------|-----|
| [Inflearn (인프런)](https://www.inflearn.com/) | Video Course | Korean Udemy — many eGovFrame + MyBatis + Spring courses in Korean |
| [OKKY](https://okky.kr/) | Community | Korean developer Q&A community |
| [Velog](https://velog.io/) | Blog | Korean developer blogs — search `mybatis`, `eGovFrame`, `스프링` |
| [Tistory](https://www.tistory.com/) | Blog | Many Korean enterprise dev blogs here |

**Recommended Inflearn searches:**
- `스프링 마이바티스` (Spring MyBatis)
- `전자정부 프레임워크` (eGovFrame)
- `스프링 MVC JSP`

---

### 8. English Resources for Deep Understanding

| Resource | Type | Why |
|----------|------|-----|
| [Baeldung](https://www.baeldung.com/) | Articles | Best English Spring + Java resource |
| [HowToDoInJava](https://howtodoinjava.com/) | Articles | Good MyBatis + Spring MVC articles |
| [StackOverflow](https://stackoverflow.com/) | Q&A | Search specific errors — most answers in English |

---

### Recommended Learning Order

```
Week 1  → MyBatis XML + Dynamic SQL (most important)
Week 2  → Oracle SQL basics + ROWNUM pagination
Week 3  → eGovFrame structure + EgovAbstractDAO pattern
Week 4  → JSP + JSTL (c:forEach, c:out, fn:)
Week 5  → SVN basic commands
Week 6+ → Read real eGovFrame project code on GitHub
```

---

## 🔗 Quick Reference Links

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MyBatis Documentation](https://mybatis.org/mybatis-3/)
- [jQuery API](https://api.jquery.com/)
- [전자정부 표준프레임워크](https://www.egovframe.go.kr/)
- [Oracle Live SQL (free practice)](https://livesql.oracle.com/)
- [Inflearn — Korean video courses](https://www.inflearn.com/)
- [OKKY — Korean dev community](https://okky.kr/)

---

## 🇰🇷 Working in Korea — Developer Guide

> For developers coming from modern stacks (Spring JPA, Next.js) transitioning to Korean enterprise development.

---

### Tech You'll See Every Day

| Tech | Why It's Used | What to Learn |
|------|--------------|---------------|
| **eGovFrame** | Government standard framework | Project structure, `globals.properties`, `EgovAbstractDAO` |
| **MyBatis XML** | Preferred over JPA — full SQL control | Dynamic SQL: `<if>`, `<where>`, `<foreach>` |
| **JSP + JSTL** | Legacy systems still running on this | `c:forEach`, `c:if`, `fn:` functions |
| **Oracle DB** | Most Korean enterprise uses Oracle | `ROWNUM`, sequences, `NVL()`, `DECODE()` |
| **iBatis** | Older version of MyBatis | Similar to MyBatis but different config |
| **Maven** | Almost no Gradle in Korean enterprise | Know `pom.xml` dependency structure |
| **SVN** | Many companies still use SVN, not Git | `svn checkout`, `svn commit`, `svn update` |

---

### Code Patterns You Must Know

**VO naming is strict:**
```
BoardVO          → data passed between layers
BoardDTO         → sometimes used for request/response
BoardDefaultVO   → eGovFrame search params (keyword, pageIndex, pageSize)
```

**eGovFrame DAO style** (you'll inherit this code):
```java
// Extends EgovAbstractDAO — not SqlSessionTemplate directly
public class BoardDAO extends EgovAbstractDAO {
    public List<BoardVO> selectBoardList(BoardVO vo) {
        return selectList("BoardMapper.selectBoardList", vo);
    }
}
```

**SQL method naming prefix convention:**
```
selectXxx        → SELECT list
selectXxxById    → SELECT single record
insertXxx        → INSERT
updateXxx        → UPDATE
deleteXxx        → DELETE (soft delete)
```
> Korean enterprise uses `select/insert/update/delete` prefixes — NOT `find/save/remove` like Spring Data JPA.

**Table naming:**
```
TB_BOARD_M       → M = Master table
TB_BOARD_D       → D = Detail/child table
CO_SMP_BOARD_M   → CO = Common, SMP = Sample
```

---

### Korean Coding Convention — Javadoc

Always write Korean Javadoc comments. Seniors expect it:
```java
/**
 * 게시글 목록 조회
 * @param boardVO 검색조건
 * @return 게시글 목록
 */
public List<BoardVO> selectBoardList(BoardVO boardVO) { ... }
```

---

### Things That Will Surprise You (JPA/Next.js → Korean Enterprise)

| What you expect | What you find |
|-----------------|---------------|
| `findById()` throws 404 automatically | You manually check `if (result == null)` everywhere |
| Lombok `@Builder` | Plain `@Getter @Setter` separately |
| `application.yml` | `globals.properties` + `application.properties` both |
| Git + PR workflow | SVN + direct commit to trunk |
| REST API with JSON | HTML form POST with `.do` URLs |
| `@DeleteMapping` | `@PostMapping` with hidden `boardSn` field |
| JPA auto-migration | Manual SQL scripts run by DBA |
| Docker + CI/CD | Manual WAR deploy to Tomcat on server |

---

### Oracle SQL You Must Learn

Most Korean enterprise uses **Oracle**, not PostgreSQL:

```sql
-- Pagination (no LIMIT/OFFSET in Oracle — use ROWNUM)
SELECT * FROM (
    SELECT ROWNUM AS rnum, A.*
    FROM (SELECT * FROM TB_BOARD_M ORDER BY REG_DT DESC) A
    WHERE ROWNUM <= 20
)
WHERE rnum >= 11;

-- Null handling (like COALESCE in PostgreSQL)
SELECT NVL(board_cn, '-') FROM TB_BOARD_M;

-- String concat
SELECT board_title || ' - ' || board_cn FROM TB_BOARD_M;

-- Sequence for auto-increment (Oracle has no SERIAL)
CREATE SEQUENCE board_seq START WITH 1 INCREMENT BY 1;
INSERT INTO TB_BOARD_M (board_sn, board_title) VALUES (board_seq.NEXTVAL, 'Hello');
```

---

### Tomcat WAR Deploy (You'll Do This)

Most Korean companies deploy as WAR to Apache Tomcat — not runnable JAR:
```bash
mvn clean package
# → copies target/app.war to Tomcat/webapps/
# → Tomcat auto-deploys on startup
```

Spring Boot can produce WAR — know how `SpringBootServletInitializer` works:
```java
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
```

---

### Board Flow — How Each Layer Connects

```
Browser
  ↓ HTTP Request (GET /board/list.do)
BoardMvcController       → receives request, calls service
  ↓
BoardService             → business logic, null checks, default values
  ↓
BoardDAO                 → SqlSessionTemplate executes SQL by namespace id
  ↓
BoardMapper.xml          → actual SQL, resultMap maps columns → BoardVO
  ↓
PostgreSQL / Oracle DB
  ↑ data returns up the chain as List<BoardVO>
Controller puts data in ModelMap
  ↓
list.jsp renders HTML with c:forEach
  ↓
Browser sees the page
```

**Compared to what you know (Next.js + JPA):**

| Step | Next.js / JPA | Korean Enterprise |
|------|--------------|-------------------|
| Route | `app/board/page.tsx` | `@GetMapping("/list.do")` |
| Data fetch | `fetch('/api/board')` or `repo.findAll()` | `boardService.findAll()` → `boardDAO.findAll()` |
| SQL | JPA auto-generates | You write every SQL in XML |
| Pass data to view | React `props` | `model.addAttribute("boards", boards)` |
| Render | React JSX | JSP `c:forEach` |
| Form submit | `fetch POST` with JSON | HTML `<form method="post">` |
| After POST | Client handles response | `redirect:/board/list.do` (PRG Pattern) |

---

### Workplace Culture Tips

- **Ask before refactoring** — propose changes, don't just do it. Hierarchy matters.
- **Comment everything in Korean** — seniors will review your code and expect Korean comments.
- **You inherit old code** — understand it before touching it. Legacy code exists for a reason.
- **Code review is informal** — often face-to-face, not PR reviews.
- **Documentation is expected** — keep your XML mapper comments clean and descriptive.
- **`// TODO:` is everywhere** — legacy Korean code has many unfinished sections. Don't panic.

---

### Quick Checklist Before Starting at a Korean Company

- [ ] Learn `svn` basic commands if they use SVN
- [ ] Understand `EgovAbstractDAO` pattern (eGovFrame)
- [ ] Know Oracle SQL — especially `ROWNUM` pagination
- [ ] Read 전자정부 표준프레임워크 (eGovFrame) docs
- [ ] Practice reading old MyBatis XML with complex dynamic SQL
- [ ] Get comfortable with IntelliJ + Tomcat local run config

---

### Honest Summary

Coming from **Spring JPA + Next.js**, you already understand the concepts — controllers, services, repositories, views. The Korean enterprise stack is **older and more manual**, but the logic is identical:

> Controller receives request → Service handles logic → DAO hits DB → return data → View renders it

The difference is **more boilerplate, more manual SQL, and more explicit layers**.

**Your JPA knowledge** helps you understand what MyBatis is replacing.
**Your Next.js knowledge** helps you understand what JSP is doing server-side.

You already know the hard parts. The Korean stack just makes them more visible.

**화이팅! 🚀**

---

## 📝 License

This project is for learning purposes.

---

## 👨‍💻 Author

Created for learning Korean Enterprise Web Development Stack.

---

*Last Updated: 2026-03-24*

---

## 📚 Thymeleaf Learning Notes

> Personal learning progress — Thymeleaf from basic to advanced.

### Week 1 — Basics (Completed ✅)

| Step | URL | Topic |
|---|---|---|
| 1 | `/learn/hello` | Basic variables: `th:text`, ternary, `th:utext` |
| 2 | `/learn/users` | Loops: `th:each`, stat (count, index, first, last) |
| 3 | `/learn/condition` | Conditions: `th:if`, `th:unless`, `th:switch` |
| 4 | `/learn/urls` | URL expressions: `@{}`, path variables, query params |

All practice pages are in `src/main/resources/templates/learn/`
Controller: `ThymeleafLearnController.java` (hardcoded data, no DB needed)

### Week 2 — Layouts & Fragments (Up Next)
- `th:fragment` — define reusable pieces (navbar, footer)
- `th:replace` / `th:insert` — use fragments in pages
- Base layout template — all pages share one layout

### Week 3 — Forms & Validation
- `th:object`, `th:field` — bind form to DTO
- `BindingResult` + `th:errors` — show validation errors

### Week 4 — Advanced
- Spring Security integration (`sec:authorize`)
- i18n / 다국어 처리 (`#{message.key}`)
- Utility objects: `#strings`, `#temporals`, `#numbers`, `#lists`
