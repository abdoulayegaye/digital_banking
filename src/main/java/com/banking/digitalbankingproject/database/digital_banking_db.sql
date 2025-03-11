-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 11 mars 2025 à 01:36
-- Version du serveur : 8.4.4
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `digital_banking_db`
--

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE IF NOT EXISTS `clients` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `nom` varchar(45) NOT NULL,
    `prenom` varchar(65) NOT NULL,
    `email` varchar(80) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `email` (`email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`id`, `nom`, `prenom`, `email`) VALUES
                                                           (23, 'THIAM', 'Abdou Karim', 'akt@gmail.br'),
                                                           (24, 'GUEYE', 'Sambaa', 'samba@mail.com'),
                                                           (25, 'Haidara', 'Mamadou Lamine', 'mlh223@gmail.com'),
                                                           (26, 'MANE', 'PAPE', 'pape0900@gmail.sn'),
                                                           (27, 'COULIBALY', 'Yayah', 'couliy678@hotmail.com'),
                                                           (29, 'Badji', 'Alioune', 'Aliob@gmail.sn');

-- --------------------------------------------------------

--
-- Structure de la table `comptes`
--

DROP TABLE IF EXISTS `comptes`;
CREATE TABLE IF NOT EXISTS `comptes` (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `numero` varchar(200) NOT NULL,
    `balance` double NOT NULL DEFAULT '5000',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `client_id` int NOT NULL,
    `typeCompte` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `statut` varchar(50) DEFAULT 'ACTIF',
    `dateOuverture` date DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `numero` (`numero`),
    KEY `client_id` (`client_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `comptes`
--

INSERT INTO `comptes` (`id`, `numero`, `balance`, `created_at`, `client_id`, `typeCompte`, `statut`, `dateOuverture`) VALUES
                                                                                                                          (14, '9876567', 385029, '2025-03-05 04:40:22', 23, 'COURANT', 'ACTIF', '2025-03-04'),
                                                                                                                          (15, '45673452', 11678833, '2025-03-05 04:40:55', 24, 'EPARGNE', 'ACTIF', '2025-02-24'),
                                                                                                                          (16, '675389173', 952632, '2025-03-05 04:41:22', 25, 'COURANT', 'ACTIF', '2025-02-06'),
                                                                                                                          (17, '29809763', 973211, '2025-03-05 04:42:13', 26, 'EPARGNE', 'FERME', '2025-02-04'),
                                                                                                                          (18, '17898764', 2594500, '2025-03-05 04:43:08', 27, 'EPARGNE', 'ACTIF', '2025-01-28'),
                                                                                                                          (19, '74689724', 30000, '2025-03-06 00:37:15', 29, 'EPARGNE', 'FERME', '2025-03-06');

-- --------------------------------------------------------

--
-- Structure de la table `operations`
--

DROP TABLE IF EXISTS `operations`;
CREATE TABLE IF NOT EXISTS `operations` (
                                            `id` int NOT NULL AUTO_INCREMENT,
                                            `date_op` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            `amount` double NOT NULL,
                                            `type` varchar(50) DEFAULT NULL,
    `compte_id` int NOT NULL,
    `compte_destination_id` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `compte_id` (`compte_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `operations`
--

INSERT INTO `operations` (`id`, `date_op`, `amount`, `type`, `compte_id`, `compte_destination_id`) VALUES
                                                                                                       (70, '2025-03-05 04:45:21', 700000, 'VERSEMENT', 14, NULL),
                                                                                                       (71, '2025-03-05 04:46:04', 85000, 'VIREMENT', 14, NULL),
                                                                                                       (72, '2025-03-05 04:46:04', 85000, 'VERSEMENT', 17, NULL),
                                                                                                       (73, '2025-03-05 04:46:55', 65000, 'RETRAIT', 17, NULL),
                                                                                                       (74, '2025-03-05 04:47:14', 34900, 'VIREMENT', 17, NULL),
                                                                                                       (75, '2025-03-05 04:47:28', 45000, 'RETRAIT', 18, NULL),
                                                                                                       (76, '2025-03-05 04:50:04', 4500000, 'VIREMENT', 16, NULL),
                                                                                                       (77, '2025-03-05 04:50:04', 4500000, 'VERSEMENT', 15, NULL),
                                                                                                       (78, '2025-03-05 04:52:52', 300, 'VIREMENT', 16, NULL),
                                                                                                       (79, '2025-03-05 04:52:52', 300, 'VERSEMENT', 14, NULL),
                                                                                                       (80, '2025-03-05 15:35:07', 12, 'VIREMENT', 14, NULL),
                                                                                                       (81, '2025-03-05 15:35:25', 23, 'VIREMENT', 14, NULL),
                                                                                                       (82, '2025-03-05 15:35:25', 23, 'VERSEMENT', 15, NULL),
                                                                                                       (83, '2025-03-05 16:22:48', 56000, 'RETRAIT', 14, NULL),
                                                                                                       (84, '2025-03-05 16:23:46', 1000000, 'VIREMENT', 14, NULL),
                                                                                                       (85, '2025-03-05 16:23:46', 1000000, 'VERSEMENT', 18, NULL),
                                                                                                       (86, '2025-03-05 16:49:18', 10000, 'VIREMENT', 14, 16),
                                                                                                       (87, '2025-03-05 17:20:49', 35000, 'VIREMENT', 16, 15),
                                                                                                       (88, '2025-03-05 18:33:03', 12000, 'VIREMENT', 16, 15),
                                                                                                       (89, '2025-03-05 18:37:38', 3000, 'VIREMENT', 16, 17),
                                                                                                       (90, '2025-03-06 01:26:38', 12, 'VIREMENT', 16, 14),
                                                                                                       (91, '2025-03-06 01:58:14', 12, 'VIREMENT', 17, 15),
                                                                                                       (92, '2025-03-06 02:05:32', 3, 'VIREMENT', 15, 14),
                                                                                                       (93, '2025-03-06 02:51:17', 234, 'RETRAIT', 14, NULL),
                                                                                                       (94, '2025-03-06 02:51:42', 345, 'VERSEMENT', 14, NULL),
                                                                                                       (95, '2025-03-06 02:52:20', 565, 'VERSEMENT', 15, NULL),
                                                                                                       (96, '2025-03-06 02:52:43', 5656, 'VIREMENT', 16, 14),
                                                                                                       (97, '2025-03-06 02:56:05', 1000, 'VIREMENT', 16, 14),
                                                                                                       (98, '2025-03-06 03:14:01', 80, 'VERSEMENT', 15, NULL),
                                                                                                       (99, '2025-03-06 03:14:24', 100, 'VIREMENT', 16, 14),
                                                                                                       (100, '2025-03-06 03:28:09', 123, 'VIREMENT', 15, 17),
                                                                                                       (101, '2025-03-08 11:19:58', 89000, 'VERSEMENT', 18, NULL),
                                                                                                       (102, '2025-03-08 11:21:25', 9000000, 'RETRAIT', 18, NULL),
                                                                                                       (103, '2025-03-08 11:22:25', 500000, 'RETRAIT', 18, NULL),
                                                                                                       (104, '2025-03-08 11:23:57', 100000, 'VIREMENT', 14, 18),
                                                                                                       (105, '2025-03-08 11:25:56', 300000, 'VIREMENT', 14, 15),
                                                                                                       (106, '2025-03-08 11:27:35', 1000000, 'VERSEMENT', 14, NULL),
                                                                                                       (107, '2025-03-08 11:28:29', 1000000, 'RETRAIT', 14, NULL),
                                                                                                       (108, '2025-03-08 11:31:58', 1000000, 'RETRAIT', 14, NULL),
                                                                                                       (109, '2025-03-08 11:33:03', 1000000, 'VERSEMENT', 14, NULL),
                                                                                                       (110, '2025-03-08 11:35:11', 1000000, 'RETRAIT', 14, NULL),
                                                                                                       (111, '2025-03-08 11:35:48', 500000, 'VERSEMENT', 14, NULL),
                                                                                                       (112, '2025-03-08 11:36:42', 1000000, 'VIREMENT', 14, 15),
                                                                                                       (113, '2025-03-10 04:11:59', 500000, 'RETRAIT', 14, NULL),
                                                                                                       (114, '2025-03-10 04:12:38', 500000, 'VIREMENT', 14, 15),
                                                                                                       (115, '2025-03-10 04:14:06', 3000, 'VIREMENT', 14, 15),
                                                                                                       (118, '2025-03-10 04:31:52', 2000, 'RETRAIT', 14, NULL),
                                                                                                       (119, '2025-03-10 18:45:13', 300000, 'VERSEMENT', 14, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` int NOT NULL AUTO_INCREMENT,
                                       `username` varchar(30) NOT NULL,
    `password` varchar(250) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
    (1, 'momar', 'passer');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `comptes`
--
ALTER TABLE `comptes`
    ADD CONSTRAINT `comptes_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`);

--
-- Contraintes pour la table `operations`
--
ALTER TABLE `operations`
    ADD CONSTRAINT `operations_ibfk_1` FOREIGN KEY (`compte_id`) REFERENCES `comptes` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
