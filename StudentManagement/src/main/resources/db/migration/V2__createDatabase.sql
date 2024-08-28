CREATE TABLE IF NOT EXISTS company
(
    company_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    description varchar(255) DEFAULT '',
    created_at timestamp  DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp  DEFAULT CURRENT_TIMESTAMP
);