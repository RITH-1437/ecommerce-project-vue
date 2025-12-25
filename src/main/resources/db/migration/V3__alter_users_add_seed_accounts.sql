-- ============================
-- V3: Alter users table and add seed accounts
-- ============================
-- Date: December 11, 2025
-- Purpose:
--   1. Make 'role' column nullable with default 'CUSTOMER'
--   2. Insert admin and sample user accounts

-- ============================
-- Step 1: Alter users table
-- ============================
-- Make role nullable and set default to 'CUSTOMER'
ALTER TABLE `users`
  MODIFY COLUMN `role` VARCHAR(50) NULL DEFAULT 'CUSTOMER';

-- ============================
-- Step 2: Insert seed accounts
-- ============================
-- Note: Passwords are BCrypt encoded
-- admin@applestore.com : admin@12345
-- user@example.com : user@123345

INSERT INTO `users` (`email`, `password_hash`, `role`, `is_active`, `email_verified`, `created_at`, `updated_at`)
VALUES
  ('admin@applestore.com', '$2a$10$9gd.BcPRbAY3..QqY/s3gu9srB0nxj61knMFKsccBBcVNgVp7.zyu', 'ADMIN', TRUE, TRUE, NOW(), NOW()),
  ('user@example.com', '$2a$10$WRfl0GzrVQgkMTT1mnhjmOZ4n0OHSjvTPnXeQUXUk1ndSmV6CT.0m', 'CUSTOMER', TRUE, TRUE, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `password_hash` = VALUES(`password_hash`),
  `role` = VALUES(`role`),
  `is_active` = VALUES(`is_active`),
  `email_verified` = VALUES(`email_verified`),
  `updated_at` = NOW();

-- ============================
-- Notes:
-- ============================
-- Seed account credentials:
--   Admin: admin@applestore.com / admin@12345
--   User:  user@example.com / user@123345
--
-- Passwords are BCrypt encoded with strength 10
-- Hash generated using: BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

