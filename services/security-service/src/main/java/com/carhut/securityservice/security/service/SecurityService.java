package com.carhut.securityservice.security.service;

import com.carhut.securityservice.http.UserServiceCaller;
import com.carhut.securityservice.security.model.RawUser;
import com.carhut.securityservice.security.dto.UserCredentialsDto;
import com.carhut.securityservice.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@Service
public class SecurityService {
    @Autowired
    private JwtUtil jwtUtil;
    private UserServiceCaller userServiceCaller = new UserServiceCaller();

    public SecurityService() {
    }

    public boolean isAuthenticationWithCredentialsValid(final UserCredentialsDto dto) {
        return dto != null && dto.username() != null && dto.password() != null;
    }

    public String generateJwtTokenForAuthenticatedUser(final UserCredentialsDto userCredentialsDto) {
        RawUser rawUser = getUserInformation(userCredentialsDto.username());
        return jwtUtil.generateToken(rawUser, new HashMap<>());
    }

    private RawUser getUserInformation(String username) {
        try {
            return userServiceCaller.getUserCredentials(username);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isAuthenticated(String username, String bearerToken) {
        return jwtUtil.isTokenValid(bearerToken, username);
    }
}
