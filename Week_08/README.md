# 作业说明

- Week_08周四(12/03)
  - 作业 2（必做）：设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表，并演示常见的增删改查操作。
  - 作业位置：learn-spring-data-sharding
  - 代码片段

    - 分库分表规则:

        订单 ID % 2，路由到库 mydb_0 或 mydb_1。

        订单 ID % 16，路由到表 t_order_master_0, .... , t_order_master_16。

        |订单编号 | 库 | 表 |
        |----|----|----|
        | 16 | mydb_0 | t_order_master_0  |
        | 2  | mydb_0 | t_order_master_2  |
        | 4  | mydb_0 | t_order_master_4  |
        | 6  | mydb_0 | t_order_master_6  |
        | 8  | mydb_0 | t_order_master_8  |
        | 10 | mydb_0 | t_order_master_10 |
        | 12 | mydb_0 | t_order_master_12 |
        | 14 | mydb_0 | t_order_master_14 |
        |    |        |                   |
        | 1  | mydb_1 | t_order_master_1  |
        | 3  | mydb_1 | t_order_master_3  |
        | 5  | mydb_1 | t_order_master_5  |
        | 7  | mydb_1 | t_order_master_7  |
        | 9  | mydb_1 | t_order_master_9  |
        | 11 | mydb_1 | t_order_master_11 |
        | 13 | mydb_1 | t_order_master_13 |
        | 15 | mydb_1 | t_order_master_15 |

    - 手动创建库和表

        mysql-cli 下执行以下脚本：
        ```
        source learn-spring-data-sharding/sql/mydb_0.sql
        source learn-spring-data-sharding/sql/mydb_1.sql
        ```
    - 添加 sharding-jdbc-spring-boot-starter 依赖
        ```xml
        <dependency>
            <groupId>org.apache.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>4.1.1</version>
        </dependency>
        ```
    - 配置文件
        ```yml
        spring:
          shardingsphere:
            props:
            sql:
                show: true
            datasource:
              names: ds-mydb-0, ds-mydb-1
              ds-mydb-0:
                type: com.zaxxer.hikari.HikariDataSource
                driver-class-name: com.mysql.jdbc.Driver
                jdbc-url: jdbc:mysql://127.0.0.1:3306/mydb_0?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                username: root
                password:
              ds-mydb-1:
                type: com.zaxxer.hikari.HikariDataSource
                driver-class-name: com.mysql.jdbc.Driver
                jdbc-url: jdbc:mysql://127.0.0.1:3326/mydb_1?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                username: root
                password:
            sharding:
              tables:
                # 逻辑表名
                t_order_master:
                  actualDataNodes: ds-mydb-0.t_order_master_$->{[0,2,4,6,8,10,12,14]},ds-mydb-1.t_order_master_$->{[1,3,5,7,9,11,13,15]}
                  keyGenerator:
                    column: id
                    type: SNOWFLAKE
                  databaseStrategy:
                    inline:
                    algorithmExpression: ds-mydb-$->{id % 2}
                    shardingColumn: id
                  tableStrategy:
                    inline:
                    algorithmExpression: t_order_master_$->{id % 16}
                    shardingColumn: id
        ```
    - 测试代码
        ```java
        @Test
        public void testInsert() {
            for (int i = 1; i <= 16; i++) {
                int insert = orderService.insert(buildOrder(i));
            }
        }

        @Test
        public void testSelectById() {
            for (int i = 1; i <= 16; i++) {
                Order order = orderService.selectById(i);
                assertNotNull(order);
                assertEquals(i, order.getId());
            }
        }

        private Order buildOrder(int id) {
            Order order = new Order();
            order.setId(id);
            order.setOrderSn("AAA");
            order.setCustomerId(id);
            order.setOrderStatus((short) 1);
            order.setCreateTime(new Date());
            order.setPayTime(new Date());
            order.setShipTime(new Date());
            order.setReceiveTime(new Date());
            order.setDiscountMoney(new BigDecimal("30"));
            order.setShipMoney(new BigDecimal("0"));
            order.setPayMoney(new BigDecimal("99"));
            order.setPayMethod((short) 1);
            order.setAddress("CHANGSHA");
            order.setReceiveUser("Bob");
            order.setShipSn("BBB");
            order.setShipCompanyName("CCC");
            return order;
        }
        ```
    - 日志输出
        ```
        写日志：

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: INSERT INTO t_order_master_1(id,...) VALUES (?,...) ::: [1,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_2(id,...) VALUES (?,...) ::: [2,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: INSERT INTO t_order_master_3(id,...) VALUES (?,...) ::: [3,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_4(id,...) VALUES (?,...) ::: [4,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: INSERT INTO t_order_master_5(id,...) VALUES (?,...) ::: [5,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_6(id,...) VALUES (?,...) ::: [6,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: INSERT INTO t_order_master_7(id,...) VALUES (?,...) ::: [7,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [8,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [9,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [10,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [11,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [12,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [13,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [14,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [15,...]

        ShardingSphere-SQL                       : Logic SQL: INSERT INTO mydb.t_order_master(id,...) VALUES (?,...)
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: INSERT INTO t_order_master_8(id,...) VALUES (?,...) ::: [16,...]

        读日志：
        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_1 where id = ? ::: [1]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_2 where id = ? ::: [2]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_3 where id = ? ::: [3]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_4 where id = ? ::: [4]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_5 where id = ? ::: [5]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_6 where id = ? ::: [6]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_7 where id = ? ::: [7]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_8 where id = ? ::: [8]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_9 where id = ? ::: [9]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_10 where id = ? ::: [10]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_11 where id = ? ::: [11]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_12 where id = ? ::: [12]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_13 where id = ? ::: [13]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_14 where id = ? ::: [14]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-1 ::: select * from t_order_master_15 where id = ? ::: [15]

        ShardingSphere-SQL                       : Logic SQL: select * from mydb.t_order_master where id = ?
        ShardingSphere-SQL                       : Actual SQL: ds-mydb-0 ::: select * from t_order_master_0 where id = ? ::: [16]
        ```
        从日志可以看出，我们执行的逻辑 SQL 语句，ShardingSphere-JDBC 会根据分片规则转换为请求真实数据库和表的 SQL 脚本执行并返回执行结果。 

        逻辑 SQL 需要包含分片键才能享受数据库分片带来的福利（通过分片键结合分片规则可以定位到具体的库和表），比如查询某条数据 SQL 语句的过滤条件没有以分片键作为过滤条件，则需要查询全部分片再聚合每次查询结果并返回。

