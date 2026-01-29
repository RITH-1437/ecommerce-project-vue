-- ============================================
-- Update Admin Password to: admin@123
-- ============================================
-- BCrypt hash for password: admin@123
-- Run this script in MySQL to update the admin password
USE apple_store;
-- Update admin password
UPDATE users
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    updated_at = NOW()
WHERE email = 'admin@applestore.com';
-- Verify the update
SELECT id,
    email,
    role,
    updated_at
FROM users
WHERE email = 'admin@applestore.com';
-- ============================================
-- New Admin Login Credentials:
-- Email: admin@applestore.com
-- Password: admin@123
-- ============================================