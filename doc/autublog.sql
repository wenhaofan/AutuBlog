/*
 Navicat Premium Data Transfer

 Source Server         : myblog
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 47.106.239.64:3306
 Source Schema         : myblog

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 15/03/2019 13:23:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_log
-- ----------------------------
DROP TABLE IF EXISTS `access_log`;
CREATE TABLE `access_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gmtCreate` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `referer` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求来源地址',
  `target` varchar(12024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '请求访问地址',
  `ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源Ip',
  `cookie` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问者cookie 标识是否是重复访问',
  `info` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细日志',
  `userAgent` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agentUserId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54514 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_user
-- ----------------------------
DROP TABLE IF EXISTS `agent_user`;
CREATE TABLE `agent_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cookie` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `website` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48714 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '文章内容',
  `htmlTitle` char(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '暂使用' COMMENT '文章的静态页面名称',
  `gmtCreate` datetime(0) NOT NULL COMMENT '发表时间',
  `pv` int(11) NULL DEFAULT 1 COMMENT '阅读量',
  `thumbImg` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '预览图片',
  `state` int(1) NULL DEFAULT 0 COMMENT '0为草稿1为发布2为删除',
  `isOriginal` int(1) NULL DEFAULT 1 COMMENT '1为原创,0为转载',
  `isTop` tinyint(1) NULL DEFAULT 0 COMMENT '1为置顶文章,0相反',
  `gmtModified` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `identify` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '访问路径',
  `intro` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `allowComment` tinyint(1) NULL DEFAULT NULL,
  `userId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;
INSERT INTO `article` VALUES ('1', '第一篇文章', 0x3C6831207374796C653D22666F6E742D66616D696C793A202671756F743B4E6F746F2053616E732671756F743B2C2073616E732D73657269663B20636F6C6F723A2072676228302C20302C2030293B223EE7ACACE4B880E7AF873C2F68313E3C703E266E6273703B20266E6273703B20E4BDA0E5868DE69DA5E58699E782B9E4BB80E4B988E590A73C2F703E, '2018-09-30 19:11:39', '6', '/upload/thumb/jpg/520520_20180930191137.jpg', '1', '0', '2018-09-30 19:11:39', '20180930191139', '第一篇    你再来写点什么吧', null, '1');

-- ----------------------------
-- Table structure for baidu_seo_config
-- ----------------------------
DROP TABLE IF EXISTS `baidu_seo_config`;
CREATE TABLE `baidu_seo_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blogroll
-- ----------------------------
DROP TABLE IF EXISTS `blogroll`;
CREATE TABLE `blogroll`  (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT COMMENT '友链id',
  `sort` tinyint(5) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url` varchar(124) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '友链网址',
  `gmtCreate` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_url`(`url`) USING BTREE,
  UNIQUE INDEX `uk_name`(`title`) USING BTREE,
  UNIQUE INDEX `uk_priority`(`sort`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;
INSERT INTO `blogroll` VALUES ('1', '1', 'Autu博客', 'www.wenhaofan.com', '2018-09-30 19:19:59');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评论者名称',
  `website` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '网站url',
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `userId` int(11) NULL DEFAULT NULL COMMENT '评论者id',
  `parentId` int(11) NULL DEFAULT NULL,
  `identify` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isAduit` tinyint(1) NULL DEFAULT NULL,
  `toUserId` int(11) NULL DEFAULT NULL COMMENT '回复对象的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keywords` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `author` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ICPRecord` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `emailServer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fromEmail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `emailPassword` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ico` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `isAuditComment` tinyint(1) NULL DEFAULT NULL,
  `qiniuAk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qiniuSk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qiniuUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qiniuBucket` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `theme` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'pinghsu',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
INSERT INTO `config` VALUES ('1', 'Autu个人博客', '一款开源的个人博客', '范文皓', null, 'Autu个人博客', '2018-09-30 19:19:32', null, null, null, '/upload/ico/ico/520520_20180930205220.ico', '/upload/logo/png/520520_20180930205236.png', null, null, null, null, 'pinghsu');

-- ----------------------------
-- Table structure for disk
-- ----------------------------
DROP TABLE IF EXISTS `disk`;
CREATE TABLE `disk`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` int(10) UNSIGNED NULL DEFAULT NULL,
  `url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parentId` int(11) NULL DEFAULT 0,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmtModify` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(2) NULL DEFAULT 0 COMMENT '0为正常1为假删除',
  `thumbUrl` varchar(248) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for html_action
-- ----------------------------
DROP TABLE IF EXISTS `html_action`;
CREATE TABLE `html_action`  (
  `id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_record
-- ----------------------------
DROP TABLE IF EXISTS `login_record`;
CREATE TABLE `login_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '登录记录id',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  `ip` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '登录地址',
  `sessionId` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `device` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '登录设备信息',
  `isValid` tinyint(1) NULL DEFAULT 1 COMMENT '0为无效1为有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for meta
-- ----------------------------
DROP TABLE IF EXISTS `meta`;
CREATE TABLE `meta`  (
  `mname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名',
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `count` tinyint(4) NULL DEFAULT NULL COMMENT '总数',
  `sort` tinyint(4) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metaweblog_config
-- ----------------------------
DROP TABLE IF EXISTS `metaweblog_config`;
CREATE TABLE `metaweblog_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `website` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userName` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for metaweblog_relevance
-- ----------------------------
DROP TABLE IF EXISTS `metaweblog_relevance`;
CREATE TABLE `metaweblog_relevance`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metaweblogId` int(11) NULL DEFAULT NULL,
  `postId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `articleId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nav
-- ----------------------------
DROP TABLE IF EXISTS `nav`;
CREATE TABLE `nav`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
INSERT INTO `nav` VALUES ('1', '首页', '/', '99');
INSERT INTO `nav` VALUES ('2', '友链', '/links', '98');
INSERT INTO `nav` VALUES ('3', '关于我', '/about', '97');

-- ----------------------------
-- Table structure for profiles
-- ----------------------------
DROP TABLE IF EXISTS `profiles`;
CREATE TABLE `profiles`  (
  `id` int(2) NOT NULL DEFAULT 0,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '展示页面内容',
  `pageName` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '静态页面名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for relevancy
-- ----------------------------
DROP TABLE IF EXISTS `relevancy`;
CREATE TABLE `relevancy`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mid` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '关联主id',
  `cid` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '内容id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 326 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for route
-- ----------------------------
DROP TABLE IF EXISTS `route`;
CREATE TABLE `route`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rkey` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `rview` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for session
-- ----------------------------
DROP TABLE IF EXISTS `session`;
CREATE TABLE `session`  (
  `id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `expireAt` bigint(20) NOT NULL,
  `userId` int(10) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data` longtext CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `ip` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `userId` int(11) NULL DEFAULT NULL,
  `level` int(2) NULL DEFAULT 0 COMMENT '0为普通日志1为警告日志2为错误日志',
  `url` varchar(2048) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 460 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户姓名，同时作为登录账号',
  `pwd` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户登录密码',
  `age` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户年龄',
  `sex` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1为男，0为女',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户邮箱',
  `level` tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户等级权限,1为超级管理员,0为普通用户',
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '登录账号',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '用户注册时间',
  `qq` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `abposition` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `headImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `about` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
