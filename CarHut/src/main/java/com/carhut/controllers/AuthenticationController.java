package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
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
    public ResponseEntity<String> resetPasswordSendEmail(@RequestBody String email) {
        return authenticationService.resetPasswordSendEmail(email, userCredentialsService) == RequestStatusEntity.SUCCESS
                ? ResponseEntity.ok("Email with password reset token was successfully sent.")
                : ResponseEntity.internalServerError().body("There was internal error while trying to send password reset token. Email: " + email);
    }

    @PostMapping("/resetPasswordInitiate")
    public ResponseEntity<String> resetPasswordInitiate(@RequestBody PasswordResetRequestBody passwordResetRequestBody) {
        return authenticationService.resetPasswordInitiate(passwordResetRequestBody) == RequestStatusEntity.SUCCESS
                ? ResponseEntity.ok("Successfully changed password.")
                : ResponseEntity.internalServerError().body("Token expired.");
    }
}
