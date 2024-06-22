package com.carhut.config;

import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.jwt.JwtAuthFilter;
import com.carhut.oauth2.OAuth2GoogleFilter;
import com.carhut.services.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private WebClient userInfoClient;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider = new CarHutAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userCredentialsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public UserCredentialsService userCredentialsService() {
        return new UserCredentialsService(userCredentialsRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(userCredentialsService());
    }

    @Bean
    public OAuth2GoogleFilter oAuth2GoogleFilter() {
        return new OAuth2GoogleFilter();
    }

    @Bean
    public OpaqueTokenIntrospector introspector() {
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }

    @Configuration
    @Order(2)
    public class OAuth2SecurityConfig {

        @Bean
        public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/api/auth/getGoogleAuthUrl", "/api/auth/getGoogleToken").permitAll()
                                    .requestMatchers("/api/auth/getUserDetailsInfo", "/api/auth/resetPassword",
                                            "/api/carhut/savedCars/**", "/api/carhut/getMyListings",
                                            "/api/carhut/removeOffer", "/api/carhut/savedSearches/**",
                                            "/api/carhut/getUserIdByUsername").hasRole("USER")
                                    .anyRequest().authenticated()
                    )
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .oauth2ResourceServer(oauth2ResourceServer ->
                            oauth2ResourceServer.opaqueToken(Customizer.withDefaults())
                    )
                    .addFilterBefore(oAuth2GoogleFilter(), BearerTokenAuthenticationFilter.class);
            return httpSecurity.build();
        }
    }

    @Configuration
    @Order(1)
    public class CarHutSecurityConfig {

        @Bean
        public SecurityFilterChain carHutSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeRequests(authorizeRequests ->
                            authorizeRequests
                                    .requestMatchers("/api/auth/authenticate", "/api/logout/**", "/api/register/**",
                                            "/api/auth/resetPasswordInitiate", "/api/auth/resetPasswordSendEmail",
                                            "/api/carhut/**", "/api/auth/getGoogleAuthUrl", "/api/auth/getGoogleToken").permitAll()
                                    .requestMatchers("/api/auth/getUserDetailsInfo", "/api/auth/resetPassword",
                                            "/api/carhut/savedCars/**", "/api/carhut/getMyListings",
                                            "/api/carhut/removeOffer", "/api/carhut/savedSearches/**",
                                            "/api/carhut/getUserIdByUsername").hasRole("USER")
                                    .anyRequest().authenticated()
                    )
                    .sessionManagement(sessionManagement ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
            return httpSecurity.build();
        }
    }
}