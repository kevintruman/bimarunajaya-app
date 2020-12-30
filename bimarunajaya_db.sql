/*
 Navicat Premium Data Transfer

 Source Server         : local mysql
 Source Server Type    : MySQL
 Source Server Version : 100137
 Source Host           : localhost:3306
 Source Schema         : bimarunajaya_db

 Target Server Type    : MySQL
 Target Server Version : 100137
 File Encoding         : 65001

 Date: 30/12/2020 13:57:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for package
-- ----------------------------
DROP TABLE IF EXISTS `package`;
CREATE TABLE `package`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `last_position_id` int(11) NULL DEFAULT NULL,
  `last_station_id` int(11) NULL DEFAULT NULL,
  `address_from` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `send_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `send_phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `address_to` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `receive_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `receive_phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `package_description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `weight` double NULL DEFAULT NULL,
  `dimension` int(11) NULL DEFAULT NULL,
  `price` double NULL DEFAULT NULL,
  `price_description` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `last_delivery_status` int(11) NULL DEFAULT NULL,
  `approve_status` int(11) NULL DEFAULT NULL,
  `resi_number` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `last_progress_status` int(11) NULL DEFAULT NULL,
  `create_by` int(11) NULL DEFAULT NULL,
  `station_by` int(11) NULL DEFAULT NULL,
  `kota_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for parameter
-- ----------------------------
DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of parameter
-- ----------------------------
INSERT INTO `parameter` VALUES (1, 'small', '0', '< 30 cm^3', NULL);
INSERT INTO `parameter` VALUES (2, 'weight', '10000', NULL, NULL);
INSERT INTO `parameter` VALUES (3, 'tax', '0.1', NULL, NULL);
INSERT INTO `parameter` VALUES (4, 'discount', '0', NULL, NULL);
INSERT INTO `parameter` VALUES (5, 'medium', '30000', '> 30 cm^3', NULL);
INSERT INTO `parameter` VALUES (6, 'large', '100000', '> 1 m^3', NULL);
INSERT INTO `parameter` VALUES (7, 'Jakarta', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (8, 'Tangerang', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (9, 'Bekasi', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (10, 'Sukabumi', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (11, 'Cianjur', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (12, 'Bandung', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (13, 'Cirebon', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (14, 'Tasikmalaya', '4000', NULL, 43);
INSERT INTO `parameter` VALUES (15, 'Pekalongan', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (16, 'Tegal', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (17, 'Semarang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (18, 'Solo', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (19, 'Klaten', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (20, 'Salatiga', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (21, 'Yogyakarta', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (22, 'Magelang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (23, 'Purwokerto', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (24, 'Kudus', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (25, 'Surabaya', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (26, 'Sidoarjo', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (27, 'Pasuruan', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (28, 'Madiun', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (29, 'Malang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (30, 'Kediri', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (31, 'Jombang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (32, 'Jember', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (33, 'Banyuwangi ', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (34, 'Denpasar', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (35, 'Wonosobo', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (36, 'Temanggung', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (37, 'Mojokerto', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (38, 'Subang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (39, 'Sumedang', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (40, 'Mataram', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (41, 'Senggigi', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (42, 'Selong', '5000', NULL, 43);
INSERT INTO `parameter` VALUES (43, 'Jawa - Bali', NULL, NULL, 85);
INSERT INTO `parameter` VALUES (44, 'Balikpapan', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (45, 'Banjarmasin', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (46, 'Batutulis', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (47, 'Bontang', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (48, 'Buntok', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (49, 'Kotabaru', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (50, 'Kutai', '7000', NULL, 60);
INSERT INTO `parameter` VALUES (51, 'Makassar', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (52, 'MuaraTewah', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (53, 'Palangkaraya', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (54, 'Pontianak', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (55, 'Samarinda', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (56, 'Sampit', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (57, 'Sangatta', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (58, 'Tenggarong', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (59, 'BanjarBaru', '9000', NULL, 60);
INSERT INTO `parameter` VALUES (60, 'Kalimantan - Sulawesi', NULL, NULL, 85);
INSERT INTO `parameter` VALUES (61, 'Batam', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (62, 'Bengkulu', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (63, 'Pangkal Pinang', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (64, 'Kalianda', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (65, 'Pring Sewu', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (66, 'Manggala', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (67, 'Minas', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (68, 'Parawang', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (69, 'Bangkinang', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (70, 'Bangkalis', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (71, 'Dumai', '8000', NULL, 84);
INSERT INTO `parameter` VALUES (72, 'Duri', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (73, 'Pangkalan Krinci', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (74, 'Kupang', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (75, 'Pekanbaru', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (76, 'Medan', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (77, 'Bandar Lampung', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (78, 'Palembang', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (79, 'Padang', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (80, 'Jambi', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (81, 'Banda Aceh', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (82, 'Sungai Liat', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (83, 'Lahat', '9000', NULL, 84);
INSERT INTO `parameter` VALUES (84, 'Sumatera', NULL, NULL, 85);
INSERT INTO `parameter` VALUES (85, 'Pulau', NULL, NULL, NULL);
INSERT INTO `parameter` VALUES (86, 'min berat', '5', NULL, NULL);

-- ----------------------------
-- Table structure for tracking
-- ----------------------------
DROP TABLE IF EXISTS `tracking`;
CREATE TABLE `tracking`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `send_by` int(255) NULL DEFAULT NULL,
  `from_by` int(255) NULL DEFAULT NULL,
  `transit_id` int(11) NULL DEFAULT NULL,
  `confirm_by` int(255) NULL DEFAULT NULL,
  `receive_by` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `delivery_status` int(255) NULL DEFAULT NULL,
  `package_id` int(11) NULL DEFAULT NULL,
  `progress_status` int(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES (1, 11, 'transit', NULL);
INSERT INTO `type` VALUES (2, 11, 'process', NULL);
INSERT INTO `type` VALUES (3, 11, 'received', NULL);
INSERT INTO `type` VALUES (4, 12, 'user', NULL);
INSERT INTO `type` VALUES (5, 12, 'station', NULL);
INSERT INTO `type` VALUES (6, 13, 'cashier', NULL);
INSERT INTO `type` VALUES (7, 13, 'admin', NULL);
INSERT INTO `type` VALUES (8, 13, 'delivery', NULL);
INSERT INTO `type` VALUES (9, 13, 'accounting', NULL);
INSERT INTO `type` VALUES (10, 13, 'head', NULL);
INSERT INTO `type` VALUES (11, NULL, 'status', NULL);
INSERT INTO `type` VALUES (12, NULL, 'type_user', NULL);
INSERT INTO `type` VALUES (13, NULL, 'role', NULL);
INSERT INTO `type` VALUES (14, 16, 'progress', NULL);
INSERT INTO `type` VALUES (15, 16, 'done', NULL);
INSERT INTO `type` VALUES (16, NULL, 'progress_status', NULL);
INSERT INTO `type` VALUES (17, 19, 'approve_accounting', NULL);
INSERT INTO `type` VALUES (18, 19, 'approve_head', NULL);
INSERT INTO `type` VALUES (19, NULL, 'approve_status', NULL);
INSERT INTO `type` VALUES (20, 22, 'active_account', NULL);
INSERT INTO `type` VALUES (21, 22, 'not_active_account', NULL);
INSERT INTO `type` VALUES (22, NULL, 'account', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `type_user_id` int(11) NULL DEFAULT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  `station_id` int(11) NULL DEFAULT NULL,
  `status_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '2020-12-23 22:01:58', '2020-12-30 09:36:20', 'kasir', '123', 'a', 'a', 'kasir', 4, 6, 2, 20);
INSERT INTO `user` VALUES (2, '2020-12-23 22:01:58', '2020-12-30 09:41:53', NULL, NULL, 'pusat@pusat', '123', 'Kantor Pusat', 5, NULL, NULL, 20);
INSERT INTO `user` VALUES (3, '2020-12-23 22:01:58', '2020-12-30 09:36:21', 'kasir_cb1', '123', 'kasirsatu@gmail.com', '123123', 'kasir cabang satu', 4, 6, 2, 20);
INSERT INTO `user` VALUES (4, '2020-12-23 22:12:27', NULL, 'kurir', '123', 'deliv1@email.com', '123123', 'kurir', 4, 8, 2, 20);
INSERT INTO `user` VALUES (5, '2020-12-23 22:01:58', '2020-12-30 02:05:34', NULL, NULL, 'cb@cabang', '345345', 'Kantor Cabang 1', 5, NULL, NULL, 20);
INSERT INTO `user` VALUES (6, '2020-12-24 11:49:47', NULL, 'kurir_cb1', '123', 'deliv11@gmail.com', '345345', 'kurir cabang satu', 4, 8, 5, 20);
INSERT INTO `user` VALUES (7, '2020-12-27 18:12:45', NULL, 'acc', '123', 'acc', '123', 'Accouting', 4, 9, 2, 20);
INSERT INTO `user` VALUES (8, '2020-12-27 21:40:46', NULL, 'head', '123', 'head', '123', 'Kepala Kantor', 4, 10, 2, 20);
INSERT INTO `user` VALUES (9, '2020-12-30 01:24:22', '2020-12-30 01:44:58', 'adm', '12345', 'adm@', '123123', 'adm', 4, 7, 2, 20);
INSERT INTO `user` VALUES (10, '2020-12-30 02:06:12', '2020-12-30 02:06:12', NULL, NULL, 'cb2@cb2', '222222', 'Kantor Cabang 2', 5, NULL, NULL, 20);
INSERT INTO `user` VALUES (11, '2020-12-30 09:41:32', '2020-12-30 09:41:57', 'kurir2', '123', 'kurir2', 'kurir2', 'kurir2', 4, 8, 10, 20);

SET FOREIGN_KEY_CHECKS = 1;
