package com.carhut.controllers;

import com.carhut.dtos.TokenDto;
import com.carhut.dtos.UrlDto;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.AuthenticationRequest;
import com.carhut.requests.requestmodels.PasswordResetRequestBody;
import com.carhut.requests.requestmodels.UserDetailsRequestBody;
import com.carhut.security.models.User;
import com.carhut.services.AuthenticationService;
import com.carhut.services.GoogleOAuth2Service;
import com.carhut.services.UserCredentialsService;
import com.carhut.util.exceptions.authentication.AuthenticationUserNotFoundException;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.googleoauth2.GoogleOAuth2Exception;
import com.carhut.util.exceptions.usercredentials.UserCredentialsException;
import com.carhut.util.loggers.ControllerLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private GoogleOAuth2Service googleOAuth2Service;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    private final JwtUtil jwtUtil;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getGoogleAuthUrl")
    public ResponseEntity<UrlDto> auth() {
        try {
            UrlDto urlDto = googleOAuth2Service.getGoogleAuthUrl();
            if (urlDto != null) {
                controllerLogger.saveToFile("[AuthenticationController][OK]: /getGoogleAuthUrl - Successfully built Google authentication url.");
                return ResponseEntity.ok().body(urlDto);
            } else {
                controllerLogger.saveToFile("[AuthenticationController][WARN]: /getGoogleAuthUrl - Cannot build Google authentication url. Url is null.");
                return ResponseEntity.status(404).body(null);
            }
        } catch (GoogleOAuth2Exception e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /getGoogleAuthUrl - Internal error. Error message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getGoogleToken")
    public ResponseEntity<TokenDto> getGoogleToken(@RequestParam("code") String code) {
        try {
            TokenDto tokenDto = googleOAuth2Service.getGoogleToken(code, jwtUtil);

            if (tokenDto != null) {
                controllerLogger.saveToFile("[AuthenticationController][OK]: /getGoogleToken - Successfully created Google token.");
                return ResponseEntity.internalServerError().body(tokenDto);
            } else {
                controllerLogger.saveToFile("[AuthenticationController][WARN]: /getGoogleToken - Cannot create Google token. Token is null.");
                return ResponseEntity.internalServerError().body(null);
            }
        } catch (GoogleOAuth2Exception e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /getGoogleToken - Internal error. Error message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            if (!authenticationService.isAuthenticationValid(request)) {
                controllerLogger.saveToFile("[AuthenticationController][ERROR] - /authenticate - Authentication request is invalid.");
                return ResponseEntity.status(404).body(null);
            }
        } catch (AuthenticationUserNotFoundException e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR] - /authenticate - Authentication request is invalid. User was not found.");
            return ResponseEntity.status(500).body(null);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return userCredentialsService.loadUserByUsername(request.getUsername()) != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userCredentialsService.loadUserByUsername(request.getUsername()), new HashMap<>()))
                : ResponseEntity.status(400).body("Token was not generated.");
    }

    @PostMapping("/getUserDetailsInfo")
    public ResponseEntity<User> getUserDetailsInfo(@RequestBody PrincipalRequest<UserDetailsRequestBody> userDetailsRequestBody) {
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
        } catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR]: /getUserDetailsInfo - Unauthorized access. Message: " + e.getMessage());
            return ResponseEntity.status(403).body(null);
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
