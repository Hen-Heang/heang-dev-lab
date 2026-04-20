-- =====================================================
-- Spring MyBatis Test Schema
-- PostgreSQL setup script for users, category, and product
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
