-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 09, 2024 at 04:07 PM
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
-- Database: `2023189557_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `Admin`
--

CREATE TABLE `Admin` (
  `AdminID` int NOT NULL,
  `UserID` int NOT NULL,
  `AdminName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Admin`
--

INSERT INTO `Admin` (`AdminID`, `UserID`, `AdminName`) VALUES
(1, 27, NULL);

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
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `isbn`, `name`, `year`, `author`, `description`, `image`, `createdAt`, `updatedAt`) VALUES
(1, '9781785883262', 'Android Programming for Beginners', '2015', 'John Horton', 'Learn Android application development with this accessible tutorial that takes you through Android and Java programming with practical and actionable steps.', '9781785883262.png', '2018-04-22 13:57:30', '2024-06-12 01:32:41'),
(2, '9781787121799', 'Practical Game Design', '2018', 'Adam Kramarzewski, Ennio De Nucci', 'Design accessible and creative games across genres, platforms, and development realities', 'B06082.png', '2024-06-12 01:32:41', '2024-06-12 01:32:41'),
(3, '9781788293631', 'Learn Node.js by Building 6 Projects', '2018', 'Eduonix Learning Solutions', 'This is an advanced, practical guide to harnessing the power of Node.js by creating 6 full-scale real-world projects, from creating a chat application to an eLearning system.', 'B07858_cover.png', '2024-06-12 01:32:41', '2024-06-12 01:32:41'),
(17, '9781785283109', 'Multiplayer Game Development with HTML5', '2015', 'Rodrigo Silveira', 'Build fully-featured, highly interactive multiplayer games with HTML5', '3109OS_Multiplayer Game Development with HTML5.jpg', '2024-06-12 01:32:41', '2024-06-12 01:32:41'),
(18, '9781788299954', 'Integrating Twitter and Facebook into Your iOS Apps', '2018', 'Nick Walter', 'Learn how to integrate Twitter and Facebook APIs into iOS apps', 'V07323_low.png', '2024-06-12 01:32:41', '2024-06-12 01:32:41'),
(30, '11111', 'Java Programming', '2024', 'Ahmad Albab', 'Java Programming Java Programming sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss', 'picture.jpg', '2024-06-12 01:32:41', '2024-06-28 16:07:52'),
(31, '2222222', 'Ultraman Mebius', '2002', 'Ultraman Taro', ' The bondship of ultra brothers', '', '2024-06-12 01:40:02', '2024-06-12 01:40:02'),
(33, '2023189557', 'The art of defending in Football', '2002', 'Hafizuddin', 'Learn art of defending in football', '', '2024-06-13 23:35:03', '2024-06-13 23:35:03'),
(35, '123912', 'The art of attacking on football', '2024', 'Ahmad Jamal', 'The art of attacking on football', '', '2024-06-17 20:23:58', '2024-06-24 06:46:04'),
(40, '', '', '', '', '', '', '2024-06-27 14:38:50', '2024-06-27 14:38:50'),
(41, '3232', 'Ffff', '2002', 'jamal', 'asdasda', 'default.jpg', '2024-06-29 00:00:00', '2024-06-29 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `Bookings`
--

CREATE TABLE `Bookings` (
  `BookingID` int NOT NULL,
  `UserId` int NOT NULL,
  `CarId` int NOT NULL,
  `BookingDate` datetime NOT NULL,
  `Status` enum('New','Approved','Rejected') DEFAULT 'New',
  `Remarks` text,
  `AdminMsg` text,
  `CreatedAt` datetime DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Bookings`
--

INSERT INTO `Bookings` (`BookingID`, `UserId`, `CarId`, `BookingDate`, `Status`, `Remarks`, `AdminMsg`, `CreatedAt`, `image`) VALUES
(1, 3, 1, '2024-06-29 00:00:00', 'New', NULL, NULL, '2024-06-29 23:22:57', ''),
(2, 3, 2, '2024-06-30 00:00:00', 'New', NULL, NULL, '2024-06-30 10:15:22', ''),
(3, 3, 3, '2024-07-01 00:00:00', 'New', NULL, NULL, '2024-07-01 14:30:45', ''),
(4, 3, 4, '2024-07-02 00:00:00', 'New', NULL, NULL, '2024-07-02 08:45:01', ''),
(7, 3, 15, '2024-07-07 00:00:00', 'Approved', 'Front of UITM Jasin Melaka', NULL, '2024-07-07 07:40:02', ''),
(8, 4, 3, '2024-07-17 00:00:00', 'Approved', 'In front of Kampung Mendapat', 'noted', '2024-07-07 08:18:11', ''),
(12, 4, 4, '2024-07-08 00:00:00', 'Approved', 'Fuel Tank full', 'Noted', '2024-07-07 17:01:17', ''),
(13, 4, 2, '2024-07-08 00:00:00', 'Approved', 'Hantar depan lanang A', 'noted', '2024-07-08 09:09:32', '');

