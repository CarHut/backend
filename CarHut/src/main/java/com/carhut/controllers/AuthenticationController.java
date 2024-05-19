package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.models.security.RegisterUserBody;
import com.carhut.models.security.User;
import com.carhut.models.security.AuthenticationRequest;
import com.carhut.models.security.PasswordResetRequestBody;
import com.carhut.services.AuthenticationService;
import com.carhut.services.UserCredentialsService;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.usercredentials.UserCredentialsException;
import com.carhut.util.loggers.ControllerLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.Control;
import java.util.HashMap;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    private final JwtUtil jwtUtil;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        // TO-DO CHECK IF USER HAS ACTIVATED ACCOUNT

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return userCredentialsService.loadUserByUsername(request.getUsername()) != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userCredentialsService.loadUserByUsername(request.getUsername()), new HashMap<>()))
                : ResponseEntity.status(400).body("Token was not generated.");
    }

    @PostMapping("/getUserDetailsInfo")
    public ResponseEntity<User> getUserDetailsInfo(@RequestBody String username) {
        try {
            User user = userCredentialsService.getUserDetailsInfo(username);
            if (user != null) {
                controllerLogger.saveToFile("[AuthenticationController][OK]: /getUserDetailsInfo - User credentials were retrieved successfully.");
                return ResponseEntity.ok(user);
            } else {
                controllerLogger.saveToFile("[AuthenticationController][WARN]: /getUserDetailsInfo - User credentials were not found.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (UserCredentialsException e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /getUserDetailsInfo - Internal error while getting user credentials. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/resetPasswordSendEmail")
    public ResponseEntity<String> resetPasswordSendEmail(@RequestBody String email) {
        try {
            RequestStatusEntity requestStatusEntity = authenticationService.resetPasswordSendEmail(email, userCredentialsService);
            if (requestStatusEntity == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[AuthenticationController][OK]: /resetPasswordSendEmail - Email with password reset token was successfully sent. Email: " + email);
                return ResponseEntity.ok("Email with password reset token was successfully sent.");
            } else {
                controllerLogger.saveToFile("[AuthenticationController][WARN]: /resetPasswordSendEmail - Email with password reset token wasn't sent. Unknown user with email: " + email);
                return ResponseEntity.internalServerError().body("There was internal error while trying to send password reset token. Email: " + email);
            }
        } catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /resetPasswordSendEmail - Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body("There was internal error while trying to send password reset token. Email: " + email);
        }
    }

    @PostMapping("/resetPasswordInitiate")
    public ResponseEntity<String> resetPasswordInitiate(@RequestBody PasswordResetRequestBody passwordResetRequestBody) {
        try {
            RequestStatusEntity requestStatusEntity = authenticationService.resetPasswordInitiate(passwordResetRequestBody);
            if (requestStatusEntity == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[AuthenticationController][OK]: /resetPasswordInitiate - Password reset was initiated.");
                return ResponseEntity.ok("Successfully changed password.");
            } else {
                controllerLogger.saveToFile("[AuthenticationController][WARN]: /resetPasswordInitiate - Password reset wasn't initiated.");
                return ResponseEntity.internalServerError().body("Token expired.");
            }
        }
        catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /resetPasswordInitiate - Password reset wasn't initiated. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body("There was internal error while trying initiate password reset.");
        }
    }

}
