package com.carhut.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String proxyOrigin = "http://localhost:8080/";
    private final String savedCarsServiceOrigin = "http://localhost:8041/";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins(proxyOrigin, savedCarsServiceOrigin);
    }
}
