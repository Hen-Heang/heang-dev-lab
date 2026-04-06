-- =====================================================
-- Store Admin Database Schema
-- PostgresSQL Table Creation Script (테이블 생성 스크립트)

-- Execution order is important! (실행 순서 중요!)
-- 1. Drop tables (referenced table last) - 테이블 삭제
-- 2. Create tables (referenced table first) - 테이블 생성
-- 3. Create indexes - 인덱스 생성
-- 4. Insert sample data - 샘플 데이터 삽입
-- =====================================================

-- =====================================================
-- 1. Drop existing tables (기존 테이블 삭제)
-- =====================================================
-- Order matters! product references category, so drop product first
-- 순서 중요! product가 category를 참조하므로 product 먼저 삭제
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS students;

-- Drop JWT-related tables too (keep schema reset clean)
DROP TABLE IF EXISTS tb_partner_otp;
DROP TABLE IF EXISTS tb_merchant_otp;
DROP TABLE IF EXISTS tb_partner_phone;
DROP TABLE IF EXISTS tb_partner_info;
DROP TABLE IF EXISTS tb_merchant_phone;
DROP TABLE IF EXISTS tb_merchant_info;
DROP TABLE IF EXISTS tb_partner_account;
DROP TABLE IF EXISTS tb_merchant_account;
DROP TABLE IF EXISTS tb_role;

-- =====================================================
-- 2. Create User table (사용자 테이블 생성)
-- =====================================================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,                    -- Auto-increment PK (자동 증가)
    username VARCHAR(50) NOT NULL UNIQUE,     -- Username, required, unique (필수, 고유)
    email VARCHAR(100) NOT NULL UNIQUE,       -- Email, required, unique (필수, 고유)
    password VARCHAR(255) NOT NULL,           -- Password (BCrypt encrypted), required (필수)
    name VARCHAR(100),                        -- Full name (이름)
    phone VARCHAR(20),                        -- Phone number (전화번호)
    role VARCHAR(20) DEFAULT 'ROLE_USER',     -- Role: ROLE_USER, ROLE_ADMIN (권한)
    status VARCHAR(20) DEFAULT 'ACTIVE',      -- Status: ACTIVE, INACTIVE, SUSPENDED (상태)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Created timestamp (생성일시)
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- Updated timestamp (수정일시)
);

-- Explanation (설명):
-- UNIQUE: No duplicate values allowed (중복 불가)
-- role: User role for Spring Security (사용자 권한)
-- status: User account status (사용자 계정 상태)

COMMENT ON TABLE users IS 'User table (사용자 테이블)';
COMMENT ON COLUMN users.id IS 'User ID (PK)';
COMMENT ON COLUMN users.username IS 'Username for login (로그인 아이디)';
COMMENT ON COLUMN users.email IS 'Email address (이메일)';
COMMENT ON COLUMN users.password IS 'Password - BCrypt encrypted (비밀번호 - BCrypt 암호화)';
COMMENT ON COLUMN users.name IS 'Full name (이름)';
COMMENT ON COLUMN users.phone IS 'Phone number (전화번호)';
COMMENT ON COLUMN users.role IS 'User role: ROLE_USER, ROLE_ADMIN (사용자 권한)';
COMMENT ON COLUMN users.status IS 'Account status: ACTIVE, INACTIVE, SUSPENDED (계정 상태)';
COMMENT ON COLUMN users.created_at IS 'Created timestamp (생성일시)';
COMMENT ON COLUMN users.updated_at IS 'Updated timestamp (수정일시)';

-- =====================================================
-- 3. Create Category table (카테고리 테이블 생성)
-- =====================================================
CREATE TABLE category (
    id SERIAL PRIMARY KEY,           -- Auto-increment PK (자동 증가)
    name VARCHAR(100) NOT NULL,      -- Category name, required (필수)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Created timestamp (생성일시)
);

-- Explanation (설명):
-- SERIAL: PostgresSQL auto-increment (MySQL의 AUTO_INCREMENT와 같음)
-- PRIMARY KEY: Unique identifier, no duplicates, no NULL (고유 식별자)
-- NOT NULL: Required field, empty not allowed (필수 값)
-- DEFAULT CURRENT_TIMESTAMP: Auto-set current time if not provided (자동 현재 시간)

COMMENT ON TABLE category IS 'Category table (카테고리 테이블)';
COMMENT ON COLUMN category.id IS 'Category ID (PK)';
COMMENT ON COLUMN category.name IS 'Category name (카테고리명)';
COMMENT ON COLUMN category.created_at IS 'Created timestamp (생성일시)';

