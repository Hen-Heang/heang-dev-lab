-- =====================================================
-- DB IDE Test SQL
-- Run schema.sql first, then use these queries for testing.
-- =====================================================

-- 1. Basic row checks
SELECT COUNT(*) AS user_count FROM users;
SELECT COUNT(*) AS category_count FROM category;
SELECT COUNT(*) AS product_count FROM product;

-- 2. User search cases
SELECT id, username, email, status
FROM users
WHERE status = 'ACTIVE'
ORDER BY id DESC;

SELECT id, username, email, status
FROM users
WHERE username LIKE '%john%'
   OR email LIKE '%john%'
ORDER BY id DESC;

SELECT id, username, email, status
FROM users
WHERE status IN ('ACTIVE', 'INACTIVE', 'PENDING')
ORDER BY id DESC;

-- 3. Product + category join
SELECT
    p.id,
    p.name,
    p.price,
    p.stock,
    c.name AS category_name
FROM product p
LEFT JOIN category c ON p.category_id = c.id
ORDER BY p.id DESC;

-- 4. Filter products like ProductMapper search
SELECT
    p.id,
    p.name,
    p.price,
    p.stock,
    c.name AS category_name
FROM product p
LEFT JOIN category c ON p.category_id = c.id
WHERE LOWER(p.name) LIKE LOWER('%lap%')
  AND p.price >= 100000
  AND p.price <= 3000000
ORDER BY p.price DESC;

-- 5. Category lookup
SELECT id, name, created_at
FROM category
ORDER BY id DESC;

-- 6. Batch-insert style test for dynamic SQL practice
-- password/role defaults are applied automatically
INSERT INTO users (username, email, status) VALUES
    ('batch_user_1', 'batch1@example.com', 'ACTIVE'),
    ('batch_user_2', 'batch2@example.com', 'INACTIVE'),
    ('batch_user_3', 'batch3@example.com', 'PENDING');

SELECT id, username, email, role, status
FROM users
WHERE username LIKE 'batch_user_%'
ORDER BY id;

-- 7. Dynamic update style test
UPDATE users
SET username = 'batch_user_1_updated',
    status = 'SUSPENDED',
    updated_at = CURRENT_TIMESTAMP
WHERE username = 'batch_user_1';

SELECT id, username, email, status, updated_at
FROM users
WHERE username = 'batch_user_1_updated';

-- 8. Cleanup test rows if needed
DELETE FROM users
WHERE username IN ('batch_user_1_updated', 'batch_user_2', 'batch_user_3');
