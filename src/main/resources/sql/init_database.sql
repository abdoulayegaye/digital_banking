-- Création de la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS digital_banking_db;

-- Utilisation de la base de données
USE digital_banking_db;

-- Création de la table users pour l'authentification
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

-- Insertion d'un utilisateur administrateur par défaut si la table est vide
-- Le mot de passe "admin123" est stocké en clair pour faciliter la connexion initiale
INSERT INTO users (username, password, nom, prenom, role)
SELECT 'admin', 'admin123', 'Administrateur', 'Système', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insertion d'un utilisateur standard par défaut si la table est vide
-- Le mot de passe "user123" est stocké en clair pour faciliter la connexion initiale
INSERT INTO users (username, password, nom, prenom, role)
SELECT 'user', 'user123', 'Utilisateur', 'Standard', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

-- Création de la table clients
CREATE TABLE IF NOT EXISTS clients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Création de la table comptes
CREATE TABLE IF NOT EXISTS comptes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    solde DOUBLE NOT NULL DEFAULT 0,
    clientId INT NOT NULL,
    FOREIGN KEY (clientId) REFERENCES clients(id)
);

-- Création de la table operations
CREATE TABLE IF NOT EXISTS operations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compteId INT NOT NULL,
    montant DOUBLE NOT NULL,
    typeOperation VARCHAR(50) NOT NULL,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (compteId) REFERENCES comptes(id)
);

-- Insertion de quelques clients de test si la table est vide
INSERT INTO clients (nom, prenom, email)
SELECT 'Dupont', 'Jean', 'jean.dupont@example.com'
WHERE NOT EXISTS (SELECT 1 FROM clients LIMIT 1);

INSERT INTO clients (nom, prenom, email)
SELECT 'Martin', 'Sophie', 'sophie.martin@example.com'
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE id = 2);

INSERT INTO clients (nom, prenom, email)
SELECT 'Dubois', 'Pierre', 'pierre.dubois@example.com'
WHERE NOT EXISTS (SELECT 1 FROM clients WHERE id = 3);

-- Insertion de quelques comptes de test si la table est vide
INSERT INTO comptes (solde, clientId)
SELECT 1000.0, 1
WHERE NOT EXISTS (SELECT 1 FROM comptes LIMIT 1);

INSERT INTO comptes (solde, clientId)
SELECT 2500.0, 1
WHERE NOT EXISTS (SELECT 1 FROM comptes WHERE id = 2);

INSERT INTO comptes (solde, clientId)
SELECT 500.0, 2
WHERE NOT EXISTS (SELECT 1 FROM comptes WHERE id = 3);

INSERT INTO comptes (solde, clientId)
SELECT 3000.0, 3
WHERE NOT EXISTS (SELECT 1 FROM comptes WHERE id = 4);

-- Insertion de quelques opérations de test si la table est vide
INSERT INTO operations (compteId, montant, typeOperation, date)
SELECT 1, 500.0, 'DEPOT', NOW()
WHERE NOT EXISTS (SELECT 1 FROM operations LIMIT 1);

INSERT INTO operations (compteId, montant, typeOperation, date)
SELECT 1, 200.0, 'RETRAIT', NOW()
WHERE NOT EXISTS (SELECT 1 FROM operations WHERE id = 2);

INSERT INTO operations (compteId, montant, typeOperation, date)
SELECT 2, 1000.0, 'DEPOT', NOW()
WHERE NOT EXISTS (SELECT 1 FROM operations WHERE id = 3);

INSERT INTO operations (compteId, montant, typeOperation, date)
SELECT 3, 300.0, 'DEPOT', NOW()
WHERE NOT EXISTS (SELECT 1 FROM operations WHERE id = 4); 