-- =====================================================
-- 4. Create Product table (상품 테이블 생성)
-- =====================================================
CREATE TABLE product (
    id SERIAL PRIMARY KEY,                    -- Auto-increment PK (자동 증가)
    name VARCHAR(200) NOT NULL,               -- Product name, required (필수)
    price INTEGER NOT NULL DEFAULT 0,         -- Price, default 0 (기본값 0)
    stock INTEGER NOT NULL DEFAULT 0,         -- Stock quantity, default 0 (기본값 0)
    category_id INTEGER NOT NULL,             -- Category FK, required (필수)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key constraint (외래 키 제약조건)
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE RESTRICT                    -- Prevent category deletion (삭제 방지)
);

-- Explanation (설명):
-- INTEGER: Integer type, good for price/stock (정수형)
-- FOREIGN KEY: References category table's id (category 테이블 참조)
-- ON DELETE RESTRICT: Cannot delete category if products exist (상품 있으면 삭제 불가)

COMMENT ON TABLE product IS 'Product table (상품 테이블)';
COMMENT ON COLUMN product.id IS 'Product ID (PK)';
COMMENT ON COLUMN product.name IS 'Product name (상품명)';
COMMENT ON COLUMN product.price IS 'Price (가격)';
COMMENT ON COLUMN product.stock IS 'Stock quantity (재고)';
COMMENT ON COLUMN product.category_id IS 'Category ID (FK)';
COMMENT ON COLUMN product.created_at IS 'Created timestamp (생성일시)';

-- =====================================================
-- 5. Create Indexes (인덱스 생성 - Performance optimization)
-- =====================================================
-- Frequently query products by category, so add index
-- 카테고리별 상품 조회가 빈번하므로 인덱스 추가
CREATE INDEX idx_product_category_id ON product(category_id);

-- Index for product name search (상품명 검색용 인덱스)
CREATE INDEX idx_product_name ON product(name);

-- Index for user search (사용자 검색용 인덱스)
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);

-- =====================================================
-- 6. Insert Sample Data (샘플 데이터 삽입)
-- =====================================================

-- Category data (카테고리 데이터)
INSERT INTO category (name) VALUES ('Electronics');      -- 전자제품
INSERT INTO category (name) VALUES ('Clothing');         -- 의류
INSERT INTO category (name) VALUES ('Food');             -- 식품
INSERT INTO category (name) VALUES ('Furniture');        -- 가구
INSERT INTO category (name) VALUES ('Books');            -- 도서

-- Product data: Electronics (전자제품: category_id = 1)
INSERT INTO product (name, price, stock, category_id) VALUES ('Laptop', 1500000, 50, 1);
INSERT INTO product (name, price, stock, category_id) VALUES ('Smartphone', 1200000, 100, 1);
INSERT INTO product (name, price, stock, category_id) VALUES ('Tablet', 800000, 30, 1);
INSERT INTO product (name, price, stock, category_id) VALUES ('Wireless Earbuds', 200000, 200, 1);
INSERT INTO product (name, price, stock, category_id) VALUES ('Smartwatch', 350000, 80, 1);

-- Product data: Clothing (의류: category_id = 2)
INSERT INTO product (name, price, stock, category_id) VALUES ('T-Shirt', 29000, 300, 2);
INSERT INTO product (name, price, stock, category_id) VALUES ('Jeans', 59000, 150, 2);
INSERT INTO product (name, price, stock, category_id) VALUES ('Sneakers', 89000, 100, 2);
INSERT INTO product (name, price, stock, category_id) VALUES ('Padding Jacket', 150000, 50, 2);
INSERT INTO product (name, price, stock, category_id) VALUES ('Socks Set', 15000, 500, 2);

-- Product data: Food (식품: category_id = 3)
INSERT INTO product (name, price, stock, category_id) VALUES ('Ramen Box', 12000, 1000, 3);
INSERT INTO product (name, price, stock, category_id) VALUES ('Water 2L (6pack)', 5000, 500, 3);
INSERT INTO product (name, price, stock, category_id) VALUES ('Fruit Gift Set', 50000, 30, 3);
INSERT INTO product (name, price, stock, category_id) VALUES ('Coffee Beans 1kg', 25000, 100, 3);
INSERT INTO product (name, price, stock, category_id) VALUES ('Chocolate Set', 18000, 200, 3);

-- Product data: Furniture (가구: category_id = 4)
INSERT INTO product (name, price, stock, category_id) VALUES ('Office Chair', 150000, 40, 4);
INSERT INTO product (name, price, stock, category_id) VALUES ('Computer Desk', 200000, 25, 4);
INSERT INTO product (name, price, stock, category_id) VALUES ('Bookshelf', 120000, 20, 4);
INSERT INTO product (name, price, stock, category_id) VALUES ('Sofa', 500000, 10, 4);
INSERT INTO product (name, price, stock, category_id) VALUES ('Bed Frame', 350000, 15, 4);

