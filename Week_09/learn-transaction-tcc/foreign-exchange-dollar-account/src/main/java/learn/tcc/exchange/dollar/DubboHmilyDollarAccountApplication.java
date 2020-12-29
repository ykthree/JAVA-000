package learn.tcc.exchange.dollar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:spring-dubbo.xml"})
@MapperScan("learn.tcc.exchange.api.mapper")
public class DubboHmilyDollarAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboHmilyDollarAccountApplication.class, args);
	}

}
