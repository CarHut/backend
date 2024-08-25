package com.carhut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedOrigins("http://localhost:3000",    // web
                                "http://0.0.0.0:3000",      // web
                                "http://192.168.1.12:3000", // web wifi
                                "http://192.168.1.54:3000", // web eth
                                "http://localhost:8081",    // chat
                                "http://0.0.0.0:8081",      // chat
                                "http://192.168.1.12:8081", // chat wifi
                                "http://192.168.1.54:8081", // chat eth
                                "http://localhost",         // default
                                "http://0.0.0.0",           // default
                                "http://192.168.1.12",      // default wifi
                                "http://192.168.1.54"       // default eth
                );
    }
}
