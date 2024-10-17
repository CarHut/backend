package com.carhut.services.sr.carhutapisr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins("http://localhost:8001")
                .allowedOrigins("http://localhost:8002")
                .allowedOrigins("http://localhost:8003")
                .allowedOrigins("http://localhost:8004")
                .allowedOrigins("http://localhost:8005")
                .allowedOrigins("http://localhost:8006")
                .allowedOrigins("http://localhost:8007")
                .allowedOrigins("http://localhost:8008")
                .allowedOrigins("http://localhost:8009");
    }
}
