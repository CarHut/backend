package com.carhut.controllers.auth;

import com.carhut.dtos.TokenDto;
import com.carhut.dtos.UrlDto;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.AuthenticationRequest;
import com.carhut.requests.requestmodels.PasswordResetRequestBody;
import com.carhut.requests.requestmodels.UserDetailsRequestBody;
import com.carhut.security.models.User;
import com.carhut.services.auth.AuthenticationService;
import com.carhut.services.auth.GoogleOAuth2Service;
import com.carhut.services.security.UserCredentialsService;
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
        UrlDto urlDto = googleOAuth2Service.getGoogleAuthUrl();
        if (urlDto != null) {
            controllerLogger.saveToFile("[AuthenticationController][OK]: /getGoogleAuthUrl - Successfully built Google authentication url.");
            return ResponseEntity.ok().body(urlDto);
        } else {
            controllerLogger.saveToFile("[AuthenticationController][WARN]: /getGoogleAuthUrl - Cannot build Google authentication url. Url is null.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getGoogleToken")
    public ResponseEntity<TokenDto> getGoogleToken(@RequestParam("code") String code) {
        TokenDto tokenDto = googleOAuth2Service.getGoogleToken(code, jwtUtil);
        if (tokenDto != null) {
            controllerLogger.saveToFile("[AuthenticationController][OK]: /getGoogleToken - Successfully created Google token.");
            return ResponseEntity.internalServerError().body(tokenDto);
        } else {
            controllerLogger.saveToFile("[AuthenticationController][WARN]: /getGoogleToken - Cannot create Google token. Token is null.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        if (!authenticationService.isAuthenticationValid(request)) {
            controllerLogger.saveToFile("[AuthenticationController][ERROR] - /authenticate - Authentication request is invalid.");
            return ResponseEntity.badRequest().body(null);
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        return userCredentialsService.loadUserByUsername(request.getUsername()) != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userCredentialsService.loadUserByUsername(request.getUsername()), new HashMap<>()))
                : ResponseEntity.badRequest().body("Token was not generated. Bad username.");
    }

    @PostMapping("/getUserDetailsInfo")
    public ResponseEntity<User> getUserDetailsInfo(@RequestBody PrincipalRequest<UserDetailsRequestBody> userDetailsRequestBody) {
        User user = userCredentialsService.getUserDetailsInfo(userDetailsRequestBody);

        if (user != null) {
            controllerLogger.saveToFile("[AuthenticationController][OK]: /getUserDetailsInfo - User credentials were retrieved successfully.");
            return ResponseEntity.ok(user);
        } else {
            controllerLogger.saveToFile("[AuthenticationController][WARN]: /getUserDetailsInfo - User credentials were not found.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/resetPasswordSendEmail")
    public ResponseEntity<String> resetPasswordSendEmail(@RequestBody String email) {
        ServiceStatusEntity serviceStatusEntity = authenticationService.resetPasswordSendEmail(email, userCredentialsService);
        if (serviceStatusEntity == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[AuthenticationController][OK]: /resetPasswordSendEmail - Email with password reset token was successfully sent. Email: " + email);
            return ResponseEntity.ok("Email with password reset token was successfully sent.");
        } else {
            controllerLogger.saveToFile("[AuthenticationController][WARN]: /resetPasswordSendEmail - Email with password reset token wasn't sent. Unknown user with email: " + email);
            return ResponseEntity.badRequest().body("There was internal error while trying to send password reset token. Email: " + email);
        }
    }

    @PostMapping("/resetPasswordInitiate")
    public ResponseEntity<String> resetPasswordInitiate(@RequestBody PasswordResetRequestBody passwordResetRequestBody) {
        ServiceStatusEntity serviceStatusEntity = authenticationService.resetPasswordInitiate(passwordResetRequestBody);
        if (serviceStatusEntity == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[AuthenticationController][OK]: /resetPasswordInitiate - Password reset was initiated.");
            return ResponseEntity.ok("Successfully changed password.");
        } else {
            controllerLogger.saveToFile("[AuthenticationController][WARN]: /resetPasswordInitiate - Password reset wasn't initiated.");
            return ResponseEntity.internalServerError().body("Token expired.");
        }
    }

}
