-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.42 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for homestay_management_db
CREATE DATABASE IF NOT EXISTS `homestay_management_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `homestay_management_db`;

-- Dumping structure for table homestay_management_db.amenities
CREATE TABLE IF NOT EXISTS `amenities` (
  `amenity_id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `amenity_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`amenity_id`),
  KEY `idx_amenities_category_id` (`category_id`),
  CONSTRAINT `fk_amenities_to_amenity_categories` FOREIGN KEY (`category_id`) REFERENCES `amenity_categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.amenities: ~23 rows (approximately)
INSERT INTO `amenities` (`amenity_id`, `category_id`, `amenity_name`) VALUES
	(1, 4, 'WiFi miễn phí'),
	(3, 2, 'Giấy vệ sinh'),
	(4, 2, 'Khăn tắm'),
	(5, 2, 'Máy sấy tóc'),
	(7, 1, 'Giường cực dài (> 2 mét)'),
	(8, 10, 'Có chỗ đỗ xe riêng miễn phí tại chỗ (không cần đặt chỗ trước).'),
	(9, 10, 'Nhà để xe'),
	(10, 9, 'Sân thượng / hiên'),
	(12, 3, 'Ấm đun nước điện'),
	(13, 1, 'Tủ hoặc phòng để quần áo'),
	(14, 1, 'Phòng thay quần áo'),
	(15, 7, 'Quầy bar'),
	(16, 14, 'Bình chữa cháy'),
	(17, 14, 'Thiết bị báo cháy'),
	(18, 15, 'Khu vực cho phép hút thuốc'),
	(19, 15, 'Điều hòa nhiệt độ'),
	(20, 15, 'Cấm hút thuốc trong toàn bộ khuôn viên'),
	(21, 15, 'Dịch vụ phòng'),
	(22, 14, 'Báo động an ninh'),
	(23, 8, 'Tầm nhìn ra khung cảnh'),
	(24, 9, 'Ban công'),
	(25, 6, 'TV màn hình phẳng'),
	(26, 5, 'Giá treo quần áo');

-- Dumping structure for table homestay_management_db.amenity_categories
CREATE TABLE IF NOT EXISTS `amenity_categories` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `icon` varchar(50) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.amenity_categories: ~15 rows (approximately)
INSERT INTO `amenity_categories` (`category_id`, `category_name`, `icon`, `description`) VALUES
	(1, 'Phòng ngủ', 'fa-bed', ''),
	(2, 'Phòng tắm', 'mdi:shower', ''),
	(3, 'Nhà bếp', 'fa6-solid:kitchen-set', ''),
	(4, 'Internet', 'lucide:wifi', ''),
	(5, 'Tiện ích trong phòng', 'mdi:google-classroom', ''),
	(6, 'Truyền thông & Công nghệ', 'mdi:television-play', ''),
	(7, 'Đồ ăn & thức uống', 'mdi:food', ''),
	(8, 'Tầm nhìn', 'mdi:view-column-outline', ''),
	(9, 'Ngoài trời', 'mdi:weather-sunny', ''),
	(10, 'Chỗ đậu xe', 'mdi:parking', ''),
	(11, 'Dịch vụ lễ tân', 'mdi:face-recognition', ''),
	(12, 'Dịch vụ lau dọn', 'mdi:broom', ''),
	(13, 'Hoạt động', 'mdi:playlist-minus', ''),
	(14, 'An ninh', 'mdi:shield-home-outline', ''),
	(15, 'Tổng quát', 'mdi:home-circle-outline', '');

