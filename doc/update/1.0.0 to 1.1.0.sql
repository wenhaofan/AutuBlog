ALTER TABLE `article`
DROP COLUMN `contentType`,
ADD COLUMN `contentType`  varchar(255) NULL DEFAULT 'html' COMMENT '可选值 html,markdown' AFTER `userId`;