-- Product data: Books (도서: category_id = 5)
INSERT INTO product (name, price, stock, category_id) VALUES ('Java Programming', 32000, 100, 5);
INSERT INTO product (name, price, stock, category_id) VALUES ('SQL First Steps', 22000, 80, 5);
INSERT INTO product (name, price, stock, category_id) VALUES ('Clean Code', 33000, 60, 5);
INSERT INTO product (name, price, stock, category_id) VALUES ('Effective Java', 36000, 50, 5);
INSERT INTO product (name, price, stock, category_id) VALUES ('Database Fundamentals', 28000, 40, 5);

-- Low stock products for testing (테스트용 재고 부족 상품)
INSERT INTO product (name, price, stock, category_id) VALUES ('Limited Edition Laptop', 2500000, 3, 1);
INSERT INTO product (name, price, stock, category_id) VALUES ('Almost Sold Out T-Shirt', 35000, 5, 2);

-- =====================================================
-- Sample Users Data with BCrypt Passwords (Spring Security용)
-- BCrypt 암호화된 비밀번호를 사용한 샘플 사용자 데이터
-- =====================================================
-- Password for all users: password123
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.

-- Admin user (관리자)
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Admin User', '010-0000-0000', 'ROLE_ADMIN', 'ACTIVE');

-- Normal users (일반 사용자)
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES ('john doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'John Doe', '010-1234-5678', 'ROLE_USER', 'ACTIVE');

INSERT INTO users (username, email, password, name, phone, role, status)
VALUES ('Jane Doe', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Jane Doe', '010-9876-5432', 'ROLE_USER', 'ACTIVE');

-- Inactive user for testing (비활성 사용자 - 테스트용)
INSERT INTO users (username, email, password, name, phone, role, status)
VALUES ('inactive_user', 'inactive@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', 'Inactive User', '010-1111-1111', 'ROLE_USER', 'INACTIVE');

-- =====================================================
-- JWT auth tables (drms_api compatibility)
-- =====================================================
CREATE TABLE tb_role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tb_partner_account (
    id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL REFERENCES tb_role(id),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_verified BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_merchant_account (
    id SERIAL PRIMARY KEY,
    role_id INTEGER NOT NULL REFERENCES tb_role(id),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_verified BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_partner_phone (
    id SERIAL PRIMARY KEY,
    partner_account_id INTEGER NOT NULL REFERENCES tb_partner_account(id) ON DELETE CASCADE,
    phone_number VARCHAR(20) NOT NULL,
    is_primary BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_partner_info (
    id SERIAL PRIMARY KEY,
    partner_account_id INTEGER NOT NULL REFERENCES tb_partner_account(id) ON DELETE CASCADE,
    primary_phone_number VARCHAR(20),
    partner_name VARCHAR(100)
);

CREATE TABLE tb_merchant_phone (
    id SERIAL PRIMARY KEY,
    merchant_account_id INTEGER NOT NULL REFERENCES tb_merchant_account(id) ON DELETE CASCADE,
    phone_number VARCHAR(20) NOT NULL,
    is_primary BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_merchant_info (
    id SERIAL PRIMARY KEY,
    merchant_account_id INTEGER NOT NULL REFERENCES tb_merchant_account(id) ON DELETE CASCADE,
    primary_phone_number VARCHAR(20),
    merchant_name VARCHAR(100)
);

-- Seed auth roles/users (admin + merchant)
INSERT INTO tb_role (name) VALUES ('PARTNER'), ('MERCHANT');

INSERT INTO tb_partner_account (role_id, email, password, is_verified, is_active)
VALUES (1, 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', TRUE, TRUE);

INSERT INTO tb_partner_phone (partner_account_id, phone_number)
VALUES ((SELECT id FROM tb_partner_account WHERE email = 'admin@example.com'), '010-0000-0000');

INSERT INTO tb_partner_info (partner_account_id, primary_phone_number, partner_name)
VALUES ((SELECT id FROM tb_partner_account WHERE email = 'admin@example.com'), '010-0000-0000', 'Admin Partner');

INSERT INTO tb_merchant_account (role_id, email, password, is_verified, is_active)
VALUES (2, 'merchant@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqkB/BW0KdPBLBqZQKZhZz5KqpYN.', TRUE, TRUE);

INSERT INTO tb_merchant_phone (merchant_account_id, phone_number)
VALUES ((SELECT id FROM tb_merchant_account WHERE email = 'merchant@example.com'), '010-1111-2222');

INSERT INTO tb_merchant_info (merchant_account_id, primary_phone_number, merchant_name)
VALUES ((SELECT id FROM tb_merchant_account WHERE email = 'merchant@example.com'), '010-1111-2222', 'Sample Merchant');

-- =====================================================
-- 7. Verification Queries (데이터 확인 쿼리)
-- =====================================================

-- Category list (카테고리 목록)
SELECT * FROM category ORDER BY id;

-- Product list with category JOIN (카테고리 JOIN한 상품 목록)
SELECT
    p.id,
    p.name AS product_name,
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