-- --------------------------------------------------------

--
-- Table structure for table `Cars`
--

CREATE TABLE `Cars` (
  `CarID` int NOT NULL,
  `Model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PlateNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `availability` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'available',
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Cars`
--

INSERT INTO `Cars` (`CarID`, `Model`, `Brand`, `PlateNumber`, `availability`, `CreatedAt`) VALUES
(1, 'Axia', 'Perodua', 'WXY 1234', 'available', '2023-01-15 00:00:00'),
(2, 'Myvi', 'Perodua', 'BHH 5678', 'available', '2023-02-20 00:00:00'),
(3, 'Vios', 'Toyota', 'JJK 9101', 'available', '2023-03-12 00:00:00'),
(4, 'City', 'Honda', 'PQR 3456', 'available', '2023-04-08 00:00:00'),
(5, 'Civic', 'Honda', 'KLM 7890', 'available', '2023-05-25 00:00:00'),
(6, 'Alza', 'Perodua', 'NST 2345', 'available', '2023-06-01 00:00:00'),
(7, 'Saga', 'Proton', 'VVV 6789', 'available', '2023-06-15 00:00:00'),
(8, 'Iriz', 'Proton', 'CCC 1122', 'available', '2023-07-10 00:00:00'),
(9, 'X70', 'Proton', 'DDD 3344', 'available', '2023-08-18 00:00:00'),
(14, 'Persona', 'Proton', 'JHH 5124', 'available', '2024-07-06 00:00:00'),
(15, 'Exora', 'Proton', 'YSS 5121', 'available', '2024-07-07 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `Customer`
--

CREATE TABLE `Customer` (
  `CustID` int NOT NULL,
  `UserID` int NOT NULL,
  `CustName` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `Customer`
--

INSERT INTO `Customer` (`CustID`, `UserID`, `CustName`) VALUES
(1, 28, NULL);

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
-- Table structure for table `maintenance`
--

CREATE TABLE `maintenance` (
  `MaintenanceID` int NOT NULL,
  `CarId` int NOT NULL,
  `MaintenanceDate` datetime NOT NULL,
  `UpdateMaintenanceDate` datetime NOT NULL,
  `Cost` decimal(10,2) NOT NULL,
  `Type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notes`
--

CREATE TABLE `notes` (
  `note_id` int NOT NULL,
  `note_title` varchar(256) NOT NULL,
  `note_desc` varchar(1028) NOT NULL,
  `author_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `notes`
--

INSERT INTO `notes` (`note_id`, `note_title`, `note_desc`, `author_id`) VALUES
(1, 'note 1', 'note desc 1', 3);

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
-- Table structure for table `seller`
--

CREATE TABLE `seller` (
  `sellerId` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `createdAt` datetime NOT NULL,
  `updatedAt` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `shop`
--

CREATE TABLE `shop` (
  `shopId` int NOT NULL,
  `address` varchar(255) NOT NULL,
  `phoneNo` int NOT NULL,
  `sellerId` int NOT NULL
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
(2, 'admin@example.com', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'cc7991e0-c497-4951-ac98-83d5ab747c3e', '2024-07-08 08:09:12', 'admin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(3, 'user@example.com', 'user', 'ee11cbb19052e40b07aac0ca060c23ee', '7f5f67f5-b279-4428-9c22-fea2d473af9a', '2024-07-09 12:07:20', 'customer', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(4, 'customer@gmail.com', 'customer', '91ec1f9324753048c0096d036a694f86', '4e18095a-9ba3-4b3f-a3d1-1f7a040ce045', '2024-07-08 09:08:57', 'customer', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(27, 'hafiz@gmail.com', 'hafiz', '839a54bf20626e4942bc8f11873f0654', '00000000-00000-0000-0000-000000000000', '0000-01-01 00:00:00', 'admin', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e'),
(28, 'danial@gmail.com', 'danial', 'ab5e831d8600f15167307bd73177b525', '00000000-00000-0000-0000-000000000000', '0000-01-01 00:00:00', 'customer', 1, '206b2dbe-ecc9-490b-b81b-83767288bc5e');

--
-- Triggers `users`
--
DELIMITER $$
CREATE TRIGGER `after_user_insert` AFTER INSERT ON `users` FOR EACH ROW BEGIN
  IF (NEW.role = 'Admin') THEN
    INSERT INTO Admin (UserID) VALUES (NEW.id);
  ELSE
    INSERT INTO Customer (UserID) VALUES (NEW.id);
  END IF;
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Admin`
--
ALTER TABLE `Admin`
  ADD PRIMARY KEY (`AdminID`),
  ADD KEY `Admin` (`UserID`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `Bookings`
--
ALTER TABLE `Bookings`
  ADD PRIMARY KEY (`BookingID`),
  ADD KEY `UserId` (`UserId`),
  ADD KEY `CarId` (`CarId`);

--
-- Indexes for table `Cars`
--
ALTER TABLE `Cars`
  ADD PRIMARY KEY (`CarID`);

--
-- Indexes for table `Customer`
--
ALTER TABLE `Customer`
  ADD PRIMARY KEY (`CustID`),
  ADD KEY `Customer` (`UserID`);

--
-- Indexes for table `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `maintenance`
--
ALTER TABLE `maintenance`
  ADD PRIMARY KEY (`MaintenanceID`),
  ADD KEY `CarId` (`CarId`);

--
-- Indexes for table `notes`
--
ALTER TABLE `notes`
  ADD PRIMARY KEY (`note_id`),
  ADD KEY `author_id` (`author_id`);

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
-- Indexes for table `seller`
--
ALTER TABLE `seller`
  ADD PRIMARY KEY (`sellerId`);

--
-- Indexes for table `shop`
--
ALTER TABLE `shop`
  ADD PRIMARY KEY (`shopId`),
  ADD KEY `sellerId` (`sellerId`);

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
-- AUTO_INCREMENT for table `Admin`
--
ALTER TABLE `Admin`
  MODIFY `AdminID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `Bookings`
--
ALTER TABLE `Bookings`
  MODIFY `BookingID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `Cars`
--
ALTER TABLE `Cars`
  MODIFY `CarID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `Customer`
--
ALTER TABLE `Customer`
  MODIFY `CustID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `files`
--
ALTER TABLE `files`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `maintenance`
--
ALTER TABLE `maintenance`
  MODIFY `MaintenanceID` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notes`
--
ALTER TABLE `notes`
  MODIFY `note_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
-- AUTO_INCREMENT for table `seller`
--
ALTER TABLE `seller`
  MODIFY `sellerId` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `shop`
--
ALTER TABLE `shop`
  MODIFY `shopId` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Admin`
--
ALTER TABLE `Admin`
  ADD CONSTRAINT `Admin` FOREIGN KEY (`UserID`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `Bookings`
--
ALTER TABLE `Bookings`
  ADD CONSTRAINT `Bookings_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `Bookings_ibfk_2` FOREIGN KEY (`CarId`) REFERENCES `Cars` (`CarID`);

--
-- Constraints for table `Customer`
--
ALTER TABLE `Customer`
  ADD CONSTRAINT `Customer` FOREIGN KEY (`UserID`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Constraints for table `maintenance`
--
ALTER TABLE `maintenance`
  ADD CONSTRAINT `maintenance_ibfk_1` FOREIGN KEY (`CarId`) REFERENCES `Cars` (`CarID`);

--
-- Constraints for table `notes`
--
ALTER TABLE `notes`
  ADD CONSTRAINT `FK_AUTHOR` FOREIGN KEY (`author_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `shop`
--
ALTER TABLE `shop`
  ADD CONSTRAINT `shop_ibfk_1` FOREIGN KEY (`sellerId`) REFERENCES `seller` (`sellerId`) ON DELETE RESTRICT ON UPDATE RESTRICT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