- Week_08周六(12/05)
  - 作业 2（必做）：（基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式 事务应用demo（二选一）。
  - learn-spring-data-mutiple-transaction
  - 代码片段

    添加依赖
    ```xml
      <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
        <version>4.1.1</version>
      </dependency>
      <dependency>
        <groupId>org.apache.shardingsphere</groupId>
        <artifactId>sharding-transaction-xa-core</artifactId>
        <version>4.1.1</version>
      </dependency>
    ```
    配置类
    ```java
    @Configuration
    @EnableTransactionManagement
    public class TransactionConfiguration {
        
        /**
        * Create platform transaction manager bean.
        *
        * @param dataSource data source
        * @return platform transaction manager
        */
        @Bean
        public PlatformTransactionManager txManager(final DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
        
        /**
        * Create JDBC template bean.
        *
        * @param dataSource data source
        * @return JDBC template bean
        */
        @Bean
        public JdbcTemplate jdbcTemplate(final DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
    ```
    访问数据库
    ```java
    @Override
    @Transactional(rollbackFor = RollbackException.class)
    @ShardingTransactionType(TransactionType.XA)
    public TransactionType insert(final Order order) {
        mapper.insert(order);
        return TransactionTypeHolder.get();
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void insertThenRollback(final Order order1, final Order order2) throws RollbackException {
        mapper.insert(order1);
        mapper.insert(order2);
        throw new RollbackException("Mock access failed");
    }
    ```
    测试代码
    ```java
    @Test
    public void testInsert() {
        for (int i = 1; i <= 16; i++) {
            TransactionType transactionType = orderService.insert(buildOrder(i));
            orderService.deleteById(i);
            log.info("TransactionType: [{}]", transactionType); // XA
        }
        log.info("end");
    }

    @Test
    public void testInsertThenRollback() {
        Order order1 = buildOrder(1);
        Order order2 = buildOrder(2);
        try {
            orderService.insertThenRollback(order1, order2);
        } catch (RollbackException e) {
            log.error("failed", e);
        }
        Integer integer = orderService.countAllOrders();
        assertEquals(0, integer);
    }
    ```
    XA事务日志：xa_tx2.log
    ```json
    {
      "id": "192.168.229.1.tm160752529201200001",
      "wasCommitted": false,
      "participants": [
          {
              "uri": "192.168.229.1.tm1",
              "state": "TERMINATED",
              "expires": 1607525593107,
              "resourceName": "resource-2-ds-mydb-1"
          },
          {
              "uri": "192.168.229.1.tm2",
              "state": "TERMINATED",
              "expires": 1607525593107,
              "resourceName": "resource-1-ds-mydb-0"
          }
      ]
    }
    ```

# 学习笔记