ALTER TABLE `jhi_user`
	ADD COLUMN `provider` VARCHAR(20) DEFAULT 'DATABASE' NULL AFTER `reset_key`;
ALTER TABLE `jhi_user`   
	ADD COLUMN `provider_uid` VARCHAR(50) NULL AFTER `provider`;