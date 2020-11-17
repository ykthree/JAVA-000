package learn.spring.bean.config;

import learn.spring.boot.starter.bean.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Student 自动配置类
 *
 * @author ykthree
 * 2020/11/16 16:09
 */
@Configuration
@ConditionalOnClass(Student.class)
public class StudentAutoConfiguration {

    @Bean
    // @ConditionalOnMissingBean(Student.class)
    @ConditionalOnProperty(name = "student.enabled", havingValue = "true", matchIfMissing = true)
    public Student studentAuto() {
        return new Student(103, "ykthree3");
    }

}
