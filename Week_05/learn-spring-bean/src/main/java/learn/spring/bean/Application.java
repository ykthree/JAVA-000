package learn.spring.bean;

import learn.spring.boot.starter.bean.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
@ComponentScan("learn.spring.boot.starter")
public class Application {

	@Bean
	public Student studentBean() {
		return new Student(102, "ykthree2");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
