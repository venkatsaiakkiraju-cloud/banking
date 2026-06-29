-- Sample seed data for testing
-- Password for all test users is: Password123 (BCrypt hashed below)
-- Hash generated for "Password123": $2a$10$EblZqNptyYtjQzGFAR8Z9.J0FQk1cBztsf4MQQ8sUowG.GUGzQOzm

INSERT INTO users (username, password, email, role, enabled, created_at) VALUES
('admin', '$2a$10$EblZqNptyYtjQzGFAR8Z9.J0FQk1cBztsf4MQQ8sUowG.GUGzQOzm', 'admin@securebank.com', 'ROLE_ADMIN', true, NOW()),
('employee1', '$2a$10$EblZqNptyYtjQzGFAR8Z9.J0FQk1cBztsf4MQQ8sUowG.GUGzQOzm', 'employee1@securebank.com', 'ROLE_EMPLOYEE', true, NOW()),
('customer1', '$2a$10$EblZqNptyYtjQzGFAR8Z9.J0FQk1cBztsf4MQQ8sUowG.GUGzQOzm', 'customer1@example.com', 'ROLE_CUSTOMER', true, NOW())
ON CONFLICT (username) DO NOTHING;
