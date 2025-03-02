-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 01 mars 2025 à 11:17
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `digital_banking`
--

-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

CREATE TABLE `clients` (
  `id` int(11) NOT NULL,
  `nom` varchar(45) NOT NULL,
  `prenom` varchar(65) NOT NULL,
  `email` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`id`, `nom`, `prenom`, `email`) VALUES
(1, 'Ngom', 'Cheikh', 'cheikhngom99@gmail.com'),
(2, 'Diop', 'Alioune', 'alioune.diop@groupeisi.com'),
(3, 'Diao', 'Cheikh', 'diao.cheikh30@groupeisi.com'),
(5, 'Niang', 'Balla', 'balla.niang@groupeisi.com'),
(6, 'Sow', 'Mouhamed', 'sow2000mouhamed@groupeisi.com'),
(7, 'Dieye', 'Waly', 'wily.dieye623@gmail.com'),
(8, 'Ngom', 'Sara', 'sara.ngom06@gmail.com'),
(9, 'Ngom', 'Aliou Sene', 'aliou.sene09@gmail.com'),
(10, 'Mabotawa', 'Marx Cesar', 'mabotawa.marx@groupeisi.com'),
(11, 'Diop', 'Alioune Badara', 'alioune.badara@groupeisi.com');

-- --------------------------------------------------------

--
-- Structure de la table `comptes`
--

