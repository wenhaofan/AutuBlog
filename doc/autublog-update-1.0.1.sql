ALTER TABLE `config`
ADD COLUMN `theme`  varchar(255) NULL AFTER `qiniuBucket`;
update config set theme="pinghsu"