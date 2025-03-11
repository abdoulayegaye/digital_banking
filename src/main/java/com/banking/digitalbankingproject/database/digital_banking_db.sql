-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 11 mars 2025 à 01:06
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.0.28

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
                                                           (1, 'bada', 'kane', 'kane@gmail.com'),
                                                           (2, 'camara Ilimane', 'diop', 'diopcamara@gmail.com'),
                                                           (3, 'mbaye sy', '', 'mbayesy@gmail.com'),
                                                           (4, 'sall', 'ismael', 'sallisma@gmail.com'),
                                                           (6, 'biram faye', '', 'faybi@gmail.com'),
                                                           (7, 'balla fay', '', 'faybal@gmail.com'),
                                                           (8, 'dame diop', '', 'diobadam@gmail.com'),
                                                           (9, 'saliou mbaye', '', 'zalmbaye@gmail.com'),
                                                           (10, 'cissé', 'coura', 'cissécou@gmail.com'),
                                                           (13, 'daba sarr', '', 'sardab@gmail.com'),
                                                           (14, 'ba', 'astou', 'bastou@gmail.com'),
                                                           (17, 'astou faye', '', 'fayast@gmail.com'),
                                                           (19, 'coura diong', '', 'courachou@gmail.com'),
                                                           (20, 'seye', 'khady bara', 'seyekhab@gmail.com'),
                                                           (21, 'tall', 'mareme', 'talma@gmail.com'),
                                                           (22, 'woulimata diop', '', 'diopwoul@gmail.com'),
                                                           (23, 'arame diallo', '', 'dialloaram@gmail.com'),
                                                           (24, 'gaye', 'zale', 'gayza@gmail.com'),
                                                           (25, 'khadim ka', '', 'kakha@gmail.com');

-- --------------------------------------------------------

--
-- Structure de la table `comptes`
--

CREATE TABLE `comptes` (
                           `id` int(11) NOT NULL,
                           `numero` varchar(200) NOT NULL,
                           `balance` double NOT NULL DEFAULT 5000,
                           `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
                           `client_id` int(11) NOT NULL,
                           `actif` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `comptes`
--

INSERT INTO `comptes` (`id`, `numero`, `balance`, `created_at`, `client_id`, `actif`) VALUES
                                                                                          (1, 'CPT1001', 14850, '2025-03-10 18:48:22', 1, 1),
                                                                                          (2, 'CPT1002', 101800, '2025-03-10 18:48:22', 2, 1),
                                                                                          (3, 'CPT1003', 1200, '2025-03-10 18:48:22', 3, 1),
                                                                                          (4, 'CPT1004', 6550, '2025-03-10 18:48:22', 4, 1),
                                                                                          (5, '8DYQ8D9WD6', 540000, '2025-03-10 18:50:47', 7, 0),
                                                                                          (6, 'WQRP1D330P', 325000, '2025-03-10 18:51:35', 8, 0),
                                                                                          (7, 'N38FEDPAR6', 150000, '2025-03-10 18:52:20', 9, 1),
                                                                                          (9, 'LI2BW3B7C9', 600450, '2025-03-10 19:59:20', 13, 1),
                                                                                          (13, 'M4VUXZBPRD', 486000, '2025-03-10 20:29:08', 19, 1),
                                                                                          (14, 'VSWYE7R6HZ', 2540000, '2025-03-10 20:42:02', 22, 1),
                                                                                          (15, '5CASP1JL6N', 66410235, '2025-03-10 20:44:00', 23, 1),
                                                                                          (16, 'Q12QKUN3E7', 32424550, '2025-03-10 21:51:50', 25, 1);

-- --------------------------------------------------------

--
-- Structure de la table `operations`
--

CREATE TABLE `operations` (
                              `id` int(11) NOT NULL,
                              `date_op` timestamp NOT NULL DEFAULT current_timestamp(),
                              `amount` double NOT NULL,
                              `type` enum('DEPOT','RETRAIT','VERSEMENT','') NOT NULL,
                              `compte_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `operations`
--

INSERT INTO `operations` (`id`, `date_op`, `amount`, `type`, `compte_id`) VALUES
                                                                              (1, '2025-03-10 18:48:56', 5000, 'DEPOT', 1),
                                                                              (2, '2025-03-10 18:48:56', 3000, 'RETRAIT', 1),
                                                                              (3, '2025-03-10 18:48:56', 2000, 'DEPOT', 2),
                                                                              (4, '2025-03-10 18:48:56', 4000, 'VERSEMENT', 3);

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
    (1, 'admin', '$2a$10$hgMYh4/GXYMkFdSCQGTMVOSY0.MHVVULMtkmwvv0hUFGkP3Woj56m');

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
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT pour la table `comptes`
--
ALTER TABLE `comptes`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `operations`
--
ALTER TABLE `operations`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
