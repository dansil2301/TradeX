package Eco.TradeX;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TradeXApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeXApplication.class, args);
	}

}
