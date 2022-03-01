ALTER TABLE `jhi_user`
	ADD COLUMN `provider` VARCHAR(20) DEFAULT 'DATABASE' NULL AFTER `reset_key`;
ALTER TABLE `jhi_user`   
	ADD COLUMN `provider_uid` VARCHAR(50) NULL AFTER `provider`;

ALTER TABLE `jhi_user`   
	CHANGE `password_hash` `password_hash` VARCHAR(60) NULL;
ALTER TABLE `land`   
	CHANGE `title` `title` TEXT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
	CHANGE `address` `address` TEXT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
	CHANGE `description` `description` TEXT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci NULL;