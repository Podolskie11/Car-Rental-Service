-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 05, 2024 at 09:02 AM
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
-- Table structure for table `Cars`
--

CREATE TABLE `Cars` (
  `CarID` int NOT NULL,
  `Model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `PlateNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `availability` enum('available','not available') DEFAULT NULL,
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
(10, 'CR-V', 'Honda', 'EEE 5566', 'available', '2023-09-05 00:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Cars`
--
ALTER TABLE `Cars`
  ADD PRIMARY KEY (`CarID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Cars`
--
ALTER TABLE `Cars`
  MODIFY `CarID` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
