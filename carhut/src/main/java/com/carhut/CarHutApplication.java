package com.carhut;

import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.jwt.JwtAuthFilter;
import com.carhut.services.carhutapi.CarHutAPIService;
import com.carhut.services.security.UserCredentialsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class CarHutApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarHutApplication.class, args);
	}

}
