package com.carhut.securityservice.security;

import com.carhut.securityservice.security.filter.BasicAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private BasicAuthenticationFilter basicAuthenticationFilter;

    @Configuration
    public class BasicCredentialsAuthentication {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{
            security
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorizationRequests -> {
                        authorizationRequests.requestMatchers("security-service/authenticate-with-credentials").permitAll();
                        authorizationRequests.requestMatchers("security-service/test-jwt").authenticated();
                        authorizationRequests.requestMatchers("security-service/authenticated").authenticated();
                    })
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .addFilterAfter(basicAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return security.build();
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
