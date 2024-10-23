package com.carhut.securityservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String proxyOrigin = "http://localhost:8080";
    private final String carHutApiOrigin = "http://localhost:8001";
    private final String imageServiceOrigin = "http://localhost:8011";
    private final String ratingServiceOrigin = "http://localhost:8031";
    private final String savedCarsServiceOrigin = "http://localhost:8041";
    private final String savedSearchesServiceOrigin = "http://localhost:8051";
    private final String userServiceOrigin = "http://localhost:8100";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins(proxyOrigin,
                        carHutApiOrigin,
                        imageServiceOrigin,
                        ratingServiceOrigin,
                        savedCarsServiceOrigin,
                        savedSearchesServiceOrigin,
                        userServiceOrigin);
    }
}