CREATE TABLE `comptes` (
  `id` int(11) NOT NULL,
  `numero` varchar(200) NOT NULL,
  `balance` double NOT NULL DEFAULT 5000,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `client_id` int(11) DEFAULT NULL,
  `etat` varchar(255) DEFAULT NULL,
  `actif` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `comptes`
--

INSERT INTO `comptes` (`id`, `numero`, `balance`, `created_at`, `client_id`, `etat`, `actif`) VALUES
(1, 'C7463', 19000, '2025-02-26 02:00:15', 2, 'Fermé', 1),
(2, 'C4566', 182000, '2025-02-26 02:34:57', 1, 'Actif', 1),
(3, 'C1679', 87000, '2025-02-26 03:20:02', 3, 'Actif', 1),
(9, 'FDHFNNHESK', 35000, '2025-02-27 00:42:52', 5, NULL, 0),
(10, 'W71QI6GTJI', 49000, '2025-02-27 00:44:09', 6, NULL, 1),
(11, 'G7RPCEJYH4', 66200, '2025-02-27 01:45:09', 7, NULL, 1),
(12, '61IB5WMF88', 73500, '2025-02-27 13:15:54', 8, NULL, 1),
(13, 'KRAQCGHGL1', 32000, '2025-02-27 13:16:36', NULL, NULL, 1),
(14, '06QHVDEWBE', 150000, '2025-02-27 13:16:48', 9, NULL, 1),
(15, 'L6EKT0V49W', 215000, '2025-02-27 13:16:57', 10, NULL, 1),
(16, 'MY7FM4KBAN', 56000, '2025-03-01 01:00:35', NULL, NULL, 1),
(17, 'AODYPV1Q92', 250000, '2025-03-01 10:05:04', 11, NULL, 1);

-- --------------------------------------------------------

--
-- Structure de la table `operations`
--

CREATE TABLE `operations` (
  `id` int(11) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `description` varchar(255) NOT NULL,
  `montant` double NOT NULL,
  `solde` double NOT NULL,
  `compte_id` int(11) NOT NULL,
  `type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `operations`
--

INSERT INTO `operations` (`id`, `date`, `description`, `montant`, `solde`, `compte_id`, `type`) VALUES
(1, '2025-02-27 19:15:40', 'Retrait', -10000, 146000, 2, 'Débit'),
(2, '2025-02-27 19:15:40', 'Dépôt', 10000, 66200, 11, 'Crédit'),
(3, '2025-02-27 19:15:40', 'Virement vers G7RPCEJYH4', -10000, 146000, 2, 'Débit'),
(4, '2025-02-27 19:15:40', 'Virement de C4566', 10000, 66200, 11, 'Crédit'),
(5, '2025-02-27 19:16:02', 'Retrait', -10000, 136000, 2, 'Débit'),
(6, '2025-02-27 19:16:02', 'Dépôt', 10000, 66500, 12, 'Crédit'),
(7, '2025-02-27 19:16:02', 'Virement vers 61IB5WMF88', -10000, 136000, 2, 'Débit'),
(8, '2025-02-27 19:16:02', 'Virement de C4566', 10000, 66500, 12, 'Crédit'),
(9, '2025-02-27 20:03:05', 'Retrait', -3000, 3000, 1, 'Débit'),
(10, '2025-02-27 20:03:05', 'Dépôt', 3000, 69500, 12, 'Crédit'),
(11, '2025-02-27 20:03:05', 'Virement vers 61IB5WMF88', -3000, 3000, 1, 'Débit'),
(12, '2025-02-27 20:03:05', 'Virement de C7463', 3000, 69500, 12, 'Crédit'),
(13, '2025-02-27 20:04:37', 'Retrait', -5000, 49000, 10, 'Débit'),
(14, '2025-02-27 20:04:37', 'Dépôt', 5000, 8000, 1, 'Crédit'),
(15, '2025-02-27 20:04:37', 'Virement vers C7463', -5000, 49000, 10, 'Débit'),
(16, '2025-02-27 20:04:37', 'Virement de W71QI6GTJI', 5000, 8000, 1, 'Crédit'),
(17, '2025-02-27 20:08:58', 'Retrait', -5000, 131000, 2, 'Débit'),
(18, '2025-02-27 20:08:58', 'Dépôt', 5000, 13000, 1, 'Crédit'),
(19, '2025-02-27 20:08:58', 'Virement vers C7463', -5000, 131000, 2, 'Débit'),
(20, '2025-02-27 20:08:58', 'Virement de C4566', 5000, 13000, 1, 'Crédit'),
(21, '2025-02-27 22:14:44', 'Retrait', -1000, 68500, 12, 'Débit'),
(22, '2025-02-27 22:14:44', 'Dépôt', 1000, 132000, 2, 'Crédit'),
(23, '2025-02-27 22:14:44', 'Virement vers C4566', -1000, 68500, 12, 'Débit'),
(24, '2025-02-27 22:14:44', 'Virement de 61IB5WMF88', 1000, 132000, 2, 'Crédit'),
(25, '2025-02-27 22:26:28', 'Retrait', -3000, 129000, 2, 'Débit'),
(26, '2025-02-27 22:26:28', 'Dépôt', 3000, 71500, 12, 'Crédit'),
(27, '2025-02-27 22:26:28', 'Virement vers 61IB5WMF88', -3000, 129000, 2, 'Débit'),
(28, '2025-02-27 22:26:28', 'Virement de C4566', 3000, 71500, 12, 'Crédit'),
(29, '2025-02-28 17:18:52', 'Retrait', -2000, 127000, 2, 'Débit'),
(30, '2025-02-28 17:18:52', 'Dépôt', 2000, 73500, 12, 'Crédit'),
(31, '2025-02-28 17:18:52', 'Virement vers 61IB5WMF88', -2000, 127000, 2, 'Débit'),
(32, '2025-02-28 17:18:52', 'Virement de C4566', 2000, 73500, 12, 'Crédit'),
(33, '2025-02-28 23:45:42', 'Retrait', -30000, 150000, 14, 'Débit'),
(34, '2025-02-28 23:45:42', 'Dépôt', 30000, 157000, 2, 'Crédit'),
(35, '2025-02-28 23:45:42', 'Virement vers C4566', -30000, 150000, 14, 'Débit'),
(36, '2025-02-28 23:45:42', 'Virement de 06QHVDEWBE', 30000, 157000, 2, 'Crédit'),
(37, '2025-03-01 00:55:10', 'Retrait', -4000, 9000, 1, 'Débit'),
(38, '2025-03-01 00:55:10', 'Dépôt', 4000, 87000, 3, 'Crédit'),
(39, '2025-03-01 00:55:10', 'Virement vers C1679', -4000, 9000, 1, 'Débit'),
(40, '2025-03-01 00:55:10', 'Virement de C7463', 4000, 87000, 3, 'Crédit'),
(41, '2025-03-01 08:35:25', 'Retrait', -10000, 215000, 15, 'Débit'),
(42, '2025-03-01 08:35:25', 'Dépôt', 10000, 19000, 1, 'Crédit'),
(43, '2025-03-01 08:35:25', 'Virement vers C7463', -10000, 215000, 15, 'Débit'),
(44, '2025-03-01 08:35:25', 'Virement de L6EKT0V49W', 10000, 19000, 1, 'Crédit'),
(45, '2025-03-01 10:06:58', 'Retrait', -25000, 250000, 17, 'Débit'),
(46, '2025-03-01 10:06:58', 'Dépôt', 25000, 182000, 2, 'Crédit'),
(47, '2025-03-01 10:06:58', 'Virement vers C4566', -25000, 250000, 17, 'Débit'),
(48, '2025-03-01 10:06:58', 'Virement de AODYPV1Q92', 25000, 182000, 2, 'Crédit');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(250) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `password`) VALUES
(1, 'admin', '$2a$10$1nXZ8tpo/1gO/1mX6xZ0X.QAIKWaEGPrs6y2DlbWANY9s9hFG9K9K');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `clients`
--
ALTER TABLE `clients`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `comptes`
--
ALTER TABLE `comptes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero` (`numero`),
  ADD KEY `client_id` (`client_id`);

--
-- Index pour la table `operations`
--
ALTER TABLE `operations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `compte_id` (`compte_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `clients`
--
ALTER TABLE `clients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT pour la table `comptes`
--
ALTER TABLE `comptes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `operations`
--
ALTER TABLE `operations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

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
