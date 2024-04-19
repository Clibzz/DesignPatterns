-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysqldb:3306
-- Gegenereerd op: 19 apr 2024 om 21:21
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
CREATE DATABASE IF NOT EXISTS `book-sales` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `book-sales`;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int NOT NULL,
  `book_type_id` int NOT NULL,
  `description` longtext NOT NULL,
  `genre` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` double NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `page_amount` int NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Gegevens worden geëxporteerd voor tabel `book`
--

INSERT INTO `book` (`id`, `book_type_id`, `description`, `genre`, `price`, `author`, `publisher`, `title`, `page_amount`, `image`) VALUES
(1, 3, 'This is just a regular book, nothing special about it', 'HORROR', 40, 'Peter Benchley', 'bAsBv', 'Jaws', 245, 'the-50-coolest-book-covers-47.jpg'),
(2, 2, 'just an audiobook, about a city which is orange', 'SCIFI', 45, 'David Yoon', 'bAsBv', 'City Of Orange', 352, '9780593422182.jpg'),
(3, 1, 'A true story, about a man who\'s all alone... in ebook form', 'HORROR', 35, 'Morgan Maxwell', 'bAsBv', 'Alone, a true story', 299, '1003w-QHBKwQnsgzs.jpg'),
(4, 3, 'A weird way to go through the jungle, how special...', 'COMEDY', 30, 'Upton Sinclair', 'bAsBv', 'The Jungle', 199, '165523a5bad8aa3df160b9bbf37d19f5-Wade2BGreenbergThe2BJungle8057.jpg'),
(5, 2, 'A book about... normal people? it\'s an Audiobook version', 'FANTASY', 35, 'Sally Rooney', 'bAsBv', 'Normal People', 421, '817BsplxI9L.jpg'),
(6, 1, 'This is an amazing story, about my book cover now in Ebook!', 'ROMANCE', 49.99, 'My Name Here', 'Book publisher', 'My Book Cover', 300, 'book-covers-big-2019101610.jpg');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `book_type`
--

DROP TABLE IF EXISTS `book_type`;
CREATE TABLE `book_type` (
  `id` int NOT NULL,
  `type` varchar(255) NOT NULL,
  `attribute_type` varchar(100) NOT NULL,
  `has_attribute` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Gegevens worden geëxporteerd voor tabel `book_type`
--

INSERT INTO `book_type` (`id`, `type`, `attribute_type`, `has_attribute`) VALUES
(1, 'EBook', 'HasAutomaticReading', 1),
(2, 'AudioBook', 'HasVoiceActor', 1),
(3, 'NormalBook', 'HasHardCover', 1);

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `payment_cart`
--

DROP TABLE IF EXISTS `payment_cart`;
CREATE TABLE `payment_cart` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `cart_details` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `review`
--

DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `title` varchar(150) NOT NULL,
  `rating` double NOT NULL,
  `text` longtext NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Gegevens worden geëxporteerd voor tabel `review`
--

INSERT INTO `review` (`id`, `user_id`, `book_id`, `title`, `rating`, `text`, `image`) VALUES
(1, 1, 1, 'super scary', 3, 'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don\'t look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn\'t anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.', 'empty'),
(2, 1, 3, 'He\'s all alone', 3, 'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don\'t look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn\'t anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.', 'empty'),
(3, 1, 5, 'Normal people?', 3, 'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don\'t look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn\'t anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.', 'empty');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int NOT NULL,
  `name` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Gegevens worden geëxporteerd voor tabel `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Tabelstructuur voor tabel `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `role_id` int NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Gegevens worden geëxporteerd voor tabel `user`
--

INSERT INTO `user` (`id`, `role_id`, `first_name`, `last_name`, `date_of_birth`, `address`, `password`) VALUES
(1, 1, 'admin', 'admin', '2000-06-07', 'nederland', '$2a$10$LNpwuSnMs5BQDBDkmPY9C..k.R7qQABvRcJNSuw.XtdilZnJr.ifO');

--
-- Indexen voor geëxporteerde tabellen
--

--
-- Indexen voor tabel `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD KEY `book_book_type` (`book_type_id`);

--
-- Indexen voor tabel `book_type`
--
ALTER TABLE `book_type`
  ADD PRIMARY KEY (`id`);

--
-- Indexen voor tabel `payment_cart`
--
ALTER TABLE `payment_cart`
  ADD PRIMARY KEY (`id`),
  ADD KEY `payment_cart_user` (`user_id`);

--
-- Indexen voor tabel `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_review_book` (`book_id`) USING BTREE,
  ADD KEY `fk_review_user` (`user_id`);

--
-- Indexen voor tabel `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexen voor tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_role` (`role_id`) USING BTREE;

--
-- AUTO_INCREMENT voor geëxporteerde tabellen
--

--
-- AUTO_INCREMENT voor een tabel `book`
--
ALTER TABLE `book`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT voor een tabel `book_type`
--
ALTER TABLE `book_type`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT voor een tabel `payment_cart`
--
ALTER TABLE `payment_cart`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT voor een tabel `review`
--
ALTER TABLE `review`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT voor een tabel `role`
--
ALTER TABLE `role`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT voor een tabel `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Beperkingen voor geëxporteerde tabellen
--

--
-- Beperkingen voor tabel `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_book_book_type` FOREIGN KEY (`book_type_id`) REFERENCES `book_type` (`id`);

--
-- Beperkingen voor tabel `payment_cart`
--
ALTER TABLE `payment_cart`
  ADD CONSTRAINT `fk_payment_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Beperkingen voor tabel `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `fk_review_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
