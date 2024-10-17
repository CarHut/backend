package com.carhut.services.sr.carhutapisr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = { "com.carhut.commons.*", "com.carhut.services.sr.carhutapisr.*" })
@EntityScan(value = "com.carhut.commons.*")
public class CarHutApiSrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarHutApiSrApplication.class, args);
	}

}
