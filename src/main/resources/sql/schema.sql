-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS digital_banking;
USE digital_banking;

-- Create clients table
CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Create comptes table
CREATE TABLE IF NOT EXISTS comptes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) NOT NULL UNIQUE,
    balance DOUBLE NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);

-- Create operations table
CREATE TABLE IF NOT EXISTS operations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_op TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DOUBLE NOT NULL,
    type VARCHAR(10) NOT NULL,
    compte_id INT NOT NULL,
    FOREIGN KEY (compte_id) REFERENCES comptes(id) ON DELETE CASCADE
);
