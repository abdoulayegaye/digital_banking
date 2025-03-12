-- Création de la base de données
CREATE DATABASE IF NOT EXISTS digital_banking;
USE digital_banking;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    active BOOLEAN DEFAULT TRUE
);

-- Table des clients
CREATE TABLE IF NOT EXISTS clients (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telephone VARCHAR(20),
    adresse TEXT
);

-- Table des types de compte
CREATE TABLE IF NOT EXISTS types_compte (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(20) NOT NULL
);

-- Table des comptes
CREATE TABLE IF NOT EXISTS comptes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(20) UNIQUE NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    client_id INT,
    type_compte_id INT,
    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (type_compte_id) REFERENCES types_compte(id)
);

-- Table des opérations
CREATE TABLE IF NOT EXISTS operations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date_op TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    montant DECIMAL(15,2) NOT NULL,
    type_operation VARCHAR(20) NOT NULL,
    compte_id INT,
    compte_destination_id INT,
    description TEXT,
    FOREIGN KEY (compte_id) REFERENCES comptes(id),
    FOREIGN KEY (compte_destination_id) REFERENCES comptes(id)
);

-- Insertion des types de compte
INSERT INTO types_compte (libelle) VALUES 
('COURANT'),
('EPARGNE');

-- Insertion d'un utilisateur admin par défaut (mot de passe: admin123)
INSERT INTO users (username, password, full_name, role) VALUES 
('admin', '$2a$10$xLxZQXXXXXXXXXXXXXXXXOXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', 'Administrateur', 'ADMIN'); 