ALTER TABLE `disk`
ADD COLUMN `hash`  varchar(28) NULL DEFAULT NULL AFTER `thumbUrl`;