-- ============================
-- Voucher Table
-- ============================

CREATE TABLE IF NOT EXISTS voucher (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(64) NOT NULL UNIQUE,
  discount_type VARCHAR(16) NOT NULL,
  value DECIMAL(10,2) NOT NULL,
  min_spend DECIMAL(12,2),
  max_discount DECIMAL(12,2),
  applicable_categories VARCHAR(512),
  stackable BOOLEAN DEFAULT FALSE,
  priority INT DEFAULT 0,
  user_limit INT,
  global_limit INT,
  used_count INT DEFAULT 0,
  expiry DATETIME,
  status VARCHAR(16) DEFAULT 'ACTIVE',
  note VARCHAR(1024)
);


-- ============================
-- User Voucher Usage Table
-- ============================

CREATE TABLE IF NOT EXISTS user_voucher (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  voucher_id BIGINT NOT NULL,
  used_at DATETIME NOT NULL,

  CONSTRAINT fk_user_voucher_voucher
    FOREIGN KEY (voucher_id) REFERENCES voucher(id)
);

CREATE INDEX idx_user_voucher_user ON user_voucher(user_id);
CREATE INDEX idx_user_voucher_voucher ON user_voucher(voucher_id);
