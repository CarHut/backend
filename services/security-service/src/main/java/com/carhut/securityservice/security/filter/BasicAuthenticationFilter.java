package com.carhut.securityservice.security.filter;

import com.carhut.securityservice.http.UserServiceCaller;
import com.carhut.securityservice.security.util.JwtUtil;
import com.carhut.securityservice.security.model.SecurityAuthentication;
import com.carhut.securityservice.security.model.RawUser;
import com.carhut.securityservice.security.model.Authority;
import com.carhut.securityservice.repository.AuthorityRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
public class BasicAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private JwtUtil jwtUtil;
    private UserServiceCaller userServiceCaller;

    public BasicAuthenticationFilter() {
        this.userServiceCaller = new UserServiceCaller();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(jwtToken);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final RawUser userCredentials;
            try {
                userCredentials = userServiceCaller.getUserCredentialsWithUsername(username);
            } catch (URISyntaxException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (jwtUtil.isTokenValid(jwtToken, userCredentials.getUsername())) {
                Authority authority = authorityRepository.getAuthorityById(userCredentials.getAuthorityId());
                SecurityContextHolder.getContext().setAuthentication(new SecurityAuthentication(
                        Collections.singleton(authority), null, request, userCredentials,
                        true, userCredentials.getUsername()
                ));
            }
        }
        filterChain.doFilter(request, response);
    }

}
