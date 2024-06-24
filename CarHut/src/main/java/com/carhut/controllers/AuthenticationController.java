package com.carhut.controllers;

import com.carhut.dtos.TokenDto;
import com.carhut.dtos.UrlDto;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.models.requestmodels.AuthenticationRequest;
import com.carhut.models.requestmodels.PasswordResetRequestBody;
import com.carhut.models.requestmodels.UserDetailsRequestBody;
import com.carhut.models.security.*;
import com.carhut.paths.NetworkPaths;
import com.carhut.services.AuthenticationService;
import com.carhut.services.UserCredentialsService;
import com.carhut.util.exceptions.authentication.AuthenticationUserNotFoundException;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.usercredentials.UserCredentialsException;
import com.carhut.util.loggers.ControllerLogger;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Autowired
    private OpaqueTokenIntrospector introspector;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    private final JwtUtil jwtUtil;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getGoogleAuthUrl")
    public ResponseEntity<UrlDto> auth() {
        String url = new GoogleAuthorizationCodeRequestUrl(
                clientId,
                NetworkPaths.publicWebAddress,
                Arrays.asList("email", "profile", "openid")
        ).build();

        return ResponseEntity.ok(new UrlDto(url));
    }

    @GetMapping("/getGoogleToken")
    public ResponseEntity<TokenDto> getGoogleToken(@RequestParam("code") String code) {
        GoogleTokenResponse token;
        try {
            token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    NetworkPaths.publicWebAddress
            ).execute();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean status = authenticationService.createAccountForUserLoggedWithGoogleOAuth2(token.getAccessToken());
        if (status) {
            System.out.println("New account has been created.");
        } else {
            System.out.println("Account already exists.");
        }

        OAuth2AuthenticatedPrincipal principal = introspector.introspect(token.getAccessToken());
        UserDetails userDetails = userCredentialsService.loadUserByUsername((String) principal.getAttributes().get("name"));

        return ResponseEntity.ok(new TokenDto(jwtUtil.generateToken(userDetails, new HashMap<>()), userDetails.getUsername()));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            if (!authenticationService.isAuthenticationValid(request)) {
                return ResponseEntity.status(404).body(null);
            }
        } catch (AuthenticationUserNotFoundException e) {
            return ResponseEntity.status(500).body(null);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return userCredentialsService.loadUserByUsername(request.getUsername()) != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userCredentialsService.loadUserByUsername(request.getUsername()), new HashMap<>()))
                : ResponseEntity.status(400).body("Token was not generated.");
    }

    @PostMapping("/getUserDetailsInfo")
    public ResponseEntity<User> getUserDetailsInfo(@RequestBody UserDetailsRequestBody userDetailsRequestBody) {
        try {
            User user = userCredentialsService.getUserDetailsInfo(userDetailsRequestBody);

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
