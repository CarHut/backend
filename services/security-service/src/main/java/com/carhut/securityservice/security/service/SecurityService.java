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
        RawUser rawUser = getUserInformationWithName(userCredentialsDto.username());
        return jwtUtil.generateToken(rawUser, new HashMap<>());
    }

    private RawUser getUserInformationWithName(String username) {
        try {
            return userServiceCaller.getUserCredentialsWithUsername(username);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private RawUser getUserInformationWithUserId(String userId) {
        try {
            return userServiceCaller.getUserCredentialsWithUserId(userId);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isAuthenticated(String userId, String bearerToken) {
        RawUser rawUser = getUserInformationWithUserId(userId);
        return jwtUtil.isTokenValid(bearerToken, rawUser.getUsername());
    }
}
