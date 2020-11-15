# 作业说明

- Week_05周四(11/12)
  - 作业 2（必做）：写代码实现Spring Bean的装配。
    - 作业位置：
    - 代码片段：
    
      Student类
      ```java
      @ToString
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

          <bean id="student" class="learn.spring.bean.Student">
              <property name="id" value="100"/>
              <property name="name" value="ykthree"/>
          </bean>
      </beans>
      ```
      方法一：使用 ClassPathXmlApplicationContext 从 xml 配置文件中加载 bean。

      ```java
      @SpringBootTest
      public class SpringBeanLoadTests {

        @Test
        public void testLoadBeanByXml() {
            // 方式一：使用 ClassPathXmlApplicationContext 从 xml 配置文件中加载 bean。
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
            Student student = context.getBean("student", Student.class);
            // xml 配置时没有设置 bean 的 id，在获取 bean 时需要使用 bean 的全路径。
            // Student student = context.getBean("learn.spring.bean.Student", Student.class);

            assertNotNull(student);
            assertEquals(100, student.getId());
            assertEquals("ykthree", student.getName());
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
        Student student;

        @Test
        public void testLoadBeanByImportResource() {
            assertNotNull(this.student);
            assertEquals(100, this.student.getId());
            assertEquals("ykthree", this.student.getName());
        }

      }
      ```
      方法三：使用 @Component/@Service/@Controller/@Repository 注解配置 bean。
      ```java
      @ToString
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
            // 方式三：使用 @Component/@Service/@Controller/@Repository 注解配置 bean。
            assertNotNull(this.studentComponent);
            assertEquals(0, this.studentComponent.getId());
            assertEquals(null, this.studentComponent.getName());
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
          return new Student(101, "ykthree1");
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
            // 方式二：使用 @Bean 将方法的返回值作为 bean。
            assertNotNull(this.studentBean);
            assertEquals(101, this.studentBean.getId());
            assertEquals("ykthree1", this.studentBean.getName());
        }
      
      }  
      ```


- Week_05周四(11/14)
  - 作业 3（必做）：给前面课程提供的Student/Klass/School实现自动配置和Starter。
    - 作业位置：
    - 代码片段：
  - 作业 6（必做）：研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法。
    
    - 作业位置：
    - 代码片段：
      1. 使用 JDBC 原生接口，实现数据库的增删改查操作。
      2. 使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
      3. 配置 Hikari 连接池，改进上述操作，提交代码到 Github。

# 学习笔记