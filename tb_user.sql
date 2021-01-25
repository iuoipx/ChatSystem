/*
Navicat MySQL Data Transfer

Source Server         : Test
Source Server Version : 80012
Source Host           : localhost:3306
Source Database       : userinfo

Target Server Type    : MYSQL
Target Server Version : 80012
File Encoding         : 65001

Date: 2020-12-04 16:29:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('ee', 'ee', 'ee@ee.c');
INSERT INTO `tb_user` VALUES ('qq', 'qq', 'qq@qq.c');
INSERT INTO `tb_user` VALUES ('ww', 'ww', 'ww@ww.c');
