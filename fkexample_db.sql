-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 01, 2024 at 02:24 PM
-- Server version: 8.0.37-0ubuntu0.20.04.3
-- PHP Version: 7.2.34-45+ubuntu20.04.1+deb.sury.org+1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fkexample_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` int NOT NULL,
  `isbn` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `year` varchar(4) NOT NULL,
  `author` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `image` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL,
  `category_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `isbn`, `name`, `year`, `author`, `description`, `image`, `createdAt`, `updatedAt`, `category_id`) VALUES
(1, '9781785883262', 'Android Programming for Beginners', '2015', 'John Horton', 'Learn Android application development with this accessible tutorial that takes you through Android and Java programming with practical and actionable steps.', 'uploads/files/3-default.png', '2018-04-22 13:57:30', '2018-04-22 13:57:30', NULL),
(2, '9781787121799', 'Practical Game Design', '2018', 'Adam Kramarzewski, Ennio De Nucci', 'Design accessible and creative games across genres, platforms, and development realities', 'uploads/files/3-default.png', '2018-04-22 13:57:30', '2018-04-22 13:57:30', NULL),
(3, '9781788293631', 'Learn Node.js by Building 6 Projects', '2018', 'Eduonix Learning Solutions', 'This is an advanced, practical guide to harnessing the power of Node.js by creating 6 full-scale real-world projects, from creating a chat application to an eLearning system.', 'uploads/files/3-default.png', '2018-04-22 13:57:14', '2024-06-26 21:53:45', 2),
(4, '1111', 'qqq', '2000', 'aaa', 'sss', 'uploads/files/3-default.png', '2018-04-22 13:57:30', '2018-04-22 13:57:30', NULL),
(17, '9781785283109', 'Multiplayer Game Development with HTML5', '2015', 'Rodrigo Silveira', 'Build fully-featured, highly interactive multiplayer games with HTML5', 'uploads/files/3-default.png', '2018-04-23 18:46:36', '2018-04-23 18:46:36', 2),
(18, '9781788299954', 'Integrating Twitter and Facebook into Your iOS Apps', '2018', 'Nick Walter', 'Learn how to integrate Twitter and Facebook APIs into iOS apps', 'uploads/files/3-default.png', '2018-04-23 18:47:04', '2018-04-23 18:47:04', 2),
(19, '1111', 'kkkk', '2000', 'kkkk', 'kkk', 'uploads/files/9-IMG_20240701_175055.jpg', '2024-06-26 21:09:45', '2024-07-01 21:46:30', 1),
(22, '1234', 'picture test', '2024', 'picture test', 'picture test', 'uploads/files/8-IMG_20240701_175054.jpg', '2024-07-01 19:50:18', '2024-07-01 19:50:18', 1);

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `id` int NOT NULL,
  `book_id` int NOT NULL,
  `user_id` int NOT NULL,
  `borrow_date` date NOT NULL,
  `return_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `borrow`
--

INSERT INTO `borrow` (`id`, `book_id`, `user_id`, `borrow_date`, `return_date`) VALUES
(1, 2, 3, '2024-06-26', '2024-06-29'),
(2, 3, 3, '2024-06-28', '2024-06-28'),
(3, 2, 3, '2024-06-28', '2024-07-06'),
(4, 19, 3, '2024-06-27', '2024-06-15');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int NOT NULL,
  `categoryName` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `categoryName`) VALUES
(1, 'fiction'),
(2, 'non-fiction');

-- --------------------------------------------------------

--
-- Table structure for table `files`
--

CREATE TABLE `files` (
  `id` int NOT NULL,
  `file` varchar(512) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `files`
--

INSERT INTO `files` (`id`, `file`) VALUES
(1, 'uploads/files/1-IMG_20240701_175055.jpg'),
(2, 'uploads/files/2-IMG_20240701_175055.jpg'),
(3, 'uploads/files/3-default.png'),
(4, 'uploads/files/4-1298885146'),
(5, 'uploads/files/5-1298885146'),
(6, 'uploads/files/6-IMG_20240701_175054.jpg'),
(7, 'uploads/files/7-IMG_20240701_175054.jpg'),
(8, 'uploads/files/8-IMG_20240701_175054.jpg'),
(9, 'uploads/files/9-IMG_20240701_175055.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `organizations`
--

CREATE TABLE `organizations` (
  `id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `license` varchar(15) NOT NULL DEFAULT 'basic',
  `validity` datetime NOT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `theme_bg` varchar(20) DEFAULT NULL,
  `theme_col` varchar(20) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `org_secret` varchar(50) NOT NULL,
  `secret` varchar(50) NOT NULL DEFAULT '206b2dbe-ecc9-490b-b81b-83767288bc5e'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `organizations`
--

INSERT INTO `organizations` (`id`, `name`, `email`, `license`, `validity`, `logo`, `theme_bg`, `theme_col`, `is_active`, `org_secret`, `secret`) VALUES
(1, 'Default Organization', 'superadmin@example.com', 'super', '0000-01-01 00:00:00', NULL, NULL, NULL, 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e', '206b2dbe-ecc9-490b-b81b-83767288bc5e');

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `first_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `last_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `theme_bg` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `theme_col` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci DEFAULT NULL,
  `secret` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `token` varchar(50) NOT NULL DEFAULT '00000000-00000-0000-0000-000000000000',
  `lease` datetime NOT NULL DEFAULT '0000-01-01 00:00:00',
  `role` varchar(50) DEFAULT 'user',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `secret` varchar(50) NOT NULL DEFAULT '206b2dbe-ecc9-490b-b81b-83767288bc5e'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `email`, `username`, `password`, `token`, `lease`, `role`, `is_active`, `secret`) VALUES
(1, 'superadmin@example.com', 'superadmin', '17c4520f6cfd1ab53d8745e84681eb49', '1', '0000-01-01 00:00:00', 'superadmin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(2, 'admin@example.com', 'admin', '21232f297a57a5a743894a0e4a801fc3', '1', '0000-01-01 00:00:00', 'admin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(3, 'user@example.com', 'user', 'ee11cbb19052e40b07aac0ca060c23ee', 'c9ac1ed9-ce74-4fd3-9fbf-d8af11e02589', '2024-07-01 09:09:06', 'user', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_category` (`category_id`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `book_id` (`book_id`,`user_id`,`borrow_date`),
  ADD KEY `idx_book_id` (`book_id`),
  ADD KEY `idx_user_id` (`user_id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `categoryName_UNIQUE` (`categoryName`);

--
-- Indexes for table `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `organizations`
--
ALTER TABLE `organizations`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `org_secret` (`org_secret`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `files`
--
ALTER TABLE `files`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `organizations`
--
ALTER TABLE `organizations`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `profiles`
--
ALTER TABLE `profiles`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `borrow`
--
ALTER TABLE `borrow`
  ADD CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
