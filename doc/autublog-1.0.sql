/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 23/06/2019 21:29:58
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
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_user
-- ----------------------------
DROP TABLE IF EXISTS `agent_user`;
CREATE TABLE `agent_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cookie` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `website` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of agent_user
-- ----------------------------
INSERT INTO `agent_user` VALUES (1, 'bea491f2a2134a12a8d5b74b2ad91c94', '范文皓', '2019-06-23 21:27:20', NULL, '2195743583@qq.com');

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `gmtCreate` datetime(0) NOT NULL COMMENT '发表时间',
  `pv` int(11) NULL DEFAULT 1 COMMENT '阅读量',
  `thumbImg` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预览图片',
  `state` int(1) NULL DEFAULT 0 COMMENT '0为草稿1为发布2为删除3',
  `isOriginal` tinyint(1) NULL DEFAULT 1 COMMENT '1为原创,0为转载',
  `isTop` tinyint(1) NULL DEFAULT 0 COMMENT '1为置顶文章,0相反',
  `gmtModified` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `identify` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问路径',
  `intro` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `allowComment` tinyint(1) NULL DEFAULT NULL,
  `userId` int(11) NULL DEFAULT NULL,
  `isCustom` tinyint(1) NULL DEFAULT NULL COMMENT '是否为定制页面',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `page` VALUES (1, '/resume', '/_view/diy/page/resume/foryou/resume/index.html', 1, '2019-06-23 21:05:34', 0, NULL, 'ceshi', 1, NULL, '1');
INSERT INTO `page` VALUES (9, '/links', '/_view/diy/page/links/links.html', NULL, '2019-06-24 12:53:02', 0, NULL, NULL, 2, NULL, NULL);
INSERT INTO `page` VALUES (10, '/about', '/_view/diy/page/diy[1561269749537].html', NULL, '2019-06-24 12:53:00', 0, NULL, NULL, 3, NULL, NULL);
-- ----------------------------
-- Table structure for baidu_seo_config
-- ----------------------------
DROP TABLE IF EXISTS `baidu_seo_config`;
CREATE TABLE `baidu_seo_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of baidu_seo_config
-- ----------------------------
INSERT INTO `baidu_seo_config` VALUES (1, '1', '2');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论者名称',
  `website` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网站url',
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `userId` int(11) NULL DEFAULT NULL COMMENT '评论者id',
  `parentId` int(11) NULL DEFAULT NULL,
  `identify` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `isAduit` tinyint(1) NULL DEFAULT NULL,
  `toUserId` int(11) NULL DEFAULT NULL COMMENT '回复对象的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (5, 'test', '2019-06-23 21:28:22', '范文皓', NULL, '2195743583@qq.com', 1, 0, '20180930191139', 1, NULL);

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keywords` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `author` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ICPRecord` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  `emailServer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fromEmail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `emailPassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ico` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `isAuditComment` tinyint(1) NULL DEFAULT NULL,
  `qiniuAk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qiniuSk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qiniuUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qiniuBucket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `theme` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'pinghsu',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'Autu个人博客', '一款开源的个人博客', '范文皓', '12', 'Autu个人博客', '2018-09-30 19:19:32', '3', '1', '2', '/upload/ico/ico/520520_20180930205220.ico', '/upload/logo/png/520520_20180930205236.png', 0, '4', '5', '7', '6', 'pinghsu');

-- ----------------------------
-- Table structure for disk
-- ----------------------------
DROP TABLE IF EXISTS `disk`;
CREATE TABLE `disk`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `size` int(10) UNSIGNED NULL DEFAULT NULL,
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parentId` int(11) NULL DEFAULT 0,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `gmtModify` datetime(0) NULL DEFAULT NULL,
  `state` tinyint(2) NULL DEFAULT 0 COMMENT '0为正常1为假删除',
  `thumbUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of disk
-- ----------------------------
INSERT INTO `disk` VALUES (2, '12', NULL, NULL, 'folder', 0, '2019-06-13 21:35:58', '2019-06-13 21:35:59', 0, NULL, NULL);
INSERT INTO `disk` VALUES (3, '12', NULL, NULL, 'folder', 2, '2019-06-13 21:36:43', '2019-06-13 21:36:43', 0, NULL, NULL);

-- ----------------------------
-- Table structure for login_record
-- ----------------------------
DROP TABLE IF EXISTS `login_record`;
CREATE TABLE `login_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '登录记录id',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '登录时间',
  `ip` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地址',
  `sessionId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录设备信息',
  `isValid` tinyint(1) NULL DEFAULT 1 COMMENT '0为无效1为有效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_record
-- ----------------------------
INSERT INTO `login_record` VALUES (1, '2019-05-30 19:11:16', '127.0.0.1', '7021e43d246448cb836e6a965ce91d25', 'Windows NT 10.0; WOW64', 0);
INSERT INTO `login_record` VALUES (2, '2019-06-14 20:45:03', '127.0.0.1', '50dc559a85634c6283df8dc8c5fe6dbc', 'Windows NT 10.0; WOW64', 1);
INSERT INTO `login_record` VALUES (3, '2019-06-23 21:19:33', '127.0.0.1', '115702af99c24812866e90fc982208b1', 'Windows NT 10.0; WOW64', 1);

-- ----------------------------
-- Table structure for meta
-- ----------------------------
DROP TABLE IF EXISTS `meta`;
CREATE TABLE `meta`  (
  `mname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `count` tinyint(4) NULL DEFAULT NULL COMMENT '总数',
  `sort` tinyint(4) NULL DEFAULT NULL COMMENT '排序',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meta
