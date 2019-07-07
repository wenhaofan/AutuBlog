CREATE TABLE `operate_log` (
  `pkId` int(11) NOT NULL AUTO_INCREMENT,
  `gmtCreate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `userId` int(11) NOT NULL COMMENT '操作人',
  `type` tinyint(2) NOT NULL COMMENT '操作类型，0增，1删，2改',
  `data` varchar(2048) COLLATE utf8mb4_bin NOT NULL COMMENT '操作的数据 request 请求数据的json格式',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL COMMENT '请求路径',
  `moduleName` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模块名称',
  PRIMARY KEY (`pkId`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC