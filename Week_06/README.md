# 作业说明

- Week_06周六(11/21)
  - 作业 2（必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构。
    - 作业位置：订单商品用户DDL.sql
    
    订单表

    ```sql
    CREATE TABLE IF NOT EXISTS `mydb`.`t_order_master` (
        `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
        `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
        `customer_id` INT NOT NULL COMMENT '下单人 ID',
        `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
        `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
        `ship_time` TIMESTAMP NULL COMMENT '发货时间',
        `pay_time` TIMESTAMP NULL COMMENT '支付时间',
        `receive_time` TIMESTAMP NULL COMMENT '收货时间',
        `discount_money` DECIMAL(8,2) NOT NULL COMMENT '优惠金额',
        `ship_money` DECIMAL(8,2) NOT NULL COMMENT '运费金额',
        `pay_money` DECIMAL(8,2) NOT NULL COMMENT '支付金额',
        `pay_method` TINYINT(1) NULL COMMENT '支付方式',
        `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
        `receice_user` VARCHAR(10) NOT NULL COMMENT '收货人',
        `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
        `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '订单主表';


    CREATE TABLE IF NOT EXISTS `mydb`.`t_order_detail` (
        `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
        `order_id` INT UNSIGNED NOT NULL COMMENT '订单 ID',
        `product_id` INT UNSIGNED NOT NULL COMMENT '商品 ID ',
        `product_name` VARCHAR(45) NOT NULL COMMENT '商品名称',
        `product_count` INT UNSIGNED NOT NULL COMMENT '商品数量',
        `product_price` DECIMAL(8,2) NOT NULL COMMENT '商品单价',
        `product_weight` DECIMAL(8,2) NULL COMMENT '商品量',
        PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '订单明细表';

    ```

    商品表

    ```sql
    CREATE TABLE IF NOT EXISTS `mydb`.`t_product_info` (
        `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
        `product_code` VARCHAR(45) NOT NULL COMMENT '商品编码',
        `product_name` VARCHAR(45) NOT NULL COMMENT '商品名称',
        `product_status` TINYINT(1) NOT NULL COMMENT '商品状态',
        `price` DECIMAL(8,2) NOT NULL COMMENT '商品价格',
        `weight` FLOAT NULL COMMENT '商品重量',
        `length` FLOAT NULL COMMENT '商品长度',
        `width` FLOAT NULL COMMENT '商品宽度',
        `height` FLOAT NULL COMMENT '商品高度',
        `production_date` DATETIME NULL COMMENT '生产日期',
        `shelf_life` VARCHAR(45) NOT NULL COMMENT '有效期',
        `description` VARCHAR(45) NULL COMMENT '商品描述',
        PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '商品信息表';

    CREATE TABLE IF NOT EXISTS `mydb`.`t_product_comment` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `product_id` INT UNSIGNED NOT NULL COMMENT '商品 ID ',
    `order_id` INT UNSIGNED NOT NULL COMMENT '订单 ID ',
    `customer_id` INT UNSIGNED NOT NULL COMMENT '用户 ID',
    `title` VARCHAR(45) NOT NULL COMMENT '评论标题',
    `content` VARCHAR(300) NOT NULL COMMENT '评论内容',
    `audit_status` TINYINT(1) NOT NULL COMMENT '评论审核状态',
    `audit_time` DATETIME NOT NULL COMMENT '评论时间',
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '商品评论表';

    ```

    用户表

    ```sql
    CREATE TABLE IF NOT EXISTS `mydb`.`t_customer_info` (
        `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
        `gender` CHAR(1) NULL COMMENT '用户性别',
        `customer_name` VARCHAR(20) NOT NULL COMMENT '用户姓名',
        `identity_card_no` VARCHAR(20) NOT NULL COMMENT '用户证件号码',
        `identity_card_type` TINYINT(1) NOT NULL COMMENT '用户证件类型',
        `phone_number` INT UNSIGNED NOT NULL COMMENT '联系电话',
        PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '用户信息表';

    CREATE TABLE IF NOT EXISTS `mydb`.`t_customer_address` (
        `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
        `province` SMALLINT NOT NULL COMMENT '省',
        `city` SMALLINT NOT NULL COMMENT '市',
        `county` SMALLINT NOT NULL COMMENT '县或区',
        `address` VARCHAR(200) NOT NULL COMMENT '详细地址',
        `is_default` TINYINT(1) NOT NULL COMMENT '是否默认，0-否，1-是',
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    COMMENT = '用户地址信息';

    ```    


学习笔记