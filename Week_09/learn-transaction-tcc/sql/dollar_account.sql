-- -----------------------------------------------------
-- Schema dollar_account
-- -----------------------------------------------------
-- 外汇交易库
DROP SCHEMA IF EXISTS `dollar_account` ;

-- -----------------------------------------------------
-- Schema dollar_account
--
-- 外汇交易库
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dollar_account` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `dollar_account` ;

-- -----------------------------------------------------
-- Table `dollar_account`.`dollar_account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dollar_account`.`dollar_account` ;

CREATE TABLE IF NOT EXISTS `dollar_account`.`dollar_account` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
  `user_id` INT UNSIGNED NOT NULL COMMENT '用户 ID',
  `balance` DECIMAL(10,0) NOT NULL COMMENT '美元余额',
  `decrease_freeze` DECIMAL(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
  `increase_freeze` DECIMAL(10,0) NOT NULL COMMENT '冻结金额，转账暂存余额',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NULL COMMENT '更新时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '美元账户表';