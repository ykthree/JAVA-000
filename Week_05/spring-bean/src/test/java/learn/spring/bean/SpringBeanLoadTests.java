package learn.spring.bean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spring Bean 装配方式测试类
 *
 * @author ykthree
 * 2020/11/15
 */
@SpringBootTest
public class SpringBeanLoadTests {

    @Autowired
    Student student;

    @Autowired
    Student studentComponent;

    @Autowired
    Student studentBean;

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

    @Test
    public void testLoadBeanByImportResource() {
        // 方式二：在Spring Boot 应用中使用 @ImportResource 注解导入 xml 配置。
        assertNotNull(this.student);
        assertEquals(100, this.student.getId());
        assertEquals("ykthree", this.student.getName());
    }

    @Test
    public void testLoadBeanByAnnotation() {
        // 方式三：使用 @Component/@Service/@Controller/@Repository 注解配置 bean。
        assertNotNull(this.studentComponent);
        assertEquals(0, this.studentComponent.getId());
        assertEquals(null, this.studentComponent.getName());
    }

    @Test
    public void testLoadBeanByBeanAnnotation() {
        // 方式二：使用 @Bean 注解将方法的返回值作为 bean。
        assertNotNull(this.studentBean);
        assertEquals(101, this.studentBean.getId());
        assertEquals("ykthree1", this.studentBean.getName());
    }

}
