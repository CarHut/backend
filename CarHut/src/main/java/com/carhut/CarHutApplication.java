package com.carhut;

import com.carhut.services.CarHutAPIService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarHutApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarHutApplication.class, args);
	}

	@Bean
	public CarHutAPIService carHutAPIService() {
		return new CarHutAPIService();
	}

}
