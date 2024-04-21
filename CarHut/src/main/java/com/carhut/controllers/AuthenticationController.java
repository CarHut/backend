package com.carhut.controllers;

import com.carhut.jwt.utils.JwtUtil;
import com.carhut.models.User;
import com.carhut.models.security.AuthenticationRequest;
import com.carhut.models.security.PasswordResetRequestBody;
import com.carhut.services.AuthenticationService;
import com.carhut.services.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return userCredentialsService.loadUserByUsername(request.getUsername()) != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userCredentialsService.loadUserByUsername(request.getUsername()), new HashMap<>()))
                : ResponseEntity.status(400).body("Token was not generated.");
    }

    @PostMapping("/getUserDetailsInfo")
    public ResponseEntity<User> getUserDetailsInfo(@RequestBody String username) {
        return userCredentialsService.getUserDetailsInfo(username) != null
                ? ResponseEntity.ok(userCredentialsService.getUserDetailsInfo(username))
                : ResponseEntity.status(400).body(null);
    }

    @PostMapping("/resetPasswordSendEmail")
    public void resetPasswordSendEmail(@RequestBody String email) {
        authenticationService.resetPasswordSendEmail(email, userCredentialsService);
    }

    @PostMapping("/resetPasswordInitiate")
    public ResponseEntity<String> resetPasswordInitiate(@RequestBody PasswordResetRequestBody passwordResetRequestBody) {
        return authenticationService.resetPasswordInitiate(passwordResetRequestBody)
                ? ResponseEntity.ok("Successfully changed password.")
                : ResponseEntity.internalServerError().body("Token expired.");
    }
}
