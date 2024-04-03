-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysqldb:3306
-- Generation Time: Apr 03, 2024 at 11:56 AM
-- Server version: 8.0.36
-- PHP Version: 8.2.16

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
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE IF NOT EXISTS `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `book_type_id` int NOT NULL,
  `description` longtext NOT NULL,
  `genre` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `price` double NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `page_amount` int NOT NULL,
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `book_book_type` (`book_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `book_type`
--

DROP TABLE IF EXISTS `book_type`;
CREATE TABLE IF NOT EXISTS `book_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `attribute_type` varchar(100) NOT NULL,
  `has_attribute` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `book_type`
--

INSERT INTO `book_type` (`id`, `type`, `attribute_type`, `has_attribute`) VALUES
(1, 'EBook', 'HasAutomaticReading', 1),
(2, 'AudioBook', 'HasVoiceActor', 1),
(3, 'NormalBook', 'HasHardCover', 1);

-- --------------------------------------------------------

--
-- Table structure for table `payment_cart`
--

DROP TABLE IF EXISTS `payment_cart`;
CREATE TABLE IF NOT EXISTS `payment_cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `cart_details` json NOT NULL,
  PRIMARY KEY (`id`),
  KEY `payment_cart_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
CREATE TABLE IF NOT EXISTS `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `title` varchar(150) NOT NULL,
  `rating` double NOT NULL,
  `text` longtext NOT NULL,
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_review_book` (`book_id`) USING BTREE,
  KEY `fk_review_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`id`, `user_id`, `book_id`, `title`, `rating`, `text`, `image`) VALUES
(1, 1, 1, 'Amazing Horror Read', 4.5, 'The Haunting of Hill House is a gripping horror novel that kept me on the edge of my seat throughout. Highly recommended for horror enthusiasts!', 'haunting_of_hill_house.jpg'),
(2, 1, 2, 'Laugh Out Loud Comedy', 3.8, 'Funny Bones is a hilarious comedy book that had me laughing from start to finish. Great for anyone in need of a good laugh!', 'funny_bones.jpg'),
(3, 1, 3, 'Heartwarming Romance Story', 4.2, 'A Walk to Remember is a beautiful romance novel that touched my heart deeply. The characters are so well-developed, and the story is incredibly moving.', 'a_walk_to_remember.jpg'),
(4, 1, 4, 'Epic Sci-Fi Adventure', 4.7, 'Dune is a masterpiece of science fiction literature. The world-building is phenomenal, and the story is absolutely captivating. A must-read for all sci-fi fans!', 'dune.jpg'),
(5, 1, 5, 'Fantasy Classic', 4.8, 'The Hobbit is a timeless fantasy adventure that transports readers to a magical realm filled with wonder and excitement. A must-read for fantasy lovers of all ages!', 'the_hobbit.jpg'),
(6, 1, 6, 'Terrifying Clown Tale', 4.6, 'IT is a chilling horror novel that will haunt your dreams long after you finish reading. Stephen King\s storytelling prowess shines brightly in this spine-tingling tale.', 'it.jpg'),
(7, 1, 7, 'Timeless Romantic Classic', 4.3, 'Pride and Prejudice is a classic romance novel that continues to enchant readers with its timeless tale of love, misunderstandings, and societal norms. A true masterpiece!', 'pride_and_prejudice.jpg'),
(8, 1, 8, 'Out-of-This-World Sci-Fi', 4.9, 'The Martian is an exhilarating sci-fi thriller that kept me glued to the pages until the very end. Andy Weir\s attention to scientific detail adds a level of realism that makes the story even more compelling.', 'the_martian.jpg'),
(9, 1, 9, 'Fantasy Epic Adventure', 4.7, 'The Hobbit is an epic fantasy adventure that takes readers on a journey through Middle-earth filled with danger, courage, and magic. A timeless classic that every fantasy lover should read!', 'the_hobbit.jpg'),
(10, 1, 10, 'Hilarious Comedy-Drama', 4.4, 'Bridget Jones\s Diary is a delightful comedy-drama that offers a witty and relatable glimpse into the life of its titular character. A fun and entertaining read!', 'bridget_jones_diary.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'admin'),
(2, 'user');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_book_book_type` FOREIGN KEY (`book_type_id`) REFERENCES `book_type` (`id`);

--
-- Constraints for table `payment_cart`
--
ALTER TABLE `payment_cart`
  ADD CONSTRAINT `fk_payment_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `fk_review_book` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`),
  ADD CONSTRAINT `fk_review_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
