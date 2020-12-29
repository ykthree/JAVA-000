package learn.tcc.exchange.entrance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class})
@ImportResource({"classpath:spring-dubbo.xml"})
public class DubboHmilyEntranceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboHmilyEntranceApplication.class, args);
	}

}
