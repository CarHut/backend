package com.carhut.securityservice.controller;

import com.carhut.securityservice.security.dto.UserCredentialsDto;
import com.carhut.securityservice.security.model.request.AuthRequest;
import com.carhut.securityservice.security.service.SecurityService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security-service")
public class SecurityController {
    @Autowired
    private SecurityService securityService;

    @PostMapping("/authenticate-with-credentials")
    public ResponseEntity<String> authenticateWithCredentials(@RequestBody UserCredentialsDto userCredentials) {
        if (!securityService.isAuthenticationWithCredentialsValid(userCredentials)) {
            return ResponseEntity.status(400).body(null);
        }

        final String jwtToken = securityService.generateJwtTokenForAuthenticatedUser(userCredentials);
        return jwtToken != null ? ResponseEntity.ok(jwtToken) : ResponseEntity.status(500).body(null);
    }

    @PostMapping("/authenticated")
    public ResponseEntity<Boolean> isAuthenticated(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(securityService.isAuthenticated(authRequest.getUsername(),
                authRequest.getBearerToken()));
    }

    @GetMapping("/test-jwt")
    public ResponseEntity<String> testJwt() {
        return ResponseEntity.ok("Hurray!");
    }
}
