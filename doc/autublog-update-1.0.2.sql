ALTER TABLE `login_record`
DROP COLUMN `userId`,
CHANGE COLUMN `time` `gmtCreate`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间' AFTER `id`,
MODIFY COLUMN `ip`  varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '登录地址' AFTER `gmtCreate`,
ADD COLUMN `sessionId`  varchar(256) NULL AFTER `ip`,
ADD COLUMN `device`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '登录设备信息' AFTER `sessionId`,
ADD COLUMN `isValid`  tinyint(1) NULL DEFAULT 1 COMMENT '0为无效1为有效' AFTER `device`;