-- ----------------------------
INSERT INTO `meta` VALUES ('1212', 'tag', NULL, NULL, NULL, 25);

-- ----------------------------
-- Table structure for metaweblog_config
-- ----------------------------
DROP TABLE IF EXISTS `metaweblog_config`;
CREATE TABLE `metaweblog_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `website` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of metaweblog_config
-- ----------------------------
INSERT INTO `metaweblog_config` VALUES (1, '1', '3', '4', '2019-06-14 23:13:44', '2');

-- ----------------------------
-- Table structure for metaweblog_relevance
-- ----------------------------
DROP TABLE IF EXISTS `metaweblog_relevance`;
CREATE TABLE `metaweblog_relevance`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `metaweblogId` int(11) NULL DEFAULT NULL,
  `postId` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `articleId` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nav
-- ----------------------------
DROP TABLE IF EXISTS `nav`;
CREATE TABLE `nav`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nav
-- ----------------------------
INSERT INTO `nav` VALUES (1, '首页', '/2', 99);
INSERT INTO `nav` VALUES (2, '友链', '/links', 98);
INSERT INTO `nav` VALUES (3, '关于我', '/about', 97);
INSERT INTO `nav` VALUES (4, '简历', '/resume', 1);
INSERT INTO `nav` VALUES (5, '归档', '/timeline', 2);

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pattern` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url正则匹配',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面路径',
  `userId` int(11) NULL DEFAULT NULL COMMENT '添加人Id',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `isDeleted` tinyint(1) NULL DEFAULT 0,
  `gmtModified` datetime(0) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `articleId` int(11) NULL DEFAULT NULL COMMENT '关联的文章id',
  `isUsing` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `remark` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of page
-- ----------------------------
INSERT INTO `page` VALUES (1, '/resume', '/_view/diy/page/resume/foryou/resume/index.html', 1, '2019-06-23 21:05:34', 0, NULL, 'ceshi', 1, NULL, '1');
INSERT INTO `page` VALUES (9, '/links', '/_view/diy/page/links/links.html', NULL, '2019-06-23 21:05:09', 0, NULL, NULL, 78, NULL, NULL);
INSERT INTO `page` VALUES (10, '/about', '/_view/diy/page/diy[1561269749537].html', NULL, '2019-06-23 21:05:19', 0, NULL, NULL, 79, NULL, NULL);

-- ----------------------------
-- Table structure for relevancy
-- ----------------------------
DROP TABLE IF EXISTS `relevancy`;
CREATE TABLE `relevancy`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mid` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '关联主id',
  `cid` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '内容id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for session
-- ----------------------------
DROP TABLE IF EXISTS `session`;
CREATE TABLE `session`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expireAt` bigint(20) NOT NULL,
  `userId` int(10) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of session
-- ----------------------------
INSERT INTO `session` VALUES ('50dc559a85634c6283df8dc8c5fe6dbc', 1592052303128, 2);
INSERT INTO `session` VALUES ('115702af99c24812866e90fc982208b1', 1592831973181, 2);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名，同时作为登录账号',
  `pwd` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录密码',
  `age` tinyint(4) NOT NULL DEFAULT 1 COMMENT '用户年龄',
  `sex` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1为男，0为女',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户邮箱',
  `level` tinyint(2) NOT NULL DEFAULT 0 COMMENT '用户等级权限,1为超级管理员,0为普通用户',
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `gmtCreate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '用户注册时间',
  `qq` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `abposition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `headImg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `about` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, '范文皓', '21232f297a57a5a743894a0e4a801fc3', 18, 1, '2195743583@qq.com', 1, 'admin', '2018-09-10 15:47:17', '476382755', '??JavaWEB???', 'JavaWEB???', NULL, '??', '<p>????</p><p><br/></p>');

SET FOREIGN_KEY_CHECKS = 1;
