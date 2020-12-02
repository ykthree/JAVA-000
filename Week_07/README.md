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
    - 代码片段：

        配置文件：

        ```yml
        spring:
          datasource:
            hikari:
              primary:
                jdbc-url: jdbc:mysql://127.0.0.1:3306/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                driver-class-name: com.mysql.jdbc.Driver
                username: root
                password:
              secondary:
                jdbc-url: jdbc:mysql://127.0.0.1:3316/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                driver-class-name: com.mysql.jdbc.Driver
                username: root
                password:
        ```

        动态数据源配置
        ```java
        public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

            @Override
            protected Object determineCurrentLookupKey() {
                String dsFlag = DynamicDSContextHolder.peek();
                log.debug("当前数据源标识：[{}]", dsFlag);
                return dsFlag;
            }

        }

        @Configuration
        public class DataSourceConfig {

            @Bean("primaryDataSource")
            @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
            public DataSource primaryDataSource() {
                return DataSourceBuilder.create().build();
            }

            @Bean("secondaryDataSource")
            @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
            public DataSource secondaryDataSource() {
                return DataSourceBuilder.create().build();
            }

            /**
            * Create specify map of target DataSources and configure DataSource
            *
            * @return DataSource
            */
            @Bean("dynamicRoutingDataSource")
            public DataSource dynamicRoutingDataSource() {
                DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
                Map<Object, Object> dataSourceMap = new HashMap<>(2);
                dataSourceMap.put(DataSourceType.PRIMARY, primaryDataSource());
                dataSourceMap.put(DataSourceType.SECONDARY, secondaryDataSource());
                // 将 master 数据源作为默认指定的数据源
                dynamicRoutingDataSource.setDefaultTargetDataSource(primaryDataSource());
                // 设置指定数据源映射
                dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
                return dynamicRoutingDataSource;
            }

        }
        ```
        自定义注解（用于拦截并切换数据源）

        ```java
        @Target({ElementType.TYPE, ElementType.METHOD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        public @interface DynamicDS {

            /**
            * 数据源标识
            *
            * @return 数据源标识
            */
            String value();
        }
        ```
        动态数据源拦截器，用来拦截带有自定义注解的方法，负责设置和清除数据源标识（标识存放于线程本地变量）。
        ```java
        @Slf4j
        public class DynamicDSAnnotationInterceptor implements MethodInterceptor {

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                try {
                    String dsFlag = invocation.getMethod().getAnnotation(DynamicDS.class).value();
                    DynamicDSContextHolder.push(dsFlag);
                    return invocation.proceed();
                } finally {
                    if (DynamicDSContextHolder.empty()) {
                        DynamicDSContextHolder.remove();
                    } else {
                        DynamicDSContextHolder.poll();
                    }
                }
            }

        }

        /**
        * 动态数据源拦截器配置类，配置切点和拦截器
        */
        @Configuration
        public class DynamicDSInterceptorConfig {

            @Bean
            public DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor() {
                return new DynamicDSAnnotationInterceptor();
            }

            @Bean
            public Advisor dynamicDSAnnotationAdvisor(DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor) {
                // 定义注解切点
                AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, DynamicDS.class);
                DefaultPointcutAdvisor dynamicDSAnnotationAdvisor = new DefaultPointcutAdvisor();
                dynamicDSAnnotationAdvisor.setPointcut(pointcut);
                dynamicDSAnnotationAdvisor.setAdvice(dynamicDSAnnotationInterceptor);
                return dynamicDSAnnotationAdvisor;
            }
        }
        ```
        数据源标识线程本地变量，主要用来设置和清除本地线程数据源标识变量（注意要确保 remove 方法被调用，防止内存泄漏）。本地变量使用了栈存放数据源标识，为了避免嵌套调用带有动态数据源注解的方法标识中途被清除问题。
        ```java
        /**
         * 线程本地数据源标识变量设置器，参考了 dynamic datasource 的实现。
         *
         * @see <a href  = "https://github.com/baomidou/dynamic-datasource-spring-boot-starter">dynamic datasource</a>
         */
        public final class DynamicDSContextHolder {

            private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<String>>("dynamic-datasource") {

                @Override
                protected Deque<String> initialValue() {
                    return new ArrayDeque<>();
                }
            };

            private DynamicDSContextHolder() {
            }

            public static String peek() {
                return LOOKUP_KEY_HOLDER.get().peek();
            }

            public static void push(String ds) {
                Deque<String> flags = LOOKUP_KEY_HOLDER.get();
                if (flags == null) {
                    LOOKUP_KEY_HOLDER.set(new ArrayDeque<>());
                } else {
                    flags.push(ds);
                }
            }

            public static void poll() {
                Deque<String> deque = LOOKUP_KEY_HOLDER.get();
                deque.poll();
                if (deque.isEmpty()) {
                    LOOKUP_KEY_HOLDER.remove();
                }
            }

            public static void remove() {
                LOOKUP_KEY_HOLDER.remove();
            }

            public static boolean empty() {
                return LOOKUP_KEY_HOLDER.get().isEmpty();
            }

        }
        ```
        测试方法，在方法前添加自定义注解。观察日志，查看 SQL 语句执行前的数据源标识。
        ```java
        // 方法位于 OrderMapper
        @DynamicDS(DataSourceType.PRIMARY)
        public int insert(Order order) {
            return jdbcTemplate.update(insertSql, ps -> setOrderValues(ps, order));
        }

        // 方法位于 OrderMapper
        @DynamicDS(DataSourceType.SECONDARY)
        public Order selectById(int id) {
            String selectSql = "select * from mydb.t_order_master where id = ?";
            return jdbcTemplate.queryForObject(selectSql, this::getOrderValues, id);
        }

        // 方法位于 OrderServiceTest
        @Test
        public void testInsert() {
            int insert = orderService.insert(buildOrder());
            Order order = orderService.selectById(1);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
        ```
        日志输出：
        ```
        Current DataSource: [primary]
        Executing prepared SQL update
        Executing prepared SQL statement [INSERT INTO mydb.t_order_master ...

        Current DataSource: [secondary]
        Executing prepared SQL query
        Executing prepared SQL statement [select * from mydb.t_order_master where id = ?]
        ```
         从日志可以看出，插入数据访问的是主库（primary），查询语句访问的是从库（secondary）。

  - 作业 3（必做）：读写分离-数据库框架版本 2.0。
    - 作业位置：learn-spring-data-mutiple-datasource
    - 代码片段

        引入依赖
        ```xml
        <dependency>
			<groupId>org.apache.shardingsphere</groupId>
			<artifactId>sharding-jdbc-spring-boot-starter</artifactId>
			<version>4.1.1</version>
		</dependency>
        ```
        配置文件
        ```yml
        spring:
          shardingsphere:
            props:
              sql:
                show: true # 5.x.x 之前日志打印配置
            datasource:
              names: primary, secondary
              primary:
                type: com.zaxxer.hikari.HikariDataSource
                driver-class-name: com.mysql.jdbc.Driver
                jdbc-url: jdbc:mysql://127.0.0.1:3306/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                username: root
                password:
              secondary:
                type: com.zaxxer.hikari.HikariDataSource
                driver-class-name: com.mysql.jdbc.Driver
                jdbc-url: jdbc:mysql://127.0.0.1:3316/mydb?useSSL=false&useUnicode=true&characterEncoding=UTF-8
                username: root
                password:
            masterslave:
              name: ms
              master-data-source-name: primary
              slave-data-source-names: secondary
        ```
        测试方法，观察日志，查看 SQL 语句执行前的数据源标识。
        ```java
        @Test
        public void testInsert() {
            int insert = orderService.insert(buildOrder());
            Order order = orderService.selectById(1);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
        ```
        日志输出：
        ```
        ShardingSphere-SQL                       : Actual SQL: primary ::: INSERT INTO mydb.t_order_master ...
       
        ShardingSphere-SQL                       : Actual SQL: secondary ::: select * from mydb.t_order_master where id = ?
        ```
        从日志可以看出，插入数据访问的是主库（primary），查询语句访问的是从库（secondary）。

# 学习笔记