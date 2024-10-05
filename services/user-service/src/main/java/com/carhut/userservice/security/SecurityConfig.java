package com.carhut.userservice.security;

import com.carhut.userservice.service.UserServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserServiceProvider userServiceProvider() {
        return new UserServiceProvider();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(requestAuthorization -> {
                    requestAuthorization.anyRequest().permitAll();
                })
                .authenticationProvider(securityAuthenticationProvider())
                .addFilter(new SecurityAuthenticationFilter());

        return httpSecurity.build();
    }

    @Bean
    public SecurityAuthenticationProvider securityAuthenticationProvider() {
        final SecurityAuthenticationProvider provider = new SecurityAuthenticationProvider();
        provider.setUserDetailsService(userServiceProvider());
        return provider;
    }

}
