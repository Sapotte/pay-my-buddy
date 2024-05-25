-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : sam. 25 mai 2024 à 07:54
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `paymybuddy`
--

-- --------------------------------------------------------

--
-- Structure de la table `extern_transaction`
--

DROP TABLE IF EXISTS `extern_transaction`;
CREATE TABLE IF NOT EXISTS `extern_transaction`
(
    `id`      int(11)                              NOT NULL AUTO_INCREMENT,
    `account` varchar(50) COLLATE utf8_unicode_ci  NOT NULL,
    `date`    date                                 NOT NULL,
    `type`    varchar(255) COLLATE utf8_unicode_ci NOT NULL,
    `amount`  double(6, 2)                         NOT NULL,
    `taxe`    double(6, 2)                         NOT NULL,
    `id_user` int(11)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `extern_transaction_user_FK` (`id_user`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Déchargement des données de la table `extern_transaction`
--

INSERT INTO `extern_transaction` (`id`, `account`, `date`, `type`, `amount`, `taxe`, `id_user`)
VALUES (6, 'CA', '2024-05-24', 'DEPOSIT', 1500.00, 7.50, 3);

-- --------------------------------------------------------

--
-- Structure de la table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
CREATE TABLE IF NOT EXISTS `friendship`
(
    `id`        int(11)    NOT NULL AUTO_INCREMENT,
    `is_active` tinyint(1) NOT NULL,
    `id_friend` int(11)    NOT NULL,
    `id_user`   int(11)    NOT NULL,
    PRIMARY KEY (`id`),
    KEY `friendship_user_FK` (`id_user`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Déchargement des données de la table `friendship`
--

INSERT INTO `friendship` (`id`, `is_active`, `id_friend`, `id_user`)
VALUES (2, 1, 4, 3);

-- --------------------------------------------------------

--
-- Structure de la table `intern_transaction`
--

DROP TABLE IF EXISTS `intern_transaction`;
CREATE TABLE IF NOT EXISTS `intern_transaction`
(
    `id`           int(11)                              NOT NULL AUTO_INCREMENT,
    `date`         date                                 NOT NULL,
    `is_completed` tinyint(1)                           NOT NULL,
    `amount`       double(6, 2)                         NOT NULL,
    `taxe`         double(6, 2)                         NOT NULL,
    `id_friend`    int(11)                              NOT NULL,
    `label`        varchar(400) COLLATE utf8_unicode_ci NOT NULL,
    `id_user`      int(11)                              NOT NULL,
    PRIMARY KEY (`id`),
    KEY `intern_transaction_user_FK` (`id_user`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Déchargement des données de la table `intern_transaction`
--

INSERT INTO `intern_transaction` (`id`, `date`, `is_completed`, `amount`, `taxe`, `id_friend`, `label`, `id_user`)
VALUES (2, '2024-05-25', 1, 150.00, 0.75, 4, '1er essai', 3);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`              int(11)                              NOT NULL AUTO_INCREMENT,
    `password`        varchar(300) COLLATE utf8_unicode_ci NOT NULL,
    `username`        varchar(100) COLLATE utf8_unicode_ci NOT NULL,
    `account_balance` double(6, 2)                         NOT NULL,
    `email`           varchar(150) COLLATE utf8_unicode_ci NOT NULL,
    `is_enabled`      tinyint(1)                           NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_AK` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `password`, `username`, `account_balance`, `email`, `is_enabled`)
VALUES (3, '$2a$10$7rDzsKp5NLw86BRcve6ajeGTaZyNw64SyHRGoFjLcOlw9IN8VcThq', 'essai1', 1349.25, 'essai1@mail.com', 0),
       (4, '$2a$10$YBz.zSY6/MsNy0nXXRIYIOaRKM94lewWjOpo3gmrAb319bBKHvylq', 'essai3', 150.00, 'essai3@mail.com', 0),
       (5, '$2a$10$X/sqKjeB.FdqKK3YpKOw1OWIaAQ.gd9mf9F5UiYqi/MbLp0CSpn2K', 'essai2', 0.00, 'essai2@mail.com', 0);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `extern_transaction`
--
ALTER TABLE `extern_transaction`
    ADD CONSTRAINT `extern_transaction_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `friendship`
--
ALTER TABLE `friendship`
    ADD CONSTRAINT `friendship_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);

--
-- Contraintes pour la table `intern_transaction`
--
ALTER TABLE `intern_transaction`
    ADD CONSTRAINT `intern_transaction_user_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
