-- Quick verification script for database
USE appl_store;

-- Check if seed accounts exist
SELECT 'User Count' as Description, COUNT(*) as Count FROM users;

SELECT 'Admin Account' as Description, COUNT(*) as Count
FROM users WHERE email = 'admin@applestore.com' AND role = 'ADMIN';

SELECT 'Sample User Account' as Description, COUNT(*) as Count
FROM users WHERE email = 'user@example.com' AND role = 'CUSTOMER';

-- Show all users
SELECT id, email, role, is_active, email_verified, created_at
FROM users
ORDER BY created_at;

-- Check all tables
SELECT 'Tables in database:' as Info;
SHOW TABLES;
