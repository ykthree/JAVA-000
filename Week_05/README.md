# 作业说明

- Week_05周四(11/12)
  - 作业 2（必做）：写代码实现Spring Bean的装配。
    - 作业位置：learn-spring-bean
    - 代码片段：
    
      Student类
      ```java
      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public class Student {

          private int id;

          private String name;
      }
      ```
      beans.xml
      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

          <bean id="studentXml" class="learn.spring.boot.starter.bean.Student">
              <property name="id" value="101"/>
              <property name="name" value="ykthree1"/>
          </bean>
      </beans>
      ```
      方法一：使用 ClassPathXmlApplicationContext 从 xml 配置文件中加载 bean。

      ```java
      @SpringBootTest
      public class SpringBeanLoadTests {

        @Test
        public void testLoadBeanByXml() {
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
            Student studentXml = context.getBean("student", Student.class);
            // xml 配置时没有设置 bean 的 id，在获取 bean 时需要使用 bean 的全路径。
            // Student studentXml = context.getBean("learn.spring.boot.starter.bean.Student", Student.class);

            assertNotNull(studentXml);
            assertEquals(101, studentXml.getId());
            assertEquals("ykthree1", studentXml.getName());
          }
      }
      ```
      方法二：在Spring Boot 应用中使用 @ImportResource 注解导入 xml 配置。 
      ```java
      @SpringBootApplication
      @ImportResource("classpath:beans.xml")
      public class Application {

        public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
        }

      }

      @SpringBootTest
      public class SpringBeanLoadTests {

        @Autowired
        Student studentXml;

        @Test
        public void testLoadBeanByImportResource() {
            assertNotNull(this.studentXml);
            assertEquals(101  , this.studentXml.getId());
            assertEquals("ykthree1", this.studentXml.getName());
        }

      }
      ```
      方法三：使用 @Component/@Service/@Controller/@Repository 注解配置 bean。
      ```java
      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      @Component("studentComponent")
      //@Service("studentComponent")
      //@Repository("studentComponent")
      //@Controller("studentComponent")
      public class Student {

          private int id;

          private String name;
      }

      @SpringBootTest
      public class SpringBeanLoadTests {

        @Autowired
        Student studentComponent;

        @Test
        public void testLoadBeanByAnnotation() {
            assertNotNull(this.studentComponent);
            assertEquals(0, this.studentComponent.getId());
            assertNull(this.studentComponent.getName());
        }

      }   
      ```
      方法四：使用 @Bean 注解将方法的返回值作为 bean。
      ```java
      @SpringBootApplication
      @ImportResource("classpath:beans.xml")
      public class Application {

        @Bean
        public Student studentBean() {
          return new Student(102, "ykthree2");
        }

        public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
        }

      }

      @SpringBootTest
      public class SpringBeanLoadTests {

        @Autowired
        Student studentBean;

        @Test
        public void testLoadBeanByBeanAnnotation() {
            assertNotNull(this.studentBean);
            assertEquals(102, this.studentBean.getId());
            assertEquals("ykthree2", this.studentBean.getName());
        }
      
      }  
      ```
      方法五：使用Spring Boot的自动配置（@Conditional）
      ```java
      /**
       * Student 自动配置类
       */
      @Configuration
      @ConditionalOnClass(Student.class)
      public class StudentAutoConfiguration {

          @Bean
          // @ConditionalOnMissingBean(Student.class)
          @ConditionalOnProperty(name = "student.enabled", havingValue = "true", matchIfMissing = true)
          public Student studentAuto() {
              return new Student(102, "ykthree2");
          }

      }

      @SpringBootTest
      public class SpringBeanLoadTests {

          @Autowired
          Student studentAuto;

          @Test
          public void testAutoLoadBean() {
              assertNotNull(this.studentAuto);
              assertEquals(103, this.studentAuto.getId());
              assertEquals("ykthree3", this.studentAuto.getName());
          }
      }
      ```
      resources/application.yml
      ```yml
      student:
        enabled: true
      ```
      resources/META-INF/spring.factories
      ```properties
      org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
      learn.spring.bean.StudentAutoConfiguration
      ```

- Week_05周四(11/14)
  - 作业 3（必做）：给前面课程提供的Student/Klass/School实现自动配置和Starter。
    - 作业位置：learn-spring-boot-starter
    - 代码片段：
    
      实体类
      ```java
      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class School {

          Klass klass;

          Student student;

      }

      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public class Klass { 
          
          List<Student> students;

      }

      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      public class Student {

          private int id;

          private String name;
      }
      ```
      自动配置类
      ```java
      @Configuration
      @ConditionalOnProperty(name = "student.enabled", havingValue = "true", matchIfMissing = true)
      public class LearnSpringBootAutoConfiguration {

          @Bean
          @ConditionalOnClass(School.class)
          @ConditionalOnMissingBean(School.class)
          public School school(Klass klass, Student student) {
              return new School(klass, student);
          }

          @Bean
          @ConditionalOnClass(Klass.class)
          @ConditionalOnMissingBean(Klass.class)
          @ConditionalOnProperty(prefix = "school", name = "klass.enabled", havingValue = "true", matchIfMissing = true)
          public Klass klass(Student student) {
              return new Klass(Collections.singletonList(student));
          }

          @Bean
          @ConditionalOnClass(Student.class)
          @ConditionalOnMissingBean(Student.class)
          @ConditionalOnProperty(prefix = "school.klass", name = "student.enabled", havingValue = "true", matchIfMissing = true)
          public Student student() {
              return new Student(100, "ykthree");
          }

      }
      ```
      resources/application.yml
      ```yml
      school:
        enabled: true
        klass:
          enabled: true
          student:
            enabled: true
      ```
      resources/META-INF/spring.factories
      ```properties
      org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
      learn.spring.boot.starter.config.LearnSpringBootAutoConfiguration
      ```
      引入依赖
      ```xml
      <dependency>
        <groupId>learn-spring</groupId>
        <artifactId>learn-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
      </dependency>
      ```
      测试类
      ```java
      @SpringBootTest
      public class SpringBeanLoadTests {

          @Autowired
          School school;

          @Test
          public void testLoadByBeanByStarter() {
              // 测试从 starter 中获取自动配置的 bean
              assertNotNull(this.school);
              Klass klass = this.school.getKlass();
              assertNotNull(klass);
              List<Student> students = klass.getStudents();
              assertNotNull(students);
              assertNotEquals(0, students.size());
              Student student = students.get(0);
              assertNotNull(student);
              assertEquals(100, student.getId());
              assertEquals("ykthree", student.getName());
          }

      }
      ```

    - 作业 6（必做）：研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法。
      
      - 作业位置：learn-spring-data-jdbc
      - 代码片段：
        1. 使用 JDBC 原生接口，实现数据库的增删改查操作（learn.spring.data.jdbc.JdbcDemo）。
        ```java
        public Foo selectById(int id) {
          Foo foo = null;
          // 查询语句
          String selectSql = "SELECT ID, NAME FROM FOO WHERE ID = ?";
          try (
                  Connection connection = DriverManager.getConnection(url, username, password);
                  PreparedStatement prepareStatement = connection.prepareStatement(selectSql);
          ) {
              prepareStatement.setInt(1, id);
              try (ResultSet resultSet = prepareStatement.executeQuery()) {
                  while (resultSet.next()) {
                      foo = new Foo();
                      foo.setId(resultSet.getInt(1));
                      foo.setName(resultSet.getString(2));
                  }
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
          return foo;
        }

        public void deleteById(int id) {
            // 删除语句
            String deleteSql = "DELETE FROM FOO WHERE ID = ?";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement prepareStatement = connection.prepareStatement(deleteSql);
            ) {
                prepareStatement.setInt(1, id);
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void insert(Foo foo) {
            if (foo == null) {
                return;
            }
            // 新增语句
            String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
            ) {
                prepareStatement.setInt(1, foo.getId());
                prepareStatement.setString(2, foo.getName());
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void update(Foo foo) {
            if (foo == null) {
                return;
            }
            // 更新语句
            String updateSql = "UPDATE FOO SET NAME = ? WHERE ID = ?";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement prepareStatement = connection.prepareStatement(updateSql);
            ) {
                prepareStatement.setString(1, foo.getName());
                prepareStatement.setInt(2, foo.getId());
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ```
        2. 使用事务，PrepareStatement 方式，批处理方式，改进上述操作（learn.spring.data.jdbc.JdbcDemo）。
        ```java
        public void batchInsert(List<Foo> fooList) {
            if (fooList == null || fooList.isEmpty()) {
                return;
            }
            // 新增语句
            String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
            ) {
                // 关闭自动提交
                connection.setAutoCommit(false);
                for (Foo foo : fooList) {
                    prepareStatement.setInt(1, foo.getId());
                    prepareStatement.setString(2, foo.getName());
                    prepareStatement.addBatch();
                }
                prepareStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void batchInsertWithTransaction(List<Foo> fooList) {
            if (fooList == null || fooList.isEmpty()) {
                return;
            }
            // 新增语句
            String insertSql = "INSERT INTO FOO(ID, NAME) VALUES(?, ?)";
            try (
                    Connection connection = DriverManager.getConnection(url, username, password);
                    PreparedStatement prepareStatement = connection.prepareStatement(insertSql);
            ) {
                // 关闭自动提交
                connection.setAutoCommit(false);
                try {
                    for (Foo foo : fooList) {
                        prepareStatement.setInt(1, foo.getId());
                        prepareStatement.setString(2, foo.getName());
                        prepareStatement.addBatch();
                    }
                    prepareStatement.executeBatch();
                    connection.commit();
                } catch (SQLException e) {
                    log.error("数据新增错误", e);
                    connection.rollback();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ```
        3. 配置 Hikari 连接池，改进上述操作（learn.spring.data.jdbc.HikariJdbcDemo）。
   
        连接改为从HikariCP连接池中获取，其余代码和上述一样。需要注意的是，使用从 HikariCP 获取的连接{@link HikariProxyConnection}，调用其{@link ProxyConnection#close()}方法不会直接关闭
        连接，而是将连接清理并重新放回连接池。

# 学习笔记