-- Dumping structure for table homestay_management_db.bookings
CREATE TABLE IF NOT EXISTS `bookings` (
  `booking_id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `room_id` bigint NOT NULL,
  `check_in` datetime NOT NULL,
  `check_out` datetime NOT NULL,
  `guest_count` int NOT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED','COMPLETED') NOT NULL DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `paid_amount` decimal(10,2) DEFAULT NULL,
  `has_sent_reminder` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`booking_id`),
  KEY `idx_bookings_customer_id` (`customer_id`),
  KEY `idx_bookings_room_id` (`room_id`),
  CONSTRAINT `fk_bookings_to_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`),
  CONSTRAINT `fk_bookings_to_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `ck_bookings_guest_count` CHECK ((`guest_count` >= 1)),
  CONSTRAINT `ck_bookings_paid_amount` CHECK ((`paid_amount` >= 0)),
  CONSTRAINT `ck_bookings_total_amount` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=168 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.bookings: ~12 rows (approximately)
INSERT INTO `bookings` (`booking_id`, `customer_id`, `room_id`, `check_in`, `check_out`, `guest_count`, `status`, `created_at`, `updated_at`, `total_amount`, `paid_amount`, `has_sent_reminder`) VALUES
	(137, 14, 2, '2025-05-13 21:00:00', '2025-05-14 09:00:00', 1, 'COMPLETED', '2025-05-13 17:32:06', '2025-05-16 17:00:38', 550000.00, 550000.00, 1),
	(138, 13, 2, '2025-05-15 14:30:00', '2025-05-16 02:30:00', 1, 'CANCELLED', '2025-05-14 14:26:56', '2025-05-14 14:28:14', 820000.00, NULL, 1),
	(139, 13, 2, '2025-05-14 23:30:00', '2025-05-15 11:00:00', 1, 'COMPLETED', '2025-05-14 14:39:41', '2025-05-15 15:11:11', 545000.00, 545000.00, 1),
	(140, 15, 4, '2025-05-15 09:30:00', '2025-05-15 13:30:00', 3, 'COMPLETED', '2025-05-15 08:56:08', '2025-05-15 16:35:31', 570000.00, 570000.00, 1),
	(141, 23, 3, '2025-05-15 22:00:00', '2025-05-16 09:00:00', 3, 'COMPLETED', '2025-05-15 21:54:21', '2025-05-16 12:06:43', 540000.00, 540000.00, 1),
	(153, 13, 2, '2025-05-16 16:00:00', '2025-05-16 20:00:00', 1, 'COMPLETED', '2025-05-16 12:53:59', '2025-05-17 07:12:59', 420000.00, 420000.00, 1),
	(154, 15, 5, '2025-05-16 18:00:00', '2025-05-16 21:00:00', 2, 'COMPLETED', '2025-05-16 17:06:29', '2025-05-17 07:12:59', 300000.00, 300000.00, 1),
	(155, 15, 7, '2025-05-23 13:00:00', '2025-05-23 16:00:00', 1, 'COMPLETED', '2025-05-23 09:06:59', '2025-05-24 09:48:22', 220000.00, 220000.00, 1),
	(156, 15, 7, '2025-05-24 13:00:00', '2025-05-24 16:00:00', 1, 'COMPLETED', '2025-05-24 12:02:35', '2025-05-25 15:42:13', 220000.00, 220000.00, 1),
	(157, 24, 3, '2025-05-25 17:00:00', '2025-05-25 21:00:00', 3, 'COMPLETED', '2025-05-25 16:25:22', '2025-05-26 09:06:52', 595000.00, 595000.00, 1),
	(164, 24, 8, '2025-05-26 12:00:00', '2025-05-26 15:00:00', 3, 'COMPLETED', '2025-05-25 19:53:17', '2025-05-27 09:30:55', 450000.00, 450000.00, 1),
	(166, 12, 5, '2025-05-31 16:00:00', '2025-06-01 17:00:00', 3, 'COMPLETED', '2025-05-31 12:52:56', '2025-06-02 16:12:23', 680000.00, 680000.00, 1),
	(167, 15, 4, '2025-05-31 22:00:00', '2025-06-01 08:00:00', 4, 'COMPLETED', '2025-05-31 18:47:00', '2025-06-02 16:12:23', 1190000.00, 1190000.00, 0);

-- Dumping structure for table homestay_management_db.booking_extensions
CREATE TABLE IF NOT EXISTS `booking_extensions` (
  `extension_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `extended_hours` float NOT NULL DEFAULT (0),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`extension_id`),
  KEY `idx_booking_extensions_booking_id` (`booking_id`),
  CONSTRAINT `fk_booking_extensions_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_booking_extensions_extended_hours` CHECK ((`extended_hours` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.booking_extensions: ~3 rows (approximately)
INSERT INTO `booking_extensions` (`extension_id`, `booking_id`, `extended_hours`, `created_at`) VALUES
	(15, 137, 1, '2025-05-13 22:25:25'),
	(16, 153, 1, '2025-05-16 13:06:58'),
	(17, 157, 1, '2025-05-25 16:28:40'),
	(18, 166, 1, '2025-05-31 12:54:00');

-- Dumping structure for table homestay_management_db.booking_pricing_snapshots
CREATE TABLE IF NOT EXISTS `booking_pricing_snapshots` (
  `snapshot_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `base_duration` int NOT NULL,
  `base_price` decimal(10,2) NOT NULL,
  `extra_hour_price` decimal(10,2) NOT NULL,
  `overnight_price` decimal(10,2) NOT NULL,
  `daily_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`snapshot_id`),
  KEY `idx_booking_pricing_snapshots_booking_id` (`booking_id`),
  CONSTRAINT `fk_booking_pricing_snapshots_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_booking_pricing_snapshots_base_duration` CHECK ((`base_duration` >= 1)),
  CONSTRAINT `ck_booking_pricing_snapshots_base_price` CHECK ((`base_price` >= 0)),
  CONSTRAINT `ck_booking_pricing_snapshots_daily_price` CHECK ((`daily_price` >= 0)),
  CONSTRAINT `ck_booking_pricing_snapshots_extra_hour_price` CHECK ((`extra_hour_price` >= 0)),
  CONSTRAINT `ck_booking_pricing_snapshots_overnight_price` CHECK ((`overnight_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.booking_pricing_snapshots: ~12 rows (approximately)
INSERT INTO `booking_pricing_snapshots` (`snapshot_id`, `booking_id`, `base_duration`, `base_price`, `extra_hour_price`, `overnight_price`, `daily_price`) VALUES
	(1, 137, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(2, 138, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(3, 139, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(4, 140, 3, 150000.00, 40000.00, 260000.00, 320000.00),
	(5, 141, 3, 300000.00, 60000.00, 380000.00, 520000.00),
	(18, 153, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(19, 154, 3, 300000.00, 60000.00, 380000.00, 520000.00),
	(20, 155, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(21, 156, 3, 220000.00, 50000.00, 320000.00, 450000.00),
	(22, 157, 3, 300000.00, 60000.00, 380000.00, 520000.00),
	(29, 164, 3, 300000.00, 60000.00, 380000.00, 520000.00),
	(31, 166, 3, 300000.00, 60000.00, 380000.00, 520000.00),
	(32, 167, 3, 150000.00, 40000.00, 260000.00, 320000.00);

-- Dumping structure for table homestay_management_db.booking_services
CREATE TABLE IF NOT EXISTS `booking_services` (
  `booking_service_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `service_id` bigint NOT NULL,
  `quantity` float DEFAULT NULL,
  `status` enum('PENDING','IN_PROGRESS','COMPLETED','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`booking_service_id`),
  KEY `idx_booking_services_booking_id` (`booking_id`),
  KEY `idx_booking_services_service_id` (`service_id`),
  CONSTRAINT `fk_booking_services_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_booking_services_to_services` FOREIGN KEY (`service_id`) REFERENCES `services` (`service_id`),
  CONSTRAINT `ck_booking_services_quantity` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.booking_services: ~12 rows (approximately)
INSERT INTO `booking_services` (`booking_service_id`, `booking_id`, `service_id`, `quantity`, `status`, `created_at`, `updated_at`, `description`) VALUES
	(75, 137, 6, 1, 'COMPLETED', '2025-05-13 17:32:06', '2025-05-14 09:04:54', ''),
	(77, 137, 2, 6, 'COMPLETED', '2025-05-14 05:42:09', '2025-05-15 21:25:29', ' '),
	(78, 138, 7, 1, 'CANCELLED', '2025-05-14 14:26:57', NULL, ''),
	(79, 139, 7, 1, 'COMPLETED', '2025-05-14 14:39:42', '2025-05-15 21:25:16', ''),
	(80, 141, 6, 1, 'COMPLETED', '2025-05-15 21:54:21', NULL, ''),
	(98, 153, 7, 1, 'COMPLETED', '2025-05-16 12:53:59', '2025-05-23 10:34:39', ''),
	(99, 157, 7, 1, 'COMPLETED', '2025-05-25 16:25:22', '2025-05-25 16:37:51', ''),
	(100, 157, 5, 3, 'COMPLETED', '2025-05-25 16:28:26', '2025-05-25 17:33:48', ' '),
	(102, 157, 2, 5, 'COMPLETED', '2025-05-25 17:43:36', '2025-05-25 18:14:37', ''),
	(104, 164, 7, 1, 'COMPLETED', '2025-05-25 19:54:39', '2025-05-26 09:44:52', ''),
	(107, 166, 6, 1, 'COMPLETED', '2025-05-31 12:53:26', '2025-05-31 12:53:44', ''),
	(110, 167, 7, 1, 'PENDING', '2025-05-31 18:48:03', NULL, '');

-- Dumping structure for table homestay_management_db.branches
CREATE TABLE IF NOT EXISTS `branches` (
  `branch_id` bigint NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  `gate_password` varchar(50) NOT NULL,
  PRIMARY KEY (`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.branches: ~2 rows (approximately)
INSERT INTO `branches` (`branch_id`, `branch_name`, `address`, `phone`, `image`, `gate_password`) VALUES
	(6, 'Lullaby HQV', 'Đường Số 3, khu đô thị mới, Hương Thủy, Thành phố Huế', '0983652165', '1740106668129-lullaby.jpg', '6354'),
	(9, 'Lullaby AC', 'Số 035 đường số 14, An Cựu City Huế, Thành phố Huế', '0983652689', '1740821395697-536466803.jpg', '1257');

-- Dumping structure for table homestay_management_db.customers
CREATE TABLE IF NOT EXISTS `customers` (
  `customer_id` bigint NOT NULL AUTO_INCREMENT,
  `reward_points` float DEFAULT NULL,
  `customer_type_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`customer_id`),
  KEY `idx_customers_customer_type_id` (`customer_type_id`),
  KEY `idx_customers_user_id` (`user_id`),
  CONSTRAINT `fk_customers_to_customer_types` FOREIGN KEY (`customer_type_id`) REFERENCES `customer_types` (`customer_type_id`),
  CONSTRAINT `fk_customers_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `ck_customers_reward_points` CHECK ((`reward_points` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.customers: ~6 rows (approximately)
INSERT INTO `customers` (`customer_id`, `reward_points`, `customer_type_id`, `user_id`) VALUES
	(12, 68, 1, 4),
	(13, 96, 1, 5),
	(14, 52, 1, 6),
	(15, 250, 2, 7),
	(23, 54, 1, 15),
	(24, 104, 1, 18);

-- Dumping structure for table homestay_management_db.customer_types
CREATE TABLE IF NOT EXISTS `customer_types` (
  `customer_type_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `discount_rate` float NOT NULL DEFAULT (0),
  `min_point` float NOT NULL DEFAULT (0),
  PRIMARY KEY (`customer_type_id`),
  CONSTRAINT `ck_customer_types_discount_rate` CHECK ((`discount_rate` >= 0)),
  CONSTRAINT `ck_customer_types_min_point` CHECK ((`min_point` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.customer_types: ~4 rows (approximately)
INSERT INTO `customer_types` (`customer_type_id`, `name`, `description`, `discount_rate`, `min_point`) VALUES
	(1, 'Lullaby Newbie', '', 0, 0),
	(2, 'Lullaby Dreamer', '', 5, 200),
	(3, 'Lullaby Chill', '', 10, 500),
	(5, 'Lullaby VibeKing', '', 15, 800);

-- Dumping structure for table homestay_management_db.email_verification_tokens
CREATE TABLE IF NOT EXISTS `email_verification_tokens` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(50) NOT NULL,
  `user_id` bigint NOT NULL,
  `expiry_date` datetime NOT NULL,
  `is_used` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`token_id`),
  KEY `idx_email_verification_user_id` (`user_id`),
  CONSTRAINT `fk_email_verification_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.email_verification_tokens: ~2 rows (approximately)
INSERT INTO `email_verification_tokens` (`token_id`, `token`, `user_id`, `expiry_date`, `is_used`) VALUES
	(8, 'ae79632c-730b-4f32-9d4a-3e9b5e4b4a0e', 15, '2025-05-16 20:52:44', 0),
	(10, '24ac0abc-f972-45e5-811a-c0b19ad6b14f', 18, '2025-05-26 16:21:58', 1);

-- Dumping structure for table homestay_management_db.employees
CREATE TABLE IF NOT EXISTS `employees` (
  `employee_id` bigint NOT NULL AUTO_INCREMENT,
  `salary` decimal(10,2) NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`employee_id`),
  KEY `idx_employees_user_id` (`user_id`),
  CONSTRAINT `fk_employees_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `ck_employees_salary` CHECK ((`salary` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.employees: ~3 rows (approximately)
INSERT INTO `employees` (`employee_id`, `salary`, `user_id`) VALUES
	(8, 8200000.00, 3),
	(9, 6200000.00, 16),
	(10, 6000000.00, 17);

-- Dumping structure for table homestay_management_db.faqs
CREATE TABLE IF NOT EXISTS `faqs` (
  `faq_id` bigint NOT NULL AUTO_INCREMENT,
  `question` varchar(500) NOT NULL,
  `answer` varchar(2000) NOT NULL,
  PRIMARY KEY (`faq_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.faqs: ~4 rows (approximately)
INSERT INTO `faqs` (`faq_id`, `question`, `answer`) VALUES
	(2, 'Lullaby homestay có những loại phòng nào?', 'Lựa chọn phòng tại Lullaby homestay bao gồm:\nPhòng giường đôi, Phòng giường đơn,\nGiường trong phòng tập thể'),
	(4, 'Chi phí nghỉ tại Lullaby homestay là bao nhiêu?', 'Giá tại Lullaby homestay có thể khác nhau tùy vào kỳ nghỉ của bạn (ví dụ: ngày, loại phòng bạn chọn,..). Vui lòng xem giá bằng cách chọn phòng và ngày bạn muốn ở.'),
	(7, 'Lullaby homestay cách trung tâm Huế bao xa?', 'Lullaby homestay cách trung tâm Huế 2,3 km.'),
	(8, 'Làm thế nào để đến với Lullaby Homestay?', 'Bạn có thể dễ dàng tìm thấy địa chỉ chi tiết của Lullaby Homestay ngay tại trang chủ của chúng tôi. Nếu cần hỗ trợ thêm, vui lòng liên hệ qua số điện thoại được hiển thị trên website.\n\nLullaby hoạt động theo mô hình tự check-in. Sau khi hoàn tất việc đặt phòng và thanh toán, bạn vui lòng kiểm tra email khoảng 30 phút trước giờ nhận phòng để nhận hướng dẫn chi tiết về cách vào homestay, bao gồm mã khóa cửa và các thông tin cần thiết khác.');

-- Dumping structure for table homestay_management_db.homestay_details
CREATE TABLE IF NOT EXISTS `homestay_details` (
  `infor_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(2500) NOT NULL,
  `description` varchar(4000) NOT NULL,
  PRIMARY KEY (`infor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.homestay_details: ~3 rows (approximately)
INSERT INTO `homestay_details` (`infor_id`, `title`, `description`) VALUES
	(2, 'Ý nghĩa tên home', 'Lullaby có nghĩa là một bản nhạc dịu êm, nhẹ nhàng ru em bé vào giấc ngủ. Trong tiếng Anh, Lullaby được xem là một trong những từ ngữ có ý nghĩa xinh đẹp nhất.'),
	(4, 'Khoảng cách địa điểm', 'Cách Cầu Tràng Tiền 3.3 km, Lullaby homestay có sân hiên, quầy bar, điều hòa, ban công và Wi-Fi miễn phí.\n\nLullaby homestay cách Chợ Đông Ba 4.8 km và Bảo tàng Museum of Royal Antiquities 4.9 km. Chỗ nghỉ cách Sân bay quốc tế Phú Bài 12 km và cung cấp dịch vụ đưa đón sân bay mất phí.'),
	(5, 'Tiện nghi chung', 'Nơi đây còn có phòng tắm chung với vòi sen ở một số căn, cùng đồ vệ sinh cá nhân miễn phí, máy sấy tóc và dép đi trong phòng.\nHome có cả dịch vụ cho thuê xe đạp và dịch vụ cho thuê ô tô.');

-- Dumping structure for table homestay_management_db.inventory_categories
CREATE TABLE IF NOT EXISTS `inventory_categories` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.inventory_categories: ~5 rows (approximately)
INSERT INTO `inventory_categories` (`category_id`, `category_name`, `description`) VALUES
	(1, 'Đồ dùng cá nhân', 'Các vật tư phục vụ cá nhân khách, thường là đồ tiêu hao.'),
	(2, 'Đồ dùng phòng', 'Các vật tư liên quan đến trang bị trong phòng, có thể tái sử dụng hoặc tiêu hao.'),
	(3, 'Đồ uống và ăn nhẹ', 'Các vật tư phục vụ khách về đồ uống hoặc đồ ăn nhẹ.'),
	(5, 'Đồ trang trí', 'Các vật tư liên quan đến trang trí phòng hoặc khu vực chung, có thể tái sử dụng hoặc thay thế.'),
	(6, 'Đồ dọn dẹp', 'Các vật tư dùng để dọn dẹp phòng, khu vực chung.');

-- Dumping structure for table homestay_management_db.inventory_items
CREATE TABLE IF NOT EXISTS `inventory_items` (
  `item_id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `unit` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `idx_inventory_items_category_id` (`category_id`),
  CONSTRAINT `fk_inventory_items_to_inventory_categories` FOREIGN KEY (`category_id`) REFERENCES `inventory_categories` (`category_id`),
  CONSTRAINT `ck_inventory_items_price` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.inventory_items: ~35 rows (approximately)
INSERT INTO `inventory_items` (`item_id`, `category_id`, `item_name`, `unit`, `price`) VALUES
	(1, 1, 'Dép đi trong phòng', 'đôi', 20000.00),
	(2, 1, 'Sữa tắm', 'chai', 55000.00),
	(3, 1, 'Dầu gội', 'chai', 65000.00),
	(4, 2, 'Gối', 'cái', 50000.00),
	(7, 1, 'Bàn chải đánh răng khách sạn', 'Thùng (100 cái)', 350000.00),
	(8, 1, 'Kem đánh răng P/S mini 20g', 'Thùng (100 tuýp)', 480000.00),
	(9, 1, 'Lược nhựa', 'Thùng (100 cái)', 280000.00),
	(10, 1, 'Bông tẩy trang (80 miếng/gói)', 'Thùng (50 gói)', 800000.00),
	(11, 1, 'Khăn tắm dùng 1 lần', 'Thùng (100 cái)', 700000.00),
	(12, 1, 'Dầu gội khách sạn 30ml', 'Thùng (100 chai)', 450000.00),
	(13, 1, 'Sữa tắm khách sạn 30ml', 'Thùng (100 chai)', 450000.00),
	(14, 2, 'Drap giường cotton 1m6', 'Cái', 230000.00),
	(15, 2, 'Chăn mỏng', 'Cái', 180000.00),
	(16, 2, 'Vỏ gối', 'Túi (10 cái)', 320000.00),
	(17, 2, 'Ly thủy tinh', 'Thùng (24 cái)', 360000.00),
	(18, 2, 'Ấm đun siêu tốc', 'Cái', 220000.00),
	(19, 2, 'Móc treo đồ', 'Túi (50 cái)', 150000.00),
	(20, 3, 'Cà phê G7 3in1 (20 gói/hộp)', 'Thùng (24 hộp)', 1008000.00),
	(21, 3, 'Cà phê sữa Highland 3in1 (16 gói/hộp)', 'Thùng (12 hộp)', 672000.00),
	(22, 3, 'Trà Cozy hương nhài (25 gói/hộp)', 'Thùng (24 hộp)', 888000.00),
	(23, 3, 'Trà Cozy đào cam sả (20 gói/hộp)', 'Thùng (24 hộp)', 864000.00),
	(24, 3, 'Trà Cozy sen vàng (25 gói/hộp)', 'Thùng (24 hộp)', 936000.00),
	(25, 3, 'Cà phê đen hòa tan Nescafe (25 gói/hộp)', 'Thùng (24 hộp)', 1056000.00),
	(26, 3, 'Milo Nestlé gói pha (18 gói/hộp)', 'Thùng (24 hộp)', 1248000.00),
	(27, 3, 'Trà Lipton chanh (25 túi lọc/hộp)', 'Thùng (24 hộp)', 936000.00),
	(28, 5, 'Đèn LED trang trí', 'Cái', 65000.00),
	(29, 5, 'Tượng decor nhỏ', 'Cái', 125000.00),
	(30, 5, 'Khung tranh treo tường', 'Cái', 95000.00),
	(31, 5, 'Chậu cây giả để bàn', 'Cái', 58000.00),
	(32, 6, 'Nước lau sàn Sunlight', 'Thùng (4 can 3.8L)', 272000.00),
	(33, 6, 'Bọt vệ sinh toilet Gift', 'Thùng (12 chai)', 336000.00),
	(34, 6, 'Bàn chải toilet', 'Thùng (20 cái)', 360000.00),
	(35, 6, 'Khăn lau đa năng', 'Thùng (50 cái)', 500000.00),
	(36, 6, 'Găng tay cao su', 'Thùng (50 cặp)', 600000.00),
	(37, 6, 'Túi rác đen loại lớn', 'Thùng (10kg)', 300000.00);

-- Dumping structure for table homestay_management_db.inventory_stocks
CREATE TABLE IF NOT EXISTS `inventory_stocks` (
  `stock_id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`stock_id`),
  KEY `idx_inventory_stocks_branch_id` (`branch_id`),
  KEY `idx_inventory_stocks_item_id` (`item_id`),
  CONSTRAINT `fk_inventory_stocks_to_branches` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`),
  CONSTRAINT `fk_inventory_stocks_to_inventory_items` FOREIGN KEY (`item_id`) REFERENCES `inventory_items` (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.inventory_stocks: ~7 rows (approximately)
INSERT INTO `inventory_stocks` (`stock_id`, `item_id`, `branch_id`, `quantity`) VALUES
	(19, 1, 6, 20),
	(20, 16, 9, 2),
	(21, 18, 6, 5),
	(22, 19, 9, 1),
	(23, 27, 9, 1),
	(24, 32, 6, 1),
	(25, 37, 6, 1);

-- Dumping structure for table homestay_management_db.inventory_transactions
CREATE TABLE IF NOT EXISTS `inventory_transactions` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT,
  `item_id` bigint NOT NULL,
  `branch_id` bigint NOT NULL,
  `employee_id` bigint DEFAULT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `transaction_type` enum('IMPORT','EXPORT') NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `idx_inventory_transactions_branch_id` (`branch_id`),
  KEY `idx_inventory_transactions_employee_id` (`employee_id`),
  KEY `idx_inventory_transactions_item_id` (`item_id`),
  CONSTRAINT `fk_inventory_transactions_to_branches` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`),
  CONSTRAINT `fk_inventory_transactions_to_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  CONSTRAINT `fk_inventory_transactions_to_inventory_items` FOREIGN KEY (`item_id`) REFERENCES `inventory_items` (`item_id`),
  CONSTRAINT `ck_inventory_transactions_quantity` CHECK ((`quantity` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.inventory_transactions: ~7 rows (approximately)
INSERT INTO `inventory_transactions` (`transaction_id`, `item_id`, `branch_id`, `employee_id`, `quantity`, `created_at`, `updated_at`, `transaction_type`) VALUES
	(83, 1, 6, 8, 20, '2025-05-16 20:53:21', NULL, 'IMPORT'),
	(88, 16, 9, 8, 2, '2025-05-16 21:26:36', NULL, 'IMPORT'),
	(89, 18, 6, 8, 5, '2025-05-16 21:27:03', NULL, 'IMPORT'),
	(90, 19, 9, 8, 1, '2025-05-16 21:27:20', NULL, 'IMPORT'),
	(91, 27, 9, 8, 1, '2025-05-16 21:28:03', NULL, 'IMPORT'),
	(92, 32, 6, 8, 1, '2025-05-16 21:28:18', NULL, 'IMPORT'),
	(93, 37, 6, 8, 1, '2025-05-16 21:28:32', NULL, 'IMPORT');

-- Dumping structure for table homestay_management_db.maintenance_requests
CREATE TABLE IF NOT EXISTS `maintenance_requests` (
  `request_id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint DEFAULT NULL,
  `description` varchar(1000) NOT NULL,
  `status` enum('PENDING','IN_PROGRESS','COMPLETED','CANCELLED','ON_HOLD') NOT NULL DEFAULT 'PENDING',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `branch_id` bigint NOT NULL,
  `image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`request_id`),
  KEY `idx_maintenance_requests_branch_id` (`branch_id`),
  KEY `idx_maintenance_requests_employee_id` (`employee_id`),
  KEY `idx_maintenance_requests_room_id` (`room_id`),
  CONSTRAINT `fk_maintenance_requests_to_branches` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`),
  CONSTRAINT `fk_maintenance_requests_to_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  CONSTRAINT `fk_maintenance_requests_to_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.maintenance_requests: ~1 rows (approximately)
INSERT INTO `maintenance_requests` (`request_id`, `room_id`, `description`, `status`, `created_at`, `updated_at`, `employee_id`, `branch_id`, `image`) VALUES
	(4, NULL, 'Đèn sảnh bị mờ', 'COMPLETED', '2025-05-09 09:00:44', '2025-05-09 09:08:07', 8, 6, NULL);

-- Dumping structure for table homestay_management_db.password_reset_tokens
CREATE TABLE IF NOT EXISTS `password_reset_tokens` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` bigint NOT NULL,
  `expiry_date` datetime NOT NULL,
  `is_used` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`token_id`),
  KEY `idx_password_reset_user_id` (`user_id`),
  CONSTRAINT `fk_password_reset_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.password_reset_tokens: ~4 rows (approximately)
INSERT INTO `password_reset_tokens` (`token_id`, `token`, `user_id`, `expiry_date`, `is_used`) VALUES
	(7, '96375cfd-9639-4adc-a4c1-b97ecb48f215', 6, '2025-05-15 16:59:51', 1),
	(10, 'eb432304-1192-4675-8f49-097524431536', 3, '2025-05-15 21:52:34', 1),
	(11, 'db073e1f-aebc-40b1-af9b-a9ac7c491081', 4, '2025-05-15 21:58:02', 1),
	(12, '1537497f-9236-4aaa-b18f-f031c48460ae', 7, '2025-05-24 13:13:42', 1);

-- Dumping structure for table homestay_management_db.payments
CREATE TABLE IF NOT EXISTS `payments` (
  `payment_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `payment_type` enum('CASH','TRANSFER') NOT NULL,
  `payment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','COMPLETED','FAILED','REFUNDED','PENDING_REFUND') NOT NULL DEFAULT 'PENDING',
  `vnp_transaction_no` varchar(50) DEFAULT NULL,
  `vnp_txn_ref` varchar(50) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `idx_payments_booking_id` (`booking_id`),
  CONSTRAINT `fk_payments_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `ck_payments_total_amount` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.payments: ~18 rows (approximately)
INSERT INTO `payments` (`payment_id`, `booking_id`, `payment_type`, `payment_date`, `status`, `vnp_transaction_no`, `vnp_txn_ref`, `total_amount`) VALUES
	(42, 137, 'TRANSFER', '2025-05-13 17:33:15', 'COMPLETED', '14954409', '06001591', 470000.00),
	(43, 137, 'TRANSFER', '2025-05-13 22:24:39', 'COMPLETED', '14954884', '04090555', 50000.00),
	(44, 139, 'TRANSFER', '2025-05-14 14:41:03', 'COMPLETED', NULL, NULL, 545000.00),
	(45, 140, 'TRANSFER', '2025-05-15 08:58:29', 'COMPLETED', '14957802', '31160533', 570000.00),
	(46, 141, 'CASH', '2025-05-15 21:56:14', 'COMPLETED', NULL, NULL, 540000.00),
	(47, 153, 'TRANSFER', '2025-05-16 12:53:56', 'COMPLETED', '14960486', '11901894', 370000.00),
	(48, 153, 'TRANSFER', '2025-05-16 13:07:03', 'COMPLETED', '14960503', '55786166', 50000.00),
	(49, 137, 'TRANSFER', '2025-05-16 17:01:04', 'COMPLETED', NULL, NULL, 30000.00),
	(50, 154, 'TRANSFER', '2025-05-16 17:08:14', 'COMPLETED', NULL, NULL, 300000.00),
	(51, 155, 'TRANSFER', '2025-05-23 09:07:41', 'COMPLETED', NULL, NULL, 220000.00),
	(52, 156, 'TRANSFER', '2025-05-24 12:01:32', 'COMPLETED', '14976572', '46394885', 220000.00),
	(53, 157, 'TRANSFER', '2025-05-25 16:26:34', 'COMPLETED', '14978154', '94535730', 450000.00),
	(54, 157, 'TRANSFER', '2025-05-25 16:27:32', 'COMPLETED', '14978158', '74751671', 60000.00),
	(55, 157, 'TRANSFER', '2025-05-25 18:02:39', 'COMPLETED', '14978304', '18344588', 85000.00),
	(56, 164, 'CASH', '2025-05-25 19:54:56', 'COMPLETED', NULL, NULL, 450000.00),
	(57, 166, 'TRANSFER', '2025-05-31 12:53:32', 'COMPLETED', NULL, NULL, 620000.00),
	(58, 166, 'TRANSFER', '2025-05-31 12:54:07', 'COMPLETED', NULL, NULL, 60000.00),
	(59, 167, 'TRANSFER', '2025-05-31 18:47:43', 'COMPLETED', '14991662', '21640163', 1190000.00);

-- Dumping structure for table homestay_management_db.payment_details
CREATE TABLE IF NOT EXISTS `payment_details` (
  `payment_detail_id` bigint NOT NULL AUTO_INCREMENT,
  `payment_id` bigint NOT NULL,
  `booking_service_id` bigint DEFAULT NULL,
  `extension_id` bigint DEFAULT NULL,
  `payment_purpose` enum('ROOM_BOOKING','PREPAID_SERVICE','ADDITIONAL_SERVICE','EXTENDED_HOURS') NOT NULL,
  `base_amount` decimal(10,2) NOT NULL,
  `final_amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`payment_detail_id`),
  KEY `idx_payment_details_extension_id` (`extension_id`),
  KEY `idx_payment_details_booking_service_id` (`booking_service_id`),
  KEY `idx_payment_details_payment_id` (`payment_id`),
  CONSTRAINT `fk_payment_details_to_booking_extensions` FOREIGN KEY (`extension_id`) REFERENCES `booking_extensions` (`extension_id`),
  CONSTRAINT `fk_payment_details_to_booking_services` FOREIGN KEY (`booking_service_id`) REFERENCES `booking_services` (`booking_service_id`),
  CONSTRAINT `fk_payment_details_to_payments` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`),
  CONSTRAINT `ck_payment_details_base_amount` CHECK ((`base_amount` >= 0)),
  CONSTRAINT `ck_payment_details_final_amount` CHECK ((`final_amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.payment_details: ~26 rows (approximately)
INSERT INTO `payment_details` (`payment_detail_id`, `payment_id`, `booking_service_id`, `extension_id`, `payment_purpose`, `base_amount`, `final_amount`) VALUES
	(56, 42, NULL, NULL, 'ROOM_BOOKING', 370000.00, 370000.00),
	(57, 42, 75, NULL, 'PREPAID_SERVICE', 100000.00, 100000.00),
	(58, 43, NULL, 15, 'EXTENDED_HOURS', 50000.00, 50000.00),
	(59, 44, NULL, NULL, 'ROOM_BOOKING', 395000.00, 395000.00),
	(60, 44, 79, NULL, 'PREPAID_SERVICE', 150000.00, 150000.00),
	(61, 45, NULL, NULL, 'ROOM_BOOKING', 570000.00, 570000.00),
	(62, 46, NULL, NULL, 'ROOM_BOOKING', 440000.00, 440000.00),
	(63, 46, 80, NULL, 'PREPAID_SERVICE', 100000.00, 100000.00),
	(64, 47, NULL, NULL, 'ROOM_BOOKING', 220000.00, 220000.00),
	(65, 47, 98, NULL, 'PREPAID_SERVICE', 150000.00, 150000.00),
	(66, 48, NULL, 16, 'EXTENDED_HOURS', 50000.00, 50000.00),
	(67, 49, 77, NULL, 'ADDITIONAL_SERVICE', 30000.00, 30000.00),
	(68, 50, NULL, NULL, 'ROOM_BOOKING', 300000.00, 300000.00),
	(69, 51, NULL, NULL, 'ROOM_BOOKING', 220000.00, 220000.00),
	(70, 52, NULL, NULL, 'ROOM_BOOKING', 220000.00, 220000.00),
	(71, 53, NULL, NULL, 'ROOM_BOOKING', 300000.00, 300000.00),
	(72, 53, 99, NULL, 'PREPAID_SERVICE', 150000.00, 150000.00),
	(73, 54, NULL, 17, 'EXTENDED_HOURS', 60000.00, 60000.00),
	(74, 55, 100, NULL, 'ADDITIONAL_SERVICE', 60000.00, 60000.00),
	(75, 55, 102, NULL, 'ADDITIONAL_SERVICE', 25000.00, 25000.00),
	(76, 56, NULL, NULL, 'ROOM_BOOKING', 300000.00, 300000.00),
	(77, 56, 104, NULL, 'PREPAID_SERVICE', 150000.00, 150000.00),
	(78, 57, NULL, NULL, 'ROOM_BOOKING', 520000.00, 520000.00),
	(79, 57, 107, NULL, 'PREPAID_SERVICE', 100000.00, 100000.00),
	(80, 58, NULL, 18, 'EXTENDED_HOURS', 60000.00, 60000.00),
	(81, 59, NULL, NULL, 'ROOM_BOOKING', 1040000.00, 1040000.00),
	(82, 59, 110, NULL, 'PREPAID_SERVICE', 150000.00, 150000.00);

-- Dumping structure for table homestay_management_db.refunds
CREATE TABLE IF NOT EXISTS `refunds` (
  `refund_id` bigint NOT NULL AUTO_INCREMENT,
  `payment_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `refund_type` enum('FULL','PARTIAL_70','PARTIAL_30') NOT NULL,
  `refund_amount` decimal(10,2) NOT NULL,
  `status` enum('PENDING','REFUNDED','PENDING_REFUND') NOT NULL,
  `vnp_transaction_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`refund_id`),
  KEY `idx_refunds_payment_id` (`payment_id`),
  CONSTRAINT `fk_refunds_to_payments` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`payment_id`),
  CONSTRAINT `ck_refunds_refund_amount` CHECK ((`refund_amount` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.refunds: ~0 rows (approximately)

-- Dumping structure for table homestay_management_db.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `review_id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  KEY `idx_reviews_booking_id` (`booking_id`),
  CONSTRAINT `fk_reviews_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`),
  CONSTRAINT `ck_reviews_rating_1` CHECK ((`rating` >= 1)),
  CONSTRAINT `ck_reviews_rating_2` CHECK ((`rating` <= 5))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.reviews: ~3 rows (approximately)
INSERT INTO `reviews` (`review_id`, `booking_id`, `rating`, `comment`, `created_at`, `image`) VALUES
	(1, 153, 5, 'Homestay sạch đẹp, dễ thương. Phòng rộng rãi, đầy đủ tiện nghi. Có máy chiếu tích hợp Netflix.\nGiường gối êm ái. Nhận và trả phòng nhanh chóng, dễ dàng. Chủ home vui vẻ, hỗ trợ.\nHomestay có vị trí tốt, rất gần Aeon Mall. Khu vực an ninh, quy hoạch đồng bộ.\nCó thể đậu xe oto quanh các đường nội khu.', '2025-05-29 21:02:14', '1748527333655-493410406.jpg'),
	(2, 154, 5, 'Phòng rất đẹp, có ban công và máy chiếu, cùng nhiều tiện ích khác, giá cả hợp lý, anh chị chủ rất nhiệt tình, mến khách, anh chị hỗ trợ rất nhiều trong lúc ở homestay, 10/10 tuyệt vời <3', '2025-05-31 18:54:02', NULL),
	(3, 137, 5, 'Home nhỏ xinh nằm ở khu mới nên hơi vắng nhưng cũng gần trung tâm lắm nha. Cách các địa điểm du lịch tầm 10p chạy xe máy thôi. Phòng mới decor xinh, sạch sẽ, nhà mình có bé tối mở máy chiếu xem cũng chill lắm. Chị chủ siêu siêu nhiệt tình luôn nha. Hỗ trợ hết mình lun ý. Highly recomment nha❤️', '2025-05-31 18:54:53', NULL),
	(4, 164, 5, 'Siêu siêu thích. Anh chủ cực kỳ nhiệt tình, luôn hỗ trợ hết mình cho chúng mình. 100% rcm các bạn ở', '2025-05-31 18:55:36', NULL);

-- Dumping structure for table homestay_management_db.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.roles: ~4 rows (approximately)
INSERT INTO `roles` (`role_id`, `role_name`, `description`) VALUES
	(3, 'MANAGER', 'Quản lý'),
	(4, 'HOUSEKEEPER', 'Nhân viên dọn dẹp'),
	(5, 'EMPLOYEE', 'Nhân viên'),
	(6, 'CUSTOMER', 'Khách hàng');

-- Dumping structure for table homestay_management_db.rooms
CREATE TABLE IF NOT EXISTS `rooms` (
  `room_id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `room_type_id` bigint NOT NULL,
  `room_number` int NOT NULL,
  `area` float DEFAULT NULL,
  `thumbnail` varchar(500) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`room_id`),
  KEY `idx_rooms_room_type_id` (`room_type_id`),
  KEY `idx_rooms_branch_id` (`branch_id`),
  CONSTRAINT `fk_rooms_to_branches` FOREIGN KEY (`branch_id`) REFERENCES `branches` (`branch_id`),
  CONSTRAINT `fk_rooms_to_room_types` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`room_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.rooms: ~8 rows (approximately)
INSERT INTO `rooms` (`room_id`, `branch_id`, `room_type_id`, `room_number`, `area`, `thumbnail`, `is_active`) VALUES
	(1, 6, 1, 1, 60, '1740414129615-536466775.jpg', 1),
	(2, 9, 2, 2, 50, '1741704362352-503296952.jpg', 1),
	(3, 9, 1, 6, 100, '1741704379787-536466803.jpg', 1),
	(4, 6, 3, 8, 120, '1741704453138-536467924.jpg', 1),
	(5, 6, 1, 5, 60, NULL, 1),
	(6, 9, 3, 9, 120, NULL, 1),
	(7, 6, 2, 10, 50, NULL, 1),
	(8, 9, 1, 12, 0, NULL, 1);

-- Dumping structure for table homestay_management_db.room_amenities
CREATE TABLE IF NOT EXISTS `room_amenities` (
  `room_id` bigint NOT NULL,
  `amenity_id` bigint NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`room_id`,`amenity_id`),
  KEY `FK_RoomAmenities_Amenities` (`amenity_id`),
  KEY `idx_room_amenities_amenity_id` (`amenity_id`),
  KEY `idx_room_amenities_room_id` (`room_id`),
  CONSTRAINT `fk_room_amenities_to_amenities` FOREIGN KEY (`amenity_id`) REFERENCES `amenities` (`amenity_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_amenities_to_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_RoomAmenities_Amenities` FOREIGN KEY (`amenity_id`) REFERENCES `amenities` (`amenity_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_RoomAmenities_Rooms1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_room_amenities_quantity` CHECK ((`quantity` >= 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.room_amenities: ~43 rows (approximately)
INSERT INTO `room_amenities` (`room_id`, `amenity_id`, `quantity`) VALUES
	(1, 1, NULL),
	(1, 3, NULL),
	(1, 4, 1),
	(1, 5, 1),
	(1, 7, 1),
	(1, 13, NULL),
	(1, 14, NULL),
	(1, 19, NULL),
	(1, 23, NULL),
	(1, 24, NULL),
	(1, 25, NULL),
	(1, 26, NULL),
	(2, 1, NULL),
	(2, 3, NULL),
	(2, 4, NULL),
	(2, 5, 1),
	(2, 7, 1),
	(2, 10, NULL),
	(2, 13, NULL),
	(2, 14, NULL),
	(2, 19, NULL),
	(2, 23, NULL),
	(2, 25, NULL),
	(2, 26, NULL),
	(3, 1, NULL),
	(3, 3, NULL),
	(3, 4, NULL),
	(3, 5, NULL),
	(3, 7, 1),
	(3, 10, NULL),
	(3, 13, NULL),
	(3, 14, NULL),
	(3, 19, NULL),
	(3, 23, NULL),
	(3, 24, NULL),
	(3, 25, NULL),
	(3, 26, NULL),
	(4, 1, NULL),
	(4, 3, 1),
	(4, 4, NULL),
	(4, 5, NULL),
	(4, 19, NULL),
	(4, 26, NULL);

-- Dumping structure for table homestay_management_db.room_photos
CREATE TABLE IF NOT EXISTS `room_photos` (
  `photo_id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `photo` varchar(500) NOT NULL,
  `is_hidden` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`photo_id`),
  KEY `idx_room_photos_room_id` (`room_id`),
  CONSTRAINT `fk_room_photos_to_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.room_photos: ~2 rows (approximately)
INSERT INTO `room_photos` (`photo_id`, `room_id`, `photo`, `is_hidden`) VALUES
	(11, 4, '1741706392796-536467920.jpg', 0),
	(12, 4, '1741706401666-536467937.jpg', 0);

-- Dumping structure for table homestay_management_db.room_pricings
CREATE TABLE IF NOT EXISTS `room_pricings` (
  `room_pricing_id` bigint NOT NULL AUTO_INCREMENT,
  `room_type_id` bigint NOT NULL,
  `base_duration` int NOT NULL,
  `base_price` decimal(10,2) NOT NULL,
  `extra_hour_price` decimal(10,2) NOT NULL,
  `overnight_price` decimal(10,2) NOT NULL,
  `daily_price` decimal(10,2) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `policy` varchar(255) DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`room_pricing_id`),
  KEY `idx_room_pricings_room_type_id` (`room_type_id`),
  CONSTRAINT `fk_room_pricings_to_room_types` FOREIGN KEY (`room_type_id`) REFERENCES `room_types` (`room_type_id`) ON DELETE CASCADE,
  CONSTRAINT `ck_room_pricings_base_duration` CHECK ((`base_duration` >= 1)),
  CONSTRAINT `ck_room_pricings_base_price` CHECK ((`base_price` > 0)),
  CONSTRAINT `ck_room_pricings_daily_price` CHECK ((`daily_price` >= 0)),
  CONSTRAINT `ck_room_pricings_extra_hour_price` CHECK ((`extra_hour_price` >= 0)),
  CONSTRAINT `ck_room_pricings_overnight_price` CHECK ((`overnight_price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.room_pricings: ~3 rows (approximately)
INSERT INTO `room_pricings` (`room_pricing_id`, `room_type_id`, `base_duration`, `base_price`, `extra_hour_price`, `overnight_price`, `daily_price`, `start_date`, `end_date`, `policy`, `is_default`) VALUES
	(2, 2, 3, 220000.00, 50000.00, 320000.00, 450000.00, NULL, NULL, NULL, 1),
	(5, 3, 3, 150000.00, 40000.00, 260000.00, 320000.00, NULL, NULL, NULL, 1),
	(7, 1, 3, 300000.00, 60000.00, 380000.00, 520000.00, NULL, NULL, '', 1);

-- Dumping structure for table homestay_management_db.room_status_histories
CREATE TABLE IF NOT EXISTS `room_status_histories` (
  `room_status_id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `status` enum('BUSY','CLEANING') NOT NULL,
  `started_at` datetime NOT NULL,
  `ended_at` datetime NOT NULL,
  `booking_id` bigint DEFAULT NULL,
  PRIMARY KEY (`room_status_id`),
  KEY `idx_room_status_histories_booking_id` (`booking_id`),
  KEY `idx_room_status_histories_room_id` (`room_id`),
  CONSTRAINT `fk_room_status_histories_to_bookings` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`booking_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_room_status_histories_to_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.room_status_histories: ~22 rows (approximately)
INSERT INTO `room_status_histories` (`room_status_id`, `room_id`, `status`, `started_at`, `ended_at`, `booking_id`) VALUES
	(271, 2, 'BUSY', '2025-05-13 21:00:00', '2025-05-14 09:00:00', 137),
	(272, 2, 'CLEANING', '2025-05-14 09:00:00', '2025-05-14 10:00:00', 137),
	(275, 2, 'BUSY', '2025-05-14 23:30:00', '2025-05-15 11:00:00', 139),
	(276, 2, 'CLEANING', '2025-05-15 11:00:00', '2025-05-15 11:30:00', 139),
	(277, 4, 'BUSY', '2025-05-15 09:30:00', '2025-05-15 13:30:00', 140),
	(278, 4, 'CLEANING', '2025-05-15 13:30:00', '2025-05-15 14:30:00', 140),
	(303, 2, 'BUSY', '2025-05-16 16:00:00', '2025-05-16 20:00:00', 153),
	(304, 2, 'CLEANING', '2025-05-16 20:00:00', '2025-05-16 21:00:00', 153),
	(305, 5, 'BUSY', '2025-05-16 18:00:00', '2025-05-16 21:00:00', 154),
	(306, 5, 'CLEANING', '2025-05-16 21:00:00', '2025-05-16 22:00:00', 154),
	(307, 7, 'BUSY', '2025-05-23 13:00:00', '2025-05-23 16:00:00', 155),
	(308, 7, 'CLEANING', '2025-05-23 16:00:00', '2025-05-23 17:00:00', 155),
	(309, 7, 'BUSY', '2025-05-24 13:00:00', '2025-05-24 16:00:00', 156),
	(310, 7, 'CLEANING', '2025-05-24 16:00:00', '2025-05-24 17:00:00', 156),
	(311, 3, 'BUSY', '2025-05-25 17:00:00', '2025-05-25 21:00:00', 157),
	(312, 3, 'CLEANING', '2025-05-25 21:00:00', '2025-05-25 22:00:00', 157),
	(325, 8, 'BUSY', '2025-05-26 12:00:00', '2025-05-26 15:00:00', 164),
	(326, 8, 'CLEANING', '2025-05-26 15:00:00', '2025-05-26 16:00:00', 164),
	(329, 5, 'BUSY', '2025-05-31 16:00:00', '2025-06-01 17:00:00', 166),
	(330, 5, 'CLEANING', '2025-06-01 17:00:00', '2025-06-01 18:00:00', 166),
	(331, 4, 'BUSY', '2025-05-31 22:00:00', '2025-06-01 08:00:00', 167),
	(332, 4, 'CLEANING', '2025-06-01 08:00:00', '2025-06-01 09:00:00', 167);

-- Dumping structure for table homestay_management_db.room_types
CREATE TABLE IF NOT EXISTS `room_types` (
  `room_type_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `max_guest` int DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `photo` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`room_type_id`),
  CONSTRAINT `ck_room_types_max_guest` CHECK ((`max_guest` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.room_types: ~3 rows (approximately)
INSERT INTO `room_types` (`room_type_id`, `name`, `max_guest`, `description`, `photo`) VALUES
	(1, 'Phòng Giường Đôi', 3, 'Phòng được thiết kế theo phong cách hiện đại, kết hợp hài hòa giữa tông màu nhẹ nhàng và ánh sáng tự nhiên, tạo cảm giác thư thái và dễ chịu. Đây là lựa chọn lý tưởng cho các cặp đôi hoặc những du khách muốn tận hưởng không gian riêng tư và thoải mái trong kỳ nghỉ.', '1741612491542-536466760.jpg'),
	(2, 'Phòng Giường Đơn', 1, 'Phòng Đơn tại homestay của chúng tôi là lựa chọn hoàn hảo cho khách du lịch hoặc người đi công tác muốn tìm kiếm không gian riêng tư và tiện nghi. Phòng được thiết kế gọn gàng, hiện đại, phù hợp cho 1 người với đầy đủ các tiện ích cần thiết', '1741611940150-493181629.jpg'),
	(3, 'Phòng Dorm', 6, 'Phòng Dorm tại homestay của chúng tôi là lựa chọn lý tưởng cho những ai thích kết nối, giao lưu và muốn tiết kiệm chi phí khi đi du lịch. Phòng được thiết kế theo phong cách trẻ trung, hiện đại, tạo không gian thoải mái và tiện nghi cho nhiều khách cùng lúc', '1741611970880-536467920.jpg');

-- Dumping structure for table homestay_management_db.rules
CREATE TABLE IF NOT EXISTS `rules` (
  `rule_id` bigint NOT NULL AUTO_INCREMENT,
  `rule_title` varchar(255) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `is_hidden` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.rules: ~11 rows (approximately)
INSERT INTO `rules` (`rule_id`, `rule_title`, `description`, `icon`, `is_hidden`) VALUES
	(1, 'Nhận phòng', '', 'fa-sign-in', 1),
	(2, 'Trả phòng', '', 'fa-sign-out', 1),
	(3, 'Độ tuổi', 'Không giới hạn độ tuổi', 'fa-users', 0),
	(4, 'Hủy đặt phòng/Trả trước', NULL, 'fa-info-circle', 1),
	(6, 'Trẻ em và giường', 'Phù hợp cho tất cả trẻ em', 'fa-bed', 0),
	(7, 'Hút thuốc', '', 'fa-smoking-ban', 1),
	(8, 'Tiệc tùng', 'Không cho phép tiệc tùng/sự kiện', 'fa-glass-cheers', 0),
	(10, 'Nấu ăn', 'Có không gian bếp chung', 'fa-coffee', 0),
	(11, 'Thú cưng', 'Vật nuôi không được phép', 'fa-paw', 0),
	(12, 'Phụ thu', NULL, 'fa-plus-square', 1),
	(13, 'Thanh toán', NULL, 'fa-credit-card', 1);

-- Dumping structure for table homestay_management_db.services
CREATE TABLE IF NOT EXISTS `services` (
  `service_id` bigint NOT NULL AUTO_INCREMENT,
  `service_name` varchar(255) NOT NULL,
  `unit` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `is_prepaid` tinyint(1) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`service_id`),
  CONSTRAINT `ck_services_price` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.services: ~5 rows (approximately)
INSERT INTO `services` (`service_id`, `service_name`, `unit`, `price`, `is_prepaid`, `description`, `icon`) VALUES
	(2, 'Dịch vụ là (ủi)', 'món', 5000.00, 0, '', 'mdi:tshirt-crew-outline'),
	(3, 'Dịch vụ đưa đón', 'km', 18000.00, 0, '', 'mdi:car'),
	(5, 'Giặt ủi', 'kg', 20000.00, 0, '', 'mdi:iron-outline'),
	(6, 'Cho thuê xe máy', 'ngày', 100000.00, 1, '', 'mdi:motorbike'),
	(7, 'Dịch vụ đưa đón sân bay', 'lần', 150000.00, 1, '', 'mdi:car-hatchback');

-- Dumping structure for table homestay_management_db.spring_session
CREATE TABLE IF NOT EXISTS `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.spring_session: ~7 rows (approximately)
INSERT INTO `spring_session` (`PRIMARY_ID`, `SESSION_ID`, `CREATION_TIME`, `LAST_ACCESS_TIME`, `MAX_INACTIVE_INTERVAL`, `EXPIRY_TIME`, `PRINCIPAL_NAME`) VALUES
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'bdaa6988-b467-4909-96a3-c2ba67c56c17', 1748581519147, 1748581525584, 2592000, 1751173525584, 'nhuytran12223@gmail.com'),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'd36fbc05-e1ea-4ed2-8650-d3a5583004f0', 1748692498260, 1748692948246, 2592000, 1751284948246, 'nhuydengxin@gmail.com'),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'd235a2ca-f927-4f70-95eb-af1a974f6298', 1747883452310, 1748619086752, 2592000, 1751211086752, '21t1020105@husc.edu.vn'),
	('85757517-f8e7-4664-a676-f726559fb0aa', '906fbe10-db9c-453e-b77e-46ae9791f05e', 1748163093237, 1748173983764, 2592000, 1750765983764, 'nhuydengxin@gmail.com'),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', '87ba1e53-af73-41a2-a29e-f8cc7cb15108', 1748484306066, 1748692052519, 2592000, 1751284052519, 'nhuytran12223@gmail.com'),
	('b749da08-81bf-4518-9cfa-aab91a48b1f5', '322deb5e-eb59-4770-9f14-9f7149d8e10b', 1747964753538, 1747973556145, 2592000, 1750565556145, 'quocanh@gmail.com'),
	('fb0b71bc-8ec9-4603-a423-7a9d4e65e106', 'd79f02d6-0cec-442f-818e-30f6821cc7ff', 1746282754450, 1746330837849, 2592000, 1748922837849, 'nhuytran@gmail.com');

-- Dumping structure for table homestay_management_db.spring_session_attributes
CREATE TABLE IF NOT EXISTS `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` longblob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.spring_session_attributes: ~34 rows (approximately)
INSERT INTO `spring_session_attributes` (`SESSION_PRIMARY_ID`, `ATTRIBUTE_NAME`, `ATTRIBUTE_BYTES`) VALUES
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'avatar', _binary 0xaced0005740000),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'email', _binary 0xaced00057400176e6875797472616e313232323340676d61696c2e636f6d),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'fullName', _binary 0xaced000574000e5472e1baa76e204e68c6b020c39d),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'id', _binary 0xaced00057372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000003),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'jakarta.servlet.jsp.jstl.fmt.request.charset', _binary 0xaced00057400055554462d38),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', _binary 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002430313234646661352d326162662d346538352d393539612d376631393232626139666234),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'role', _binary 0xaced00057400074d414e41474552),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'SPRING_SECURITY_CONTEXT', _binary 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c000000000000026c0200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e000000000000026c0200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f72697479000000000000026c0200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b787074000c524f4c455f4d414e414745527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c73000000000000026c0200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002466393439616639322d633037622d343134352d626437302d39663636343238333430636570737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e55736572000000000000026c0200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f72000000000000026c020000787077040000000171007e001078707400176e6875797472616e313232323340676d61696c2e636f6d),
	('0782e2d8-54b0-4e5c-9022-3ed6e6d64c55', 'SPRING_SECURITY_SAVED_REQUEST', _binary 0xaced0005737200416f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e44656661756c74536176656452657175657374000000000000026c02000f49000a736572766572506f72744c000b636f6e74657874506174687400124c6a6176612f6c616e672f537472696e673b4c0007636f6f6b6965737400154c6a6176612f7574696c2f41727261794c6973743b4c00076865616465727374000f4c6a6176612f7574696c2f4d61703b4c00076c6f63616c657371007e00024c001c6d61746368696e6752657175657374506172616d657465724e616d6571007e00014c00066d6574686f6471007e00014c000a706172616d657465727371007e00034c000870617468496e666f71007e00014c000b7175657279537472696e6771007e00014c000a7265717565737455524971007e00014c000a7265717565737455524c71007e00014c0006736368656d6571007e00014c000a7365727665724e616d6571007e00014c000b736572766c65745061746871007e0001787000001f90740000737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a6578700000000077040000000078737200116a6176612e7574696c2e547265654d61700cc1f63e2d256ae60300014c000a636f6d70617261746f727400164c6a6176612f7574696c2f436f6d70617261746f723b78707372002a6a6176612e6c616e672e537472696e672443617365496e73656e736974697665436f6d70617261746f7277035c7d5c50e5ce020000787077040000000e7400066163636570747371007e000600000001770400000001740087746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f617669662c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e377874000f6163636570742d656e636f64696e677371007e000600000001770400000001740017677a69702c206465666c6174652c2062722c207a7374647874000f6163636570742d6c616e67756167657371007e00060000000177040000000174000276697874000a636f6e6e656374696f6e7371007e00060000000177040000000174000a6b6565702d616c69766578740004686f73747371007e00060000000177040000000174000e6c6f63616c686f73743a38303830787400097365632d63682d75617371007e000600000001770400000001740041224368726f6d69756d223b763d22313336222c2022476f6f676c65204368726f6d65223b763d22313336222c20224e6f742e412f4272616e64223b763d22393922787400107365632d63682d75612d6d6f62696c657371007e0006000000017704000000017400023f30787400127365632d63682d75612d706c6174666f726d7371007e0006000000017704000000017400092257696e646f7773227874000e7365632d66657463682d646573747371007e000600000001770400000001740008646f63756d656e747874000e7365632d66657463682d6d6f64657371007e0006000000017704000000017400086e617669676174657874000e7365632d66657463682d736974657371007e0006000000017704000000017400046e6f6e657874000e7365632d66657463682d757365727371007e0006000000017704000000017400023f3178740019757067726164652d696e7365637572652d72657175657374737371007e000600000001770400000001740001317874000a757365722d6167656e747371007e00060000000177040000000174006f4d6f7a696c6c612f352e30202857696e646f7773204e542031302e303b2057696e36343b2078363429204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f3133362e302e302e30205361666172692f3533372e333678787371007e000600000001770400000001737200106a6176612e7574696c2e4c6f63616c657ef811609c30f9ec03000649000868617368636f64654c0007636f756e74727971007e00014c000a657874656e73696f6e7371007e00014c00086c616e677561676571007e00014c000673637269707471007e00014c000776617269616e7471007e00017870ffffffff71007e000571007e0005740002766971007e000571007e00057878740008636f6e74696e75657400034745547371007e0008707704000000007870707400102f61646d696e2f64617368626f617264740025687474703a2f2f6c6f63616c686f73743a383038302f61646d696e2f64617368626f617264740004687474707400096c6f63616c686f73747400102f61646d696e2f64617368626f617264),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'email', _binary 0xaced00057400156e68757964656e6778696e40676d61696c2e636f6d),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'fullName', _binary 0xaced0005740010486fc3a06e67204b68c3a16e6820416e),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'id', _binary 0xaced00057372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000012),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'jakarta.servlet.jsp.jstl.fmt.request.charset', _binary 0xaced00057400055554462d38),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', _binary 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002437373631386334362d303834352d343765362d623837362d663737613266303935303033),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'role', _binary 0xaced0005740008435553544f4d4552),
	('3a67c4b9-4940-4608-b2d3-71955c33c219', 'SPRING_SECURITY_CONTEXT', _binary 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c000000000000026c0200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e000000000000026c0200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f72697479000000000000026c0200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b787074000d524f4c455f435553544f4d45527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c73000000000000026c0200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002430303931353336342d613266362d343935332d626662612d39333632333630393161376470737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e55736572000000000000026c0200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f72000000000000026c020000787077040000000171007e001078707400156e68757964656e6778696e40676d61696c2e636f6d),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'avatar', _binary 0xaced0005740041313734363935333034393837332d7a353738393130313537343837315f35393339393965326236323336386662633065626161323565323061373033312e6a7067),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'email', _binary 0xaced00057400163231743130323031303540687573632e6564752e766e),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'fullName', _binary 0xaced000574000e4e68c6b020c39d205472e1baa76e),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'id', _binary 0xaced00057372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000005),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'jakarta.servlet.jsp.jstl.fmt.request.charset', _binary 0xaced00057400055554462d38),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', _binary 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002461316239373937382d373939622d343665382d393530322d366330353865373432346563),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'role', _binary 0xaced0005740008435553544f4d4552),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'SPRING_SECURITY_CONTEXT', _binary 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c000000000000026c0200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e000000000000026c0200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f72697479000000000000026c0200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b787074000d524f4c455f435553544f4d45527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c73000000000000026c0200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002430626432373366612d636336622d343362612d613835622d30306435393566396266663070737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e55736572000000000000026c0200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f72000000000000026c020000787077040000000171007e001078707400163231743130323031303540687573632e6564752e766e),
	('803394c9-fbbe-4291-81f7-608e77a93514', 'SPRING_SECURITY_SAVED_REQUEST', _binary 0xaced0005737200416f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e44656661756c74536176656452657175657374000000000000026c02000f49000a736572766572506f72744c000b636f6e74657874506174687400124c6a6176612f6c616e672f537472696e673b4c0007636f6f6b6965737400154c6a6176612f7574696c2f41727261794c6973743b4c00076865616465727374000f4c6a6176612f7574696c2f4d61703b4c00076c6f63616c657371007e00024c001c6d61746368696e6752657175657374506172616d657465724e616d6571007e00014c00066d6574686f6471007e00014c000a706172616d657465727371007e00034c000870617468496e666f71007e00014c000b7175657279537472696e6771007e00014c000a7265717565737455524971007e00014c000a7265717565737455524c71007e00014c0006736368656d6571007e00014c000a7365727665724e616d6571007e00014c000b736572766c65745061746871007e0001787000001f90740000737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200396f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e5361766564436f6f6b6965000000000000026c0200084900066d61784167655a000673656375726549000776657273696f6e4c0007636f6d6d656e7471007e00014c0006646f6d61696e71007e00014c00046e616d6571007e00014c00047061746871007e00014c000576616c756571007e00017870ffffffff0000000000707074000753455353494f4e707400304d474a6b4d6a637a5a6d457459324d32596930304d324a684c5745344e5749744d44426b4e546b315a6a6c695a6d597778737200116a6176612e7574696c2e547265654d61700cc1f63e2d256ae60300014c000a636f6d70617261746f727400164c6a6176612f7574696c2f436f6d70617261746f723b78707372002a6a6176612e6c616e672e537472696e672443617365496e73656e736974697665436f6d70617261746f7277035c7d5c50e5ce020000787077040000000d74000f6163636570742d656e636f64696e677371007e000600000001770400000001740017677a69702c206465666c6174652c2062722c207a7374647874000f6163636570742d6c616e67756167657371007e000600000001770400000001740023656e2d55532c656e3b713d302e392c76693b713d302e382c76692d564e3b713d302e377874000d63616368652d636f6e74726f6c7371007e0006000000017704000000017400086e6f2d63616368657874000a636f6e6e656374696f6e7371007e0006000000017704000000017400075570677261646578740006636f6f6b69657371007e00060000000177040000000174003853455353494f4e3d4d474a6b4d6a637a5a6d457459324d32596930304d324a684c5745344e5749744d44426b4e546b315a6a6c695a6d597778740004686f73747371007e00060000000177040000000174000e6c6f63616c686f73743a38303830787400066f726967696e7371007e000600000001770400000001740015687474703a2f2f6c6f63616c686f73743a3830383078740006707261676d617371007e0006000000017704000000017400086e6f2d6361636865787400187365632d776562736f636b65742d657874656e73696f6e737371007e00060000000177040000000174002a7065726d6573736167652d6465666c6174653b20636c69656e745f6d61785f77696e646f775f62697473787400117365632d776562736f636b65742d6b65797371007e00060000000177040000000174001849514964454e3959426d74626e564c516c775a744d513d3d787400157365632d776562736f636b65742d76657273696f6e7371007e000600000001770400000001740002313378740007757067726164657371007e000600000001770400000001740009776562736f636b65747874000a757365722d6167656e747371007e00060000000177040000000174006f4d6f7a696c6c612f352e30202857696e646f7773204e542031302e303b2057696e36343b2078363429204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f3133362e302e302e30205361666172692f3533372e333678787371007e000600000004770400000004737200106a6176612e7574696c2e4c6f63616c657ef811609c30f9ec03000649000868617368636f64654c0007636f756e74727971007e00014c000a657874656e73696f6e7371007e00014c00086c616e677561676571007e00014c000673637269707471007e00014c000776617269616e7471007e00017870ffffffff740002555371007e0005740002656e71007e000571007e0005787371007e0039ffffffff71007e000571007e000571007e003c71007e000571007e0005787371007e0039ffffffff71007e000571007e0005740002766971007e000571007e0005787371007e0039ffffffff740002564e71007e000571007e003f71007e000571007e00057878740008636f6e74696e75657400034745547371007e000c707704000000007870707400062f77732f777374001b687474703a2f2f6c6f63616c686f73743a383038302f77732f7773740004687474707400096c6f63616c686f73747400062f77732f7773),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'avatar', _binary 0xaced0005740000),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'email', _binary 0xaced00057400176e6875797472616e313232323340676d61696c2e636f6d),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'fullName', _binary 0xaced000574000e5472e1baa76e204e68c6b020c39d),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'id', _binary 0xaced00057372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b02000078700000000000000003),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'jakarta.servlet.jsp.jstl.fmt.request.charset', _binary 0xaced00057400055554462d38),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN', _binary 0xaced0005737200366f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e637372662e44656661756c7443737266546f6b656e5aefb7c82fa2fbd50200034c000a6865616465724e616d657400124c6a6176612f6c616e672f537472696e673b4c000d706172616d657465724e616d6571007e00014c0005746f6b656e71007e0001787074000c582d435352462d544f4b454e7400055f6373726674002430313039306666372d373661332d346266372d626432612d353730663564363161383634),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'role', _binary 0xaced00057400074d414e41474552),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'SPRING_SECURITY_CONTEXT', _binary 0xaced00057372003d6f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e636f6e746578742e5365637572697479436f6e74657874496d706c000000000000026c0200014c000e61757468656e7469636174696f6e7400324c6f72672f737072696e676672616d65776f726b2f73656375726974792f636f72652f41757468656e7469636174696f6e3b78707372004f6f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e557365726e616d6550617373776f726441757468656e7469636174696f6e546f6b656e000000000000026c0200024c000b63726564656e7469616c737400124c6a6176612f6c616e672f4f626a6563743b4c00097072696e636970616c71007e0004787200476f72672e737072696e676672616d65776f726b2e73656375726974792e61757468656e7469636174696f6e2e416273747261637441757468656e7469636174696f6e546f6b656ed3aa287e6e47640e0200035a000d61757468656e746963617465644c000b617574686f7269746965737400164c6a6176612f7574696c2f436f6c6c656374696f6e3b4c000764657461696c7371007e0004787001737200266a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c654c697374fc0f2531b5ec8e100200014c00046c6973747400104c6a6176612f7574696c2f4c6973743b7872002c6a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65436f6c6c656374696f6e19420080cb5ef71e0200014c00016371007e00067870737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200426f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e617574686f726974792e53696d706c654772616e746564417574686f72697479000000000000026c0200014c0004726f6c657400124c6a6176612f6c616e672f537472696e673b787074000c524f4c455f4d414e414745527871007e000d737200486f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e61757468656e7469636174696f6e2e57656241757468656e7469636174696f6e44657461696c73000000000000026c0200024c000d72656d6f74654164647265737371007e000f4c000973657373696f6e496471007e000f787074000f303a303a303a303a303a303a303a3174002437353266353135342d336163332d343939302d393930612d39646666346635376133336570737200326f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e55736572000000000000026c0200075a00116163636f756e744e6f6e457870697265645a00106163636f756e744e6f6e4c6f636b65645a001563726564656e7469616c734e6f6e457870697265645a0007656e61626c65644c000b617574686f72697469657374000f4c6a6176612f7574696c2f5365743b4c000870617373776f726471007e000f4c0008757365726e616d6571007e000f787001010101737200256a6176612e7574696c2e436f6c6c656374696f6e7324556e6d6f6469666961626c65536574801d92d18f9b80550200007871007e000a737200116a6176612e7574696c2e54726565536574dd98509395ed875b0300007870737200466f72672e737072696e676672616d65776f726b2e73656375726974792e636f72652e7573657264657461696c732e5573657224417574686f72697479436f6d70617261746f72000000000000026c020000787077040000000171007e001078707400176e6875797472616e313232323340676d61696c2e636f6d),
	('9f72116e-a9af-4c66-971f-9d1cafc46b86', 'SPRING_SECURITY_SAVED_REQUEST', _binary 0xaced0005737200416f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e44656661756c74536176656452657175657374000000000000026c02000f49000a736572766572506f72744c000b636f6e74657874506174687400124c6a6176612f6c616e672f537472696e673b4c0007636f6f6b6965737400154c6a6176612f7574696c2f41727261794c6973743b4c00076865616465727374000f4c6a6176612f7574696c2f4d61703b4c00076c6f63616c657371007e00024c001c6d61746368696e6752657175657374506172616d657465724e616d6571007e00014c00066d6574686f6471007e00014c000a706172616d657465727371007e00034c000870617468496e666f71007e00014c000b7175657279537472696e6771007e00014c000a7265717565737455524971007e00014c000a7265717565737455524c71007e00014c0006736368656d6571007e00014c000a7365727665724e616d6571007e00014c000b736572766c65745061746871007e0001787000001f90740000737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a65787000000001770400000001737200396f72672e737072696e676672616d65776f726b2e73656375726974792e7765622e7361766564726571756573742e5361766564436f6f6b6965000000000000026c0200084900066d61784167655a000673656375726549000776657273696f6e4c0007636f6d6d656e7471007e00014c0006646f6d61696e71007e00014c00046e616d6571007e00014c00047061746871007e00014c000576616c756571007e00017870ffffffff0000000000707074000753455353494f4e707400304e7a55795a6a55784e5451744d32466a4d7930304f546b774c546b354d4745744f57526d5a6a526d4e5464684d7a4e6c78737200116a6176612e7574696c2e547265654d61700cc1f63e2d256ae60300014c000a636f6d70617261746f727400164c6a6176612f7574696c2f436f6d70617261746f723b78707372002a6a6176612e6c616e672e537472696e672443617365496e73656e736974697665436f6d70617261746f7277035c7d5c50e5ce02000078707704000000107400066163636570747371007e000600000001770400000001740087746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f617669662c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e377874000f6163636570742d656e636f64696e677371007e000600000001770400000001740017677a69702c206465666c6174652c2062722c207a7374647874000f6163636570742d6c616e67756167657371007e00060000000177040000000174003876692c656e2d55533b713d302e392c656e3b713d302e382c76692d564e3b713d302e372c66722d46523b713d302e362c66723b713d302e357874000a636f6e6e656374696f6e7371007e00060000000177040000000174000a6b6565702d616c69766578740006636f6f6b69657371007e00060000000177040000000174003853455353494f4e3d4e7a55795a6a55784e5451744d32466a4d7930304f546b774c546b354d4745744f57526d5a6a526d4e5464684d7a4e6c78740004686f73747371007e00060000000177040000000174000e6c6f63616c686f73743a3830383078740007726566657265727371007e000600000001770400000001740027687474703a2f2f6c6f63616c686f73743a383038302f61646d696e2f626f6f6b696e672f313431787400097365632d63682d75617371007e000600000001770400000001740041224368726f6d69756d223b763d22313336222c2022476f6f676c65204368726f6d65223b763d22313336222c20224e6f742e412f4272616e64223b763d22393922787400107365632d63682d75612d6d6f62696c657371007e0006000000017704000000017400023f30787400127365632d63682d75612d706c6174666f726d7371007e0006000000017704000000017400092257696e646f7773227874000e7365632d66657463682d646573747371007e000600000001770400000001740008646f63756d656e747874000e7365632d66657463682d6d6f64657371007e0006000000017704000000017400086e617669676174657874000e7365632d66657463682d736974657371007e00060000000177040000000174000b73616d652d6f726967696e7874000e7365632d66657463682d757365727371007e0006000000017704000000017400023f3178740019757067726164652d696e7365637572652d72657175657374737371007e000600000001770400000001740001317874000a757365722d6167656e747371007e00060000000177040000000174006f4d6f7a696c6c612f352e30202857696e646f7773204e542031302e303b2057696e36343b2078363429204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f3133362e302e302e30205361666172692f3533372e333678787371007e000600000006770400000006737200106a6176612e7574696c2e4c6f63616c657ef811609c30f9ec03000649000868617368636f64654c0007636f756e74727971007e00014c000a657874656e73696f6e7371007e00014c00086c616e677561676571007e00014c000673637269707471007e00014c000776617269616e7471007e00017870ffffffff71007e000571007e0005740002766971007e000571007e0005787371007e0042ffffffff740002555371007e0005740002656e71007e000571007e0005787371007e0042ffffffff71007e000571007e000571007e004771007e000571007e0005787371007e0042ffffffff740002564e71007e000571007e004471007e000571007e0005787371007e0042ffffffff740002465271007e0005740002667271007e000571007e0005787371007e0042ffffffff71007e000571007e000571007e004d71007e000571007e00057878740008636f6e74696e75657400034745547371007e000c707704000000007870707400102f61646d696e2f64617368626f617264740025687474703a2f2f6c6f63616c686f73743a383038302f61646d696e2f64617368626f617264740004687474707400096c6f63616c686f73747400102f61646d696e2f64617368626f617264);

