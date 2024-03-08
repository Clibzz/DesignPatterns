-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysqldb:3306
-- Gegenereerd op: 08 mrt 2024 om 14:05
-- Serverversie: 8.0.36
-- PHP-versie: 8.2.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `book-sales`
--

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `cd`
--

CREATE TABLE `cd` (
  `id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `collectable`
--

CREATE TABLE `collectable` (
  `collection_id` int DEFAULT NULL,
  `date` date DEFAULT NULL,
  `genre_id` int DEFAULT NULL,
  `id` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `collection`
--

CREATE TABLE `collection` (
  `id` int NOT NULL,
  `user_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `credit`
--

CREATE TABLE `credit` (
  `collectable_id` int DEFAULT NULL,
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `dvd`
--

CREATE TABLE `dvd` (
  `duration` decimal(21,0) DEFAULT NULL,
  `id` int NOT NULL,
  `distribution_company` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `production_company` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `genre`
--

CREATE TABLE `genre` (
  `id` int NOT NULL,
  `colorcode` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `track`
--

CREATE TABLE `track` (
  `cd_id` int DEFAULT NULL,
  `duration` float NOT NULL,
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexen voor geëxporteerde tabellen
--

--
-- Indexen voor tabel `cd`
--
ALTER TABLE `cd`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Indexen voor tabel `collectable`
--
ALTER TABLE `collectable`
  ADD PRIMARY KEY (`id`),
  ADD KEY `collection_id` (`collection_id`) USING BTREE,
  ADD KEY `genre_id` (`genre_id`) USING BTREE;

--
-- Indexen voor tabel `collection`
--
ALTER TABLE `collection`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`) USING BTREE;

--
-- Indexen voor tabel `credit`
--
ALTER TABLE `credit`
  ADD PRIMARY KEY (`id`),
  ADD KEY `collectable_id` (`collectable_id`) USING BTREE;

--
-- Indexen voor tabel `dvd`
--
ALTER TABLE `dvd`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`) USING BTREE;

--
-- Indexen voor tabel `genre`
--
ALTER TABLE `genre`
  ADD PRIMARY KEY (`id`);

--
-- Indexen voor tabel `track`
--
ALTER TABLE `track`
  ADD PRIMARY KEY (`id`),
  ADD KEY `cd_id` (`cd_id`) USING BTREE;

--
-- Indexen voor tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT voor geëxporteerde tabellen
--

--
-- AUTO_INCREMENT voor een tabel `collectable`
--
ALTER TABLE `collectable`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `collection`
--
ALTER TABLE `collection`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `credit`
--
ALTER TABLE `credit`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `genre`
--
ALTER TABLE `genre`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `track`
--
ALTER TABLE `track`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `cd`
--
ALTER TABLE `cd`
  ADD CONSTRAINT `FKns26db74wv4gtyhb1vlvc8t77` FOREIGN KEY (`id`) REFERENCES `collectable` (`id`);

--
-- Beperkingen voor tabel `collectable`
--
ALTER TABLE `collectable`
  ADD CONSTRAINT `FKayjb1srxphtq59xawu43kbq3d` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`),
  ADD CONSTRAINT `FKqi5wg2l9gde5op2tlqmjptb5c` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`);

--
-- Beperkingen voor tabel `collection`
--
ALTER TABLE `collection`
  ADD CONSTRAINT `FKpo8h7vwck3icylwdgbhs04jf8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Beperkingen voor tabel `credit`
--
ALTER TABLE `credit`
  ADD CONSTRAINT `FKm2gt5b0igupcvykoivkofjssq` FOREIGN KEY (`collectable_id`) REFERENCES `collectable` (`id`);

--
-- Beperkingen voor tabel `dvd`
--
ALTER TABLE `dvd`
  ADD CONSTRAINT `FKbayv776tnw7foioxiy5hr76ni` FOREIGN KEY (`id`) REFERENCES `collectable` (`id`);

--
-- Beperkingen voor tabel `track`
--
ALTER TABLE `track`
  ADD CONSTRAINT `FK5cr4kjx4j9njw95htewv56fov` FOREIGN KEY (`cd_id`) REFERENCES `cd` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
