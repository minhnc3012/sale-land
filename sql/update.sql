ALTER TABLE `saleland`.`jhi_user`
	ADD COLUMN `provider` VARCHAR(20) DEFAULT 'APPLICATION' NULL AFTER `reset_key`;