-- Dumping structure for table homestay_management_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `avatar` varchar(500) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_enabled` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `idx_users_role_id` (`role_id`),
  CONSTRAINT `fk_users_to_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.users: ~9 rows (approximately)
INSERT INTO `users` (`user_id`, `email`, `password`, `role_id`, `full_name`, `phone`, `address`, `avatar`, `created_at`, `is_enabled`) VALUES
	(3, 'nhuytran12223@gmail.com', '$2a$10$ox4/rTcqw1h7gVojBNMLxeXHDX0kmUccCZ574hiC4vggjNM4kBm/e', 3, 'Trần Như Ý', '0796632547', 'TP Huế', '', NULL, 1),
	(4, 'vinmininhok@gmail.com', '$2a$10$VPjlR/4KVXNFQW45dLb94Os0zWl4Or0K8R7ON//yJ3T/cSXPaGvm2', 6, 'Nguyễn Hoàng Minh', '0932598742', 'Huế', NULL, NULL, 1),
	(5, '21t1020105@husc.edu.vn', '$2a$10$fFD2LKe.KR/ojItBkHOcEeiEPmsqV1XGMEMCtmhRjjtuDQ8boy2I.', 6, 'Như Ý Trần', '0796639876', 'Huế', '1746953049873-z5789101574871_593999e2b62368fbc0ebaa25e20a7031.jpg', '2025-05-11 15:08:53', 1),
	(6, 'nhuyvinmini1218@gmail.com', '$2a$10$NCTO/fxRUAUNdMiwDi8EGuNQYScksX9lb/CLjlRqPjYQ5.e6AgJo2', 6, 'Nguyễn Anh Quân', '', NULL, NULL, '2025-05-13 17:22:21', 1),
	(7, 'nhuytran12022003@gmail.com', '$2a$10$R/ol7BkA1oJM7hzoZeuj7eju564.LiHw9y2TTOXMb6I5yLRggFriS', 6, 'Phạm Khánh Minh', '', NULL, NULL, '2025-05-15 08:46:30', 1),
	(15, 'nhuydengxin2@gmail.com', '$2a$10$Kqe8Cuz3UgrV/UCXVTvYRu0VYkPi/aHd6Pu3MGKJ5N4ITP3oq69IS', 6, 'Nguyễn Bình An', '', NULL, NULL, '2025-05-15 20:52:44', 1),
	(16, 'quocanh@gmail.com', '$2a$10$O/Lgg7/EpTFgLweLTdVwWeJQ6Tq0bQp5OO4YOBmanasRqlxREaV6G', 5, 'Lê Quốc Anh', '0936587936', 'TP Huế', NULL, '2025-05-16 22:09:33', 1),
	(17, 'thaonguyen@gmail.com', '$2a$10$3FUsPtVa9FtBJb7OauQmFuJ0z8f2VpEwp0D0eR5S12tbSBFGA1uLG', 4, 'Nguyễn Lê Thảo Nguyên', '0385645752', 'Huế', NULL, '2025-05-16 22:33:30', 1),
	(18, 'nhuydengxin@gmail.com', '$2a$10$swxgLXE.PjP2RIepDDLXs.c0usAMydTHeHBy7hDTmVYjHGQdlUtMq', 6, 'Hoàng Khánh An', '', NULL, NULL, '2025-05-24 12:51:24', 1);

-- Dumping structure for table homestay_management_db.verification_tokens
CREATE TABLE IF NOT EXISTS `verification_tokens` (
  `token_id` bigint NOT NULL AUTO_INCREMENT,
  `token` varchar(100) NOT NULL,
  `user_id` bigint NOT NULL,
  `expiry_date` datetime NOT NULL,
  `is_used` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`token_id`),
  KEY `idx_verification_customer_id` (`user_id`),
  CONSTRAINT `fk_verification_tokens_to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table homestay_management_db.verification_tokens: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
