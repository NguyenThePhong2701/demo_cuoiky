-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 04, 2021 lúc 04:05 AM
-- Phiên bản máy phục vụ: 10.4.17-MariaDB
-- Phiên bản PHP: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `db_server`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `new_record`
--

CREATE TABLE `new_record` (
  `id` int(11) NOT NULL,
  `id_of_user` int(11) NOT NULL,
  `name` varchar(365) COLLATE utf8_vietnamese_ci NOT NULL,
  `birth_day` varchar(11) COLLATE utf8_vietnamese_ci NOT NULL,
  `sex` varchar(4) COLLATE utf8_vietnamese_ci NOT NULL,
  `height` varchar(4) COLLATE utf8_vietnamese_ci NOT NULL,
  `weight` varchar(4) COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `new_record`
--

INSERT INTO `new_record` (`id`, `id_of_user`, `name`, `birth_day`, `sex`, `height`, `weight`) VALUES
(24, 1, 'NORMAN', '26/5/2021', 'Nam', '175', '65'),
(26, 2, 'Name User 2', '5/9/2002', 'Nam', '176', '66'),
(30, 7, 'Name user 7', '27/2/2005', 'Nam', '175', '65'),
(31, 8, 'Name User 8', '11/7/2001', 'Nam', '175', '65');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `table3`
--

CREATE TABLE `table3` (
  `id` int(11) NOT NULL,
  `id_of_user` int(11) NOT NULL,
  `date` varchar(50) COLLATE utf8_vietnamese_ci NOT NULL,
  `time` varchar(7) COLLATE utf8_vietnamese_ci NOT NULL,
  `diseases` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `medicine` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `name_doctor` varchar(365) COLLATE utf8_vietnamese_ci NOT NULL,
  `number_phone_of_doctor` varchar(12) COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `table3`
--

INSERT INTO `table3` (`id`, `id_of_user`, `date`, `time`, `diseases`, `medicine`, `name_doctor`, `number_phone_of_doctor`) VALUES
(70, 1, '4/6/2021', '7 : 58', '[Achlorhydria, Acne]', '[Azathioprine, Benazepril]', 'Name A', '34234234');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `table_accounts`
--

CREATE TABLE `table_accounts` (
  `id` int(11) NOT NULL,
  `username` varchar(200) COLLATE utf8_vietnamese_ci NOT NULL,
  `password` varchar(32) COLLATE utf8_vietnamese_ci NOT NULL,
  `done` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `table_accounts`
--

INSERT INTO `table_accounts` (`id`, `username`, `password`, `done`) VALUES
(1, 'user1', '123', 1),
(2, 'user2', '123', 1),
(3, 'user3', '123', 0),
(4, 'user4', '123', 0),
(7, 'user7', '123', 1),
(8, 'user8', '123', 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `table_detail_medicine`
--

CREATE TABLE `table_detail_medicine` (
  `id` int(11) NOT NULL,
  `id_of_user` int(11) NOT NULL,
  `id_of_record` int(11) NOT NULL,
  `name_medicine` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `number_medicine` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `time_medicine` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL,
  `notice_medicine` longtext COLLATE utf8_vietnamese_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `table_detail_medicine`
--

INSERT INTO `table_detail_medicine` (`id`, `id_of_user`, `id_of_record`, `name_medicine`, `number_medicine`, `time_medicine`, `notice_medicine`) VALUES
(57, 1, 70, '[Benazepril]', '2', 'toi', 'Uong sau khi an'),
(58, 1, 70, '[Azathioprine]', '1', 'sang, toi', 'Uong truoc khi an');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `table_diseases`
--

CREATE TABLE `table_diseases` (
  `id` int(11) NOT NULL,
  `id_of_user` int(11) NOT NULL,
  `name_diseases` longtext COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `table_diseases`
--

INSERT INTO `table_diseases` (`id`, `id_of_user`, `name_diseases`) VALUES
(25, 1, 'Abacterial cystitis (Alkylating Agent Cystitis)'),
(26, 1, 'B12 Nutritional Deficiency'),
(27, 1, 'Abdominal Adhesions'),
(28, 1, 'B Cell Lymphoma'),
(29, 1, 'Gallbladder Obstruction w/o Calculus'),
(30, 1, 'Achlorhydria'),
(31, 1, 'Acne');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `table_medicine`
--

CREATE TABLE `table_medicine` (
  `id` int(11) NOT NULL,
  `id_of_user` int(11) NOT NULL,
  `name_medicine` longtext COLLATE utf8_vietnamese_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_vietnamese_ci;

--
-- Đang đổ dữ liệu cho bảng `table_medicine`
--

INSERT INTO `table_medicine` (`id`, `id_of_user`, `name_medicine`) VALUES
(27, 1, 'Abilify'),
(28, 1, 'Advil'),
(29, 1, 'Heparin'),
(30, 1, 'Benazepril'),
(31, 1, 'Azathioprine');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `new_record`
--
ALTER TABLE `new_record`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `table3`
--
ALTER TABLE `table3`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `table_accounts`
--
ALTER TABLE `table_accounts`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `table_detail_medicine`
--
ALTER TABLE `table_detail_medicine`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `table_diseases`
--
ALTER TABLE `table_diseases`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `table_medicine`
--
ALTER TABLE `table_medicine`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `new_record`
--
ALTER TABLE `new_record`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT cho bảng `table3`
--
ALTER TABLE `table3`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT cho bảng `table_accounts`
--
ALTER TABLE `table_accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `table_detail_medicine`
--
ALTER TABLE `table_detail_medicine`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT cho bảng `table_diseases`
--
ALTER TABLE `table_diseases`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT cho bảng `table_medicine`
--
ALTER TABLE `table_medicine`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
