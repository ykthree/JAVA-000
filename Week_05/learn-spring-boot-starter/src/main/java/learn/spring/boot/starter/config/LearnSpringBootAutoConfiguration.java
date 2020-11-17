package learn.spring.boot.starter.config;

import learn.spring.boot.starter.bean.Klass;
import learn.spring.boot.starter.bean.School;
import learn.spring.boot.starter.bean.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * 自动配置类
 *
 * @author ykthree
 * 2020/11/16 16:09
 */
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
