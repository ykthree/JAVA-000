# 作业说明 

- Week_09周六(12/12)
  - 作业 3（必做）：改造自定义RPC的程序。
  - 作业位置：learn-rpc-custom


- Week_09周日(12/13)
  - 作业 3（必做）：结合 dubbo + hmily，实现一个 TCC 外汇交易处理。 
  - 作业位置：learn-transaction-tcc
  - 需求分析:
    1. 正常交易具体流程如下：
       1. 交易服务中，用户 1 欲使用其美元账户下的 1 美元换取用户 2 人民币账户下的 7 块钱。
       2. 美元账户服务将用户 1 美元余额减 1，将用户 2 美元余额加 1，人名币账户服务将用户 2 的人名币余额减 7， 将用户 1 人名币余额加 7。
    2. 服务设计：
       - 外汇交易服务（exchange-dubbo）：接受用户发起的外汇交易。
       - 美元账户服务（dollar-account-dubbo）：处理美元的增删改查。
       - 人名币账户服务（cny-account-dubbo）：处理人名币的增删改查。
    3. 表设计：
       - 美元账户表（dollar_account.sql）：
      
          ```sql
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
          ```

       - 人名币账户表（cny_account.sql）：

          ```sql
          CREATE TABLE IF NOT EXISTS `cny_account`.`cny_account` (
          `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
          `user_id` INT UNSIGNED NOT NULL COMMENT '用户 ID',
          `balance` DECIMAL(10,0) NOT NULL COMMENT '人名币余额',
          `decrease_freeze` DECIMAL(10,0) NOT NULL COMMENT '冻结金额，扣款暂存余额',
          `increase_freeze` DECIMAL(10,0) NOT NULL COMMENT '冻结金额，转账暂存余额',
          `create_time` DATETIME NOT NULL COMMENT '创建时间',
          `update_time` DATETIME NULL COMMENT '更新时间',
          PRIMARY KEY (`id`))
          ENGINE = InnoDB
          COMMENT = '人名币账户表';
          ```


# 学习笔记