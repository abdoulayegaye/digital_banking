-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS digital_banking;
USE digital_banking;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create clients table
CREATE TABLE IF NOT EXISTS clients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create comptes table
CREATE TABLE IF NOT EXISTS comptes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    client_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients(id)
);

-- Create operations table
CREATE TABLE IF NOT EXISTS operations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    compte_id INT NOT NULL,
    type ENUM('DEPOT', 'RETRAIT') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    date_op TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (compte_id) REFERENCES comptes(id)
);

-- Insert default admin user (password: admin)
INSERT INTO users (username, password) 
VALUES ('admin', '$2a$10$FbzO3PROfQXjUqYuV2SXAOHx7g0TxB2QZqOupV9h6RYEzb3lGBwXy')
ON DUPLICATE KEY UPDATE username = username;
