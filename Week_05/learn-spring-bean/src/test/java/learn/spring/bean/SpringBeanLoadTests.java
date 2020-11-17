package learn.spring.bean;

import learn.spring.boot.starter.bean.Klass;
import learn.spring.boot.starter.bean.School;
import learn.spring.boot.starter.bean.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring Bean 装配方式测试类
 *
 * @author ykthree
 * 2020/11/15
 */
@SpringBootTest
public class SpringBeanLoadTests {

    @Autowired
    Student studentXml;

    @Autowired
    Student studentComponent;

    @Autowired
    Student studentBean;

    @Autowired
    Student studentAuto;

    @Autowired
    School school;

    @Test
    public void testLoadBeanByXml() {
        // 方式一：使用 ClassPathXmlApplicationContext 从 xml 配置文件中加载 bean。
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:beans.xml");
        Student studentXml = context.getBean("studentXml", Student.class);
        // xml 配置时没有设置 bean 的 id，在获取 bean 时需要使用 bean 的全路径。
        // Student student = context.getBean("learn.spring.boot.starter.bean.Student", Student.class);

        assertNotNull(studentXml);
        assertEquals(101, studentXml.getId());
        assertEquals("ykthree1", studentXml.getName());
    }

    @Test
    public void testLoadBeanByImportResource() {
        // 方式二：在Spring Boot 应用中使用 @ImportResource 注解导入 xml 配置。
        assertNotNull(this.studentXml);
        assertEquals(101, this.studentXml.getId());
        assertEquals("ykthree1", this.studentXml.getName());
    }

    @Test
    public void testLoadBeanByAnnotation() {
        // 方式三：使用 @Component/@Service/@Controller/@Repository 注解配置 bean。
        assertNotNull(this.studentComponent);
        assertEquals(0, this.studentComponent.getId());
        assertNull(this.studentComponent.getName());
    }

    @Test
    public void testLoadBeanByBeanAnnotation() {
        // 方式四：使用 @Bean 注解将方法的返回值作为 bean。
        assertNotNull(this.studentBean);
        assertEquals(102, this.studentBean.getId());
        assertEquals("ykthree2", this.studentBean.getName());
    }

    @Test
    public void testAutoLoadBean() {
        // 方式五：自动配置 Bean 。
        assertNotNull(this.studentAuto);
        assertEquals(103, this.studentAuto.getId());
        assertEquals("ykthree3", this.studentAuto.getName());
    }

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
