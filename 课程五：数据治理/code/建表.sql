/*
 Navicat Premium Data Transfer

 Source Server         : sql
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 10.16.1.105:3306
 Source Schema         : student25

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 03/07/2021 12:17:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for 卖家
-- ----------------------------
DROP TABLE IF EXISTS `卖家`;
CREATE TABLE `卖家` (
  `producer-id` int(11) NOT NULL COMMENT '商家ID',
  `date` datetime DEFAULT NULL COMMENT '商家注册时间',
  PRIMARY KEY (`producer-id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 卖家
-- ----------------------------
BEGIN;
INSERT INTO `卖家` VALUES (1, '2021-04-01 00:00:00');
INSERT INTO `卖家` VALUES (2, '2021-03-01 00:00:00');
INSERT INTO `卖家` VALUES (3, '2021-01-01 00:00:00');
COMMIT;

-- ----------------------------
-- Table structure for 商品
-- ----------------------------
DROP TABLE IF EXISTS `商品`;
CREATE TABLE `商品` (
  `product-id` int(11) NOT NULL COMMENT '商品ID',
  `user-id` int(11) DEFAULT NULL COMMENT '购买该商品的用户ID',
  `producer-id` int(11) DEFAULT NULL COMMENT '售卖此商品的商家ID',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售卖单价',
  `category` varchar(255) DEFAULT NULL COMMENT '商品类型',
  `sale-date` datetime DEFAULT NULL COMMENT '销售时间',
  `sale-number` int(11) DEFAULT NULL COMMENT '共卖出了多少个',
  PRIMARY KEY (`product-id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 商品
-- ----------------------------
BEGIN;
INSERT INTO `商品` VALUES (1, 1, 1, 20.00, '日用品', '2021-05-01 00:00:00', 10);
INSERT INTO `商品` VALUES (2, 1, 1, 7.00, '电子产品', '2021-06-01 00:00:00', 5);
INSERT INTO `商品` VALUES (3, 2, 2, 15.00, '化妆品', '2021-01-01 00:00:00', 2);
INSERT INTO `商品` VALUES (4, 3, 3, 10.00, '电子产品', '2021-02-03 00:00:00', 3);
INSERT INTO `商品` VALUES (5, 4, 2, 6.00, '书本', '2021-03-05 00:00:00', 5);
INSERT INTO `商品` VALUES (6, 5, 3, 8.00, '日用品', '2021-04-07 00:00:00', 3);
COMMIT;

-- ----------------------------
-- Table structure for 用户
-- ----------------------------
DROP TABLE IF EXISTS `用户`;
CREATE TABLE `用户` (
  `user-id` int(11) NOT NULL COMMENT '用户ID',
  `phone` varchar(255) DEFAULT NULL COMMENT '用户手机号',
  `e-mail` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `sex` varchar(255) DEFAULT NULL COMMENT '用户性别',
  `age` int(11) DEFAULT NULL COMMENT '用户年龄',
  `date` datetime DEFAULT NULL COMMENT '注册时间',
  `city` varchar(255) DEFAULT NULL COMMENT '用户所在城市',
  PRIMARY KEY (`user-id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 用户
-- ----------------------------
BEGIN;
INSERT INTO `用户` VALUES (1, '13381372030', 'edwardlyz@163.com', '男性', 18, '2021-05-01 00:00:00', '广州');
INSERT INTO `用户` VALUES (2, '15626406829', '12345678@gmail.com', '女性', 19, '2021-06-07 00:00:00', '佛山');
INSERT INTO `用户` VALUES (3, '123456789', '23456@test', '男性', 17, '2021-05-08 00:00:00', '东莞');
INSERT INTO `用户` VALUES (4, '13872801342', '1208870448@163.com', '男性', 27, '2021-03-08 00:00:00', '中山');
INSERT INTO `用户` VALUES (5, '13272801356', 'edward@foxmaill.com', '女性', 22, '2021-01-01 00:00:00', '广州');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
