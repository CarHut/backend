package com.carhut.services.auth;

import com.carhut.dtos.TokenDto;
import com.carhut.dtos.UrlDto;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.jwt.utils.JwtUtil;
import com.carhut.paths.NetworkPaths;
import com.carhut.services.security.UserCredentialsService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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

    public UrlDto getGoogleAuthUrl() {
        try {
            String url = new GoogleAuthorizationCodeRequestUrl(
                    clientId,
                    NetworkPaths.publicWebAddress,
                    Arrays.asList("email", "profile", "openid")
            ).build();

            return new UrlDto(url);
        } catch (Exception e) {
            return null;
        }
    }

    public TokenDto getGoogleToken(String code, JwtUtil jwtUtil) {
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
            return null;
        }

        try {
            ServiceStatusEntity status = authenticationService.createAccountForUserLoggedWithGoogleOAuth2(token.getAccessToken());
            if (status == ServiceStatusEntity.SUCCESS) {
                System.out.println("New account has been created.");
            } else {
                System.out.println("Account already exists.");
            }
        }
        catch (Exception e) {
            return null;
        }

        OAuth2AuthenticatedPrincipal principal;
        try {
            principal = introspector.introspect(token.getAccessToken());
        } catch (Exception e) {
            return null;
        }

        UserDetails userDetails = userCredentialsService.loadUserByUsername((String) principal.getAttributes().get("name"));
        return new TokenDto(jwtUtil.generateToken(userDetails, new HashMap<>()), userDetails.getUsername());
    }
}
