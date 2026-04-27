-- =====================================================
-- Spring MyBatis Test — Master Schema
-- PostgreSQL | heang-dev-lab practice project
-- =====================================================
--
-- HOW TO USE THIS FILE
-- 1. Full reset (new DB):   run the entire file top to bottom
-- 2. Existing DB migration: run only the MIGRATIONS section at the bottom
--
-- CHANGELOG — what was added and when
-- -------------------------------------------------------
-- [initial]    users, co_smp_board_m, category, product, students
-- [2026-04-17] product.image_url column added
-- [2026-04-19] common_code table added
-- [2026-04-19] board_file table added
-- [2026-04-20] company table added
-- [2026-04-21] sidebar.html fragment created (not SQL — noted for reference)
-- [2026-04-22] budget_mng table added
-- [2026-04-27] co_bbs_m, co_comm_cd_d, bbs_seq added (FAQ management)
-- -------------------------------------------------------
--
-- HOW TO ADD A NEW TABLE (새 테이블 추가 방법)
-- Step 1: Add CREATE TABLE to the main section below (for full reset)
-- Step 2: Add CREATE TABLE IF NOT EXISTS to the MIGRATIONS section (for existing DB)
-- Step 3: Update the CHANGELOG above with [YYYY-MM-DD] description
-- Step 4: Update CLAUDE.md > Database Tables section
--
-- HOW TO ADD A NEW FUNCTION (새 함수 추가 방법)
-- Step 1: Add CREATE OR REPLACE FUNCTION to the FUNCTIONS section at the bottom
-- Step 2: Add a comment above the function: -- [YYYY-MM-DD] function_name — what it does
-- Step 3: Update the CHANGELOG above
-- =====================================================

-- Reset tables in dependency order
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS co_smp_board_m;

-- =====================================================
-- BOARD (Korean enterprise eGov style table name)
-- =====================================================
CREATE TABLE co_smp_board_m (
    board_sn    SERIAL PRIMARY KEY,
    board_title VARCHAR(500) NOT NULL,
    board_cn    TEXT,
    use_yn      CHAR(1) NOT NULL DEFAULT 'Y',
    data_reg_dt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_board_use_yn CHECK (use_yn IN ('Y', 'N'))
);

INSERT INTO co_smp_board_m (board_title, board_cn, use_yn) VALUES
    ('첫 번째 게시글', '안녕하세요! 첫 번째 게시글입니다.', 'Y'),
    ('Spring Boot 학습', 'Spring Boot와 MyBatis를 공부하고 있습니다.', 'Y'),
    ('Thymeleaf 연습', 'JSP 대신 Thymeleaf로 뷰를 구성합니다.', 'Y'),
    ('삭제된 게시글', '이 게시글은 소프트 삭제 상태입니다.', 'N'),
    ('PostgreSQL 설정', 'Railway에 PostgreSQL을 연결했습니다.', 'Y');

-- =====================================================
-- USERS
-- Supports both regular CRUD and dynamic SQL practice endpoints.
-- password defaults to the BCrypt hash for "password123" so batch
-- inserts that omit password still work.
-- =====================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL DEFAULT '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.',
    name VARCHAR(100),
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_users_role
        CHECK (role IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MANAGER')),
    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING'))
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_created_at ON users(created_at);

-- =====================================================
-- CATEGORY
-- =====================================================
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PRODUCT
-- =====================================================
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    price INTEGER NOT NULL DEFAULT 0,
    stock INTEGER NOT NULL DEFAULT 0,
    category_id INTEGER NOT NULL,
    image_url VARCHAR(500),                              -- product image path e.g. /uploads/abc.jpg
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE RESTRICT,
    CONSTRAINT chk_product_price CHECK (price >= 0),
    CONSTRAINT chk_product_stock CHECK (stock >= 0)
);

CREATE INDEX idx_product_category_id ON product(category_id);
CREATE INDEX idx_product_name ON product(name);
CREATE INDEX idx_product_price ON product(price);

-- =====================================================
-- SAMPLE DATA
-- Password for seeded users: password
-- =====================================================

