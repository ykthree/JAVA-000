CREATE SCHEMA IF NOT EXISTS `mydb_1` DEFAULT CHARACTER SET utf8 ;
USE `mydb_1` ;

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_1` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_1` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_3` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_3` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_5` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_5` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_7` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_7` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_9` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_9` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_11` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_11` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_13` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_13` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

DROP TABLE IF EXISTS `mydb_1`.`t_order_master_15` ;
CREATE TABLE IF NOT EXISTS `mydb_1`.`t_order_master_15` (
  `id` INT UNSIGNED NOT NULL COMMENT '主键 ID',
  `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
  `customer_id` INT NOT NULL COMMENT '下单人 ID',
  `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
  `ship_time` TIMESTAMP NULL COMMENT '发货时间',
  `pay_time` TIMESTAMP NULL COMMENT '支付时间',
  `receive_time` TIMESTAMP NULL COMMENT '收货时间',
  `discount_money` DECIMAL(8,2) NOT NULL,
  `ship_money` DECIMAL(8,2) NOT NULL,
  `pay_money` DECIMAL(8,2) NOT NULL,
  `pay_method` TINYINT(1) NULL,
  `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
  `receive_user` VARCHAR(10) NOT NULL COMMENT '收获人',
  `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
  `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';
