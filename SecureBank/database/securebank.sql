CREATE DATABASE IF NOT EXISTS securebank;
USE securebank;

CREATE TABLE IF NOT EXISTS accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    encrypted_account TEXT NOT NULL,
    balance DECIMAL(12, 2) NOT NULL,
    data_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
