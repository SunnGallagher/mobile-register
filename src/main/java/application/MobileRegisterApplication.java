package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"application", "service", "model"})
public class MobileRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileRegisterApplication.class, args);
	}

}
