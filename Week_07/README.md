# 作业说明

- Week_06周六(11/20)
  - 作业 2（必做）：按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。
  - 作业位置：learn-spring-data-mutiple-datasource
  - 代码片段

    使用 JdbcTemplate 批量新增数据。
    ```java
    private String insertSql = "INSERT INTO `mydb`.`t_order_master`\n" +
                "(`order_sn`,\n" +
                "`customer_id`,\n" +
                "`order_status`,\n" +
                "`create_time`,\n" +
                "`ship_time`,\n" +
                "`pay_time`,\n" +
                "`receive_time`,\n" +
                "`discount_money`,\n" +
                "`ship_money`,\n" +
                "`pay_money`,\n" +
                "`pay_method`,\n" +
                "`address`,\n" +
                "`receive_user`,\n" +
                "`ship_sn`,\n" +
                "`ship_company_name`)\n" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Override
        public int[] batchInsert(final List<Order> orders) {
            int[] insertCounts;
            insertCounts = jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Order order = orders.get(i);

                    ps.setString(1, order.getOrderSn());
                    ps.setInt(2, order.getCustomerId());
                    ps.setShort(3, order.getOrderStatus());
                    ps.setDate(4, new java.sql.Date(order.getCreateTime().getTime()));
                    ps.setDate(5, new java.sql.Date(order.getShipTime().getTime()));
                    ps.setDate(6, new java.sql.Date(order.getPayTime().getTime()));
                    ps.setDate(7, new java.sql.Date(order.getReceiveTime().getTime()));
                    ps.setBigDecimal(8, order.getDiscountMoney());
                    ps.setBigDecimal(9, order.getShipMoney());
                    ps.setBigDecimal(10, order.getPayMoney());
                    ps.setShort(11, order.getPayMethod());
                    ps.setString(12, order.getAddress());
                    ps.setString(13, order.getReceiveUser());
                    ps.setString(14, order.getShipSn());
                    ps.setString(15, order.getShipCompanyName());
                }

                @Override
                public int getBatchSize() {
                    return orders.size();
                }
            });
            return insertCounts;
        }
    ```
    测试代码，生成 1,000,000 订单数据耗时 1348613 毫秒，约 23 分钟。
    ```java
    @Test
    public void testBatchInsert() {
        // 模拟批量插入 1000,000 订单数据
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            List<Order> orders = new ArrayList<>(1000);
            for (int j = 1; j <= 1000; j++) {
                orders.add(buildOrder());
            }
            // 批量插入，每次 1000 条
            int[] ints = orderService.batchInsert(orders);
            assertNotNull(ints);
            log.info("{}", Arrays.toString(ints));
        }
        // [1348613]ms 23 分钟
        log.info("订单生成结束，耗时：[{}]ms", System.currentTimeMillis() - start);
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setOrderSn("AAA");
        order.setCustomerId(1);
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

- Week_05周四(11/12)
  - 作业 2（必做）：读写分离-动态切换数据源版本 1.0。
    - 作业位置：learn-spring-data-mutiple-datasource




  - 作业 3（）：读写分离-数据库框架版本 2.0。
    - 作业位置：learn-spring-data-mutiple-datasource

# 学习笔记