INSERT INTO users (username, email, password, name, phone, role, status) VALUES
    ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Admin User', '010-0000-0000', 'ROLE_ADMIN', 'ACTIVE'),
    ('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'John Doe', '010-1234-5678', 'ROLE_USER', 'ACTIVE'),
    ('jane_doe', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Jane Doe', '010-9876-5432', 'ROLE_USER', 'ACTIVE'),
    ('inactive_user', 'inactive@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Inactive User', '010-1111-1111', 'ROLE_USER', 'INACTIVE'),
    ('suspended_user', 'suspended@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Suspended User', '010-2222-2222', 'ROLE_USER', 'SUSPENDED'),
    ('pending_user', 'pending@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Pending User', '010-3333-3333', 'ROLE_MANAGER', 'PENDING');

INSERT INTO category (name) VALUES
    ('Electronics'),
    ('Clothing'),
    ('Food'),
    ('Furniture'),
    ('Books');

INSERT INTO product (name, price, stock, category_id) VALUES
    ('Laptop', 1500000, 50, 1),
    ('Smartphone', 1200000, 100, 1),
    ('Tablet', 800000, 30, 1),
    ('Wireless Earbuds', 200000, 200, 1),
    ('T-Shirt', 29000, 300, 2),
    ('Jeans', 59000, 150, 2),
    ('Sneakers', 89000, 100, 2),
    ('Ramen Box', 12000, 1000, 3),
    ('Coffee Beans 1kg', 25000, 100, 3),
    ('Office Chair', 150000, 40, 4),
    ('Bookshelf', 120000, 20, 4),
    ('Java Programming', 32000, 100, 5),
    ('Clean Code', 33000, 60, 5),
    ('Effective Java', 36000, 50, 5),
    ('Limited Edition Laptop', 2500000, 3, 1),
    ('Almost Sold Out T-Shirt', 35000, 5, 2);

-- =====================================================
-- QUICK VERIFY QUERIES
-- =====================================================
SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS category_count FROM category;
SELECT COUNT(*) AS product_count FROM product;

SELECT id, username, email, role, status, created_at
FROM users
ORDER BY id;

SELECT
    p.id,
    p.name,
    c.name AS category_name,
    p.price,
    p.stock,
    p.created_at
FROM product p
LEFT JOIN category c ON p.category_id = c.id
ORDER BY p.id;

-- Product count per category (카테고리별 상품 개수)
SELECT
    c.id,
    c.name AS category_name,
    COUNT(p.id) AS product_count
FROM category c
LEFT JOIN product p ON c.id = p.category_id
GROUP BY c.id, c.name
ORDER BY c.id;

-- Low stock products (under 10) (재고 부족 상품 - 10개 미만)
SELECT
    p.id,
    p.name,
    c.name AS category_name,
    p.stock
FROM product p
LEFT JOIN category c ON p.category_id = c.id
WHERE p.stock < 10
ORDER BY p.stock;

-- User list (사용자 목록)
SELECT * FROM users ORDER BY id;

-- User count (사용자 수)
SELECT COUNT(*) FROM users;

-- Active users (활성 사용자)
SELECT * FROM users WHERE status = 'ACTIVE';

-- Inactive users (비활성 사용자)
SELECT * FROM users WHERE status = 'INACTIVE';

-- Suspended users (정지된 사용자)
SELECT * FROM users WHERE status = 'SUSPENDED';

-- User login example (로그인 예시)
SELECT * FROM users WHERE username = 'john doe' AND password = 'password123';

-- =====================================================
-- 8. Students table (학생 테이블)
-- =====================================================
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    age INTEGER,
    major VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE students IS 'Student table (학생 테이블)';
COMMENT ON COLUMN students.id IS 'Student ID (PK)';
COMMENT ON COLUMN students.name IS 'Student name (이름)';
COMMENT ON COLUMN students.email IS 'Email address (이메일)';
COMMENT ON COLUMN students.age IS 'Age (나이)';
COMMENT ON COLUMN students.major IS 'Major (전공)';
COMMENT ON COLUMN students.created_at IS 'Created timestamp (생성일시)';

-- Sample Students Data
INSERT INTO students (name, email, age, major) VALUES ('Alice Smith', 'alice@example.com', 20, 'Computer Science');
INSERT INTO students (name, email, age, major) VALUES ('Bob Johnson', 'bob@example.com', 22, 'Mathematics');
INSERT INTO students (name, email, age, major) VALUES ('Charlie Brown', 'charlie@example.com', 21, 'Physics');

-- =====================================================
-- [2026-04-22] Budget management table (예산 관리 테이블)
-- Purpose: store national/local budget by region and period
-- =====================================================
CREATE TABLE budget_mng (
    id          BIGSERIAL    PRIMARY KEY,
    sido_cd     VARCHAR(10)  NOT NULL,
    sido_nm     VARCHAR(50),
    sigungu_cd  VARCHAR(10),
    sigungu_nm  VARCHAR(50),
    gu_cd       VARCHAR(10),
    gu_nm       VARCHAR(50),
    start_yy    CHAR(4)      NOT NULL,
    start_mm    CHAR(2)      NOT NULL,
    end_yy      CHAR(4)      NOT NULL,
    end_mm      CHAR(2)      NOT NULL,
    ntnl_bgt    BIGINT       NOT NULL DEFAULT 0,
    lcl_bgt     BIGINT       NOT NULL DEFAULT 0,
    del_yn      CHAR(1)      NOT NULL DEFAULT 'N',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO budget_mng (sido_cd, sido_nm, sigungu_cd, sigungu_nm, start_yy, start_mm, end_yy, end_mm, ntnl_bgt, lcl_bgt)
VALUES
    ('41', '경기도', '41210', '광명시', '2026', '01', '2026', '12', 10000000, 5000000),
    ('41', '경기도', '41170', '안양시', '2026', '01', '2026', '12', 20000000, 10000000),
    ('41', '경기도', '41460', '용인시', '2026', '01', '2026', '12', 30000000, 15000000),
    ('41', '경기도', '41500', '이천시', '2026', '01', '2026', '12', 40000000, 20000000);

-- =====================================================
-- MIGRATIONS
-- Run these only on existing databases (already created).
-- If you ran schema.sql from scratch, skip this section.
-- =====================================================

-- [2026-04-17] Add image_url column to the product table
-- Allows products to store an uploaded image path e.g. /uploads/abc.jpg
ALTER TABLE product ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

-- [2026-04-19] Common code table (공통코드 테이블)
CREATE TABLE IF NOT EXISTS common_code (
    code_group  VARCHAR(30)  NOT NULL,
    code_value  VARCHAR(30)  NOT NULL,
    code_name   VARCHAR(100) NOT NULL,
    sort_order  INT          NOT NULL DEFAULT 0,
    use_yn      CHAR(1)      NOT NULL DEFAULT 'Y',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (code_group, code_value)
);

INSERT INTO common_code (code_group, code_value, code_name, sort_order) VALUES
('USER_STATUS', 'ACTIVE', '활성', 1), ('USER_STATUS', 'INACTIVE', '비활성', 2),
('USER_STATUS', 'SUSPENDED', '정지', 3), ('USER_STATUS', 'PENDING', '대기', 4),
('BOARD_TYPE', '01', '공지사항', 1), ('BOARD_TYPE', '02', '자유게시판', 2), ('BOARD_TYPE', '03', 'FAQ', 3),
('PRODUCT_STATUS', 'SALE', '판매중', 1), ('PRODUCT_STATUS', 'SOLDOUT', '품절', 2), ('PRODUCT_STATUS', 'HIDDEN', '숨김', 3),
('USER_ROLE', 'ROLE_ADMIN', '관리자', 1), ('USER_ROLE', 'ROLE_MANAGER', '매니저', 2), ('USER_ROLE', 'ROLE_USER', '일반', 3)
ON CONFLICT DO NOTHING;


-- [2026-04-19] Board file attachments (게시판 첨부파일 테이블)
CREATE TABLE IF NOT EXISTS board_file (
    file_sn    SERIAL PRIMARY KEY,
    board_sn   INTEGER      NOT NULL,
    orig_name  VARCHAR(500) NOT NULL,   -- original filename shown to user (원본 파일명)
    saved_name VARCHAR(500) NOT NULL,   -- UUID filename stored on disk (저장 파일명)
    file_size  BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_board_file_board
        FOREIGN KEY (board_sn)
        REFERENCES co_smp_board_m(board_sn)
        ON DELETE CASCADE    -- delete files when board post is deleted (게시글 삭제 시 파일도 삭제)
);


-- [2026-04-20] Add company table

CREATE TABLE company (
                         id               BIGSERIAL PRIMARY KEY,
                         company_name     VARCHAR(100)  NOT NULL,
                         ceo_name         VARCHAR(50)   NOT NULL,
                         business_no      VARCHAR(20)   NOT NULL UNIQUE,
                         address          VARCHAR(200),
                         address_detail   VARCHAR(100),
                         phone            VARCHAR(20),
                         homepage         VARCHAR(200),
                         payment_type     VARCHAR(10),
                         payment_provider VARCHAR(50),
                         apply_channel    VARCHAR(10)  DEFAULT 'ONLINE',
                         status           VARCHAR(10)  DEFAULT '신청',
                         contact_name     VARCHAR(50),
                         contact_phone    VARCHAR(20),
                         worker_count     INT          DEFAULT 0,
                         created_at       TIMESTAMP    DEFAULT NOW(),
                         updated_at       TIMESTAMP    DEFAULT NOW()
);

INSERT INTO company (company_name, ceo_name, business_no, phone, payment_type, payment_provider, apply_channel, status, contact_name, contact_phone, worker_count)
VALUES
    ('비즈플레이 주식회사', '홍길동', '107-88-36126', '010-1111-7777', '카드사', '하나카드', 'ONLINE', '승인', '김철수', '010-1111-0001', 300),
    ('동대문엽기떡볶이',   '장홍련', '107-88-36125', '010-0000-8888', '식권사', '비플식권', 'ONLINE', '신청', '이영희', '010-1111-0002', 50),
    ('당진종합할인마트',   '이순신', '107-88-36127', '041-3773-2945', '식권사', '식권대장', 'OFFLINE','신청', '박민준', '010-1111-0003', 120),
    ('에이티',           '세종대왕','107-88-36180', '041-1111-1111', '식권사', '페이코',   'OFFLINE','승인', '최지원', '010-1111-0004', 80),
    ('세븐티',           '유관순', '107-88-36170', '041-8888-0000', '카드사', '신한카드', 'OFFLINE','반려', '정수빈', '010-1111-0005', 200);


-- =====================================================
-- [2026-04-22] Budget management table (for existing DB)
-- =====================================================
CREATE TABLE IF NOT EXISTS budget_mng (
    id          BIGSERIAL    PRIMARY KEY,
    sido_cd     VARCHAR(10)  NOT NULL,
    sido_nm     VARCHAR(50),
    sigungu_cd  VARCHAR(10),
    sigungu_nm  VARCHAR(50),
    gu_cd       VARCHAR(10),
    gu_nm       VARCHAR(50),
    start_yy    CHAR(4)      NOT NULL,
    start_mm    CHAR(2)      NOT NULL,
    end_yy      CHAR(4)      NOT NULL,
    end_mm      CHAR(2)      NOT NULL,
    ntnl_bgt    BIGINT       NOT NULL DEFAULT 0,
    lcl_bgt     BIGINT       NOT NULL DEFAULT 0,
    del_yn      CHAR(1)      NOT NULL DEFAULT 'N',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- [2026-04-27] FAQ board tables (자주하는 질문 관리)
-- Purpose: co_bbs_m stores FAQ posts; co_comm_cd_d stores shared common codes
--          including bbs_ki_cd (게시판 구분) used in the board type dropdown
-- =====================================================

-- sequence for co_bbs_m PK
CREATE SEQUENCE IF NOT EXISTS bbs_seq START 1;

-- common code detail table (공통코드 상세 테이블)
CREATE TABLE IF NOT EXISTS co_comm_cd_d (
    comm_cd          VARCHAR(20)  NOT NULL,
    comm_dtcd        VARCHAR(10)  NOT NULL,
    comm_dtl_cd_nm   VARCHAR(300),
    inq_ord_no       NUMERIC(3)   DEFAULT 0,
    comm_dtl_cn      VARCHAR(1000),
    use_yn           CHAR(1)      NOT NULL DEFAULT 'Y',
    data_reg_ip_addr VARCHAR(155),
    data_reg_id      VARCHAR(20),
    data_reg_dt      TIMESTAMP    NOT NULL DEFAULT NOW(),
    data_chg_ip_addr VARCHAR(155),
    data_chg_id      VARCHAR(20),
    data_chg_dt      TIMESTAMP,
    comm_cd_len      NUMERIC(2),
    PRIMARY KEY (comm_cd, comm_dtcd),
    CONSTRAINT chk_co_comm_cd_d_use_yn CHECK (use_yn IN ('Y', 'N'))
);

-- board kind codes (게시판 구분 코드)
INSERT INTO co_comm_cd_d (comm_cd, comm_dtcd, comm_dtl_cd_nm, inq_ord_no, use_yn) VALUES
    ('bbs_ki_cd', '01', '공지사항',          1, 'Y'),
    ('bbs_ki_cd', '02', '자주하는 질문(FAQ)', 2, 'Y'),
    ('bbs_ki_cd', '03', '이벤트',            3, 'Y')
ON CONFLICT DO NOTHING;

-- FAQ board table (자주하는 질문 게시판 테이블)
CREATE TABLE IF NOT EXISTS co_bbs_m (
    bbs_sn           BIGINT       PRIMARY KEY DEFAULT nextval('bbs_seq'),
    bbs_ki_cd        VARCHAR(10),
    bbs_title        VARCHAR(500) NOT NULL,
    bbs_cn           TEXT,
    use_yn           CHAR(1)      NOT NULL DEFAULT 'Y',
    data_reg_ip_addr VARCHAR(155),
    data_reg_id      VARCHAR(20),
    data_reg_dt      TIMESTAMP    NOT NULL DEFAULT NOW(),
    data_chg_ip_addr VARCHAR(155),
    data_chg_id      VARCHAR(20),
    data_chg_dt      TIMESTAMP,
    CONSTRAINT chk_co_bbs_m_use_yn CHECK (use_yn IN ('Y', 'N'))
);

INSERT INTO co_bbs_m (bbs_ki_cd, bbs_title, bbs_cn, use_yn, data_reg_id) VALUES
    ('02', '지원대상은 누구인가요?',     '사업대상 지방 정부에 위치한 중소기업에 재직중인 근로자입니다.', 'Y', 'admin'),
    ('02', '복지관련서비스 질문1',       '복지관련서비스 답변1 입니다.',                              'Y', 'admin'),
    ('02', '복지관련서비스 질문2',       '복지관련서비스 답변2 입니다.',                              'Y', 'admin'),
    ('01', '공지사항 테스트',            '공지사항 내용입니다.',                                     'Y', 'admin');

-- =====================================================
-- FUNCTIONS (함수)
-- Add new functions below following this pattern:
--
-- [YYYY-MM-DD] function_name(params) — what it does (Korean: 설명)
-- CREATE OR REPLACE FUNCTION function_name(...) ...
-- =====================================================

-- (no functions yet — add here when needed)
-- Example pattern:
--
-- -- [2026-04-21] get_company_stats() — returns count by status (상태별 기업 수 집계)
-- CREATE OR REPLACE FUNCTION get_company_stats()
-- RETURNS TABLE(status VARCHAR, cnt BIGINT) AS $$
-- BEGIN
--     RETURN QUERY
--     SELECT c.status, COUNT(*)
--     FROM company c
--     GROUP BY c.status;
-- END;
-- $$ LANGUAGE plpgsql;