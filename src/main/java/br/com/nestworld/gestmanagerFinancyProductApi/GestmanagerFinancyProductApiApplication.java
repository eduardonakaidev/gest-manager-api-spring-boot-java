package br.com.nestworld.gestmanagerFinancyProductApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("br.com.nestworld.gestmanagerFinancyProductApi")
public class GestmanagerFinancyProductApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestmanagerFinancyProductApiApplication.class, args);
	}

}
