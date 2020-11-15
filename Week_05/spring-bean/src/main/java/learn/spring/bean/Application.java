package learn.spring.bean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

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
