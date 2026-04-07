-- =====================================================
-- Spring MyBatis Test Schema
-- PostgreSQL setup script for users, category, and product
-- =====================================================

-- Reset tables in dependency order
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

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
-- Password for seeded users: password123
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
