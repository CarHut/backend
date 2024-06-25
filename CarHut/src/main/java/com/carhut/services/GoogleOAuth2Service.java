package com.carhut.services;

import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.dtos.TokenDto;
import com.carhut.dtos.UrlDto;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.paths.NetworkPaths;
import com.carhut.util.exceptions.googleoauth2.CannotBuildGoogleOAuth2UrlException;
import com.carhut.util.exceptions.googleoauth2.CannotCreateGoogleOAuth2TokenException;
import com.carhut.util.exceptions.googleoauth2.CannotIntrospectTokenException;
import com.carhut.util.exceptions.googleoauth2.CreateNewGoogleAccountException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class GoogleOAuth2Service {


    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private OpaqueTokenIntrospector introspector;
    @Autowired
    private UserCredentialsService userCredentialsService;

    public UrlDto getGoogleAuthUrl() throws CannotBuildGoogleOAuth2UrlException {
        try {
            String url = new GoogleAuthorizationCodeRequestUrl(
                    clientId,
                    NetworkPaths.publicWebAddress,
                    Arrays.asList("email", "profile", "openid")
            ).build();

            return new UrlDto(url);
        } catch (Exception e) {
            throw new CannotBuildGoogleOAuth2UrlException("Cannot build Google OAuth2 url. Error message: " + e.getMessage());
        }
    }

    public TokenDto getGoogleToken(String code, JwtUtil jwtUtil) throws CannotCreateGoogleOAuth2TokenException, CreateNewGoogleAccountException, CannotIntrospectTokenException, UsernameNotFoundException {
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
        } catch (Exception e) {
            throw new CannotCreateGoogleOAuth2TokenException("Cannot fetch Google OAuth2 response token. Error message: " + e.getMessage());
        }

        try {
            boolean status = authenticationService.createAccountForUserLoggedWithGoogleOAuth2(token.getAccessToken());
            if (status) {
                System.out.println("New account has been created.");
            } else {
                System.out.println("Account already exists.");
            }
        }
        catch (Exception e) {
            throw new CreateNewGoogleAccountException("Error occurred while creating/checking google account in database. Error message: " + e.getMessage());
        }

        OAuth2AuthenticatedPrincipal principal;
        try {
            principal = introspector.introspect(token.getAccessToken());
        } catch (Exception e) {
            throw new CannotIntrospectTokenException("Cannot inspect Google OAuth2 response token: " + token.getAccessToken() + ". Error message: " + e.getMessage());
        }

        UserDetails userDetails = userCredentialsService.loadUserByUsername((String) principal.getAttributes().get("name"));

        return new TokenDto(jwtUtil.generateToken(userDetails, new HashMap<>()), userDetails.getUsername());
    }
}
