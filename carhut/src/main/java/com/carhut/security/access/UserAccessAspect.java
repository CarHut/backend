package com.carhut.security.access;

import com.carhut.database.repository.security.UserCredentialsRepository;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.PrincipalRequestBody;
import com.carhut.security.models.AuthenticationPrincipal;
import com.carhut.security.models.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("production")
public class UserAccessAspect {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Before("@annotation(com.carhut.security.annotations.UserAccessCheck)")
    public void checkUserAccess(JoinPoint joinPoint) throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Extract the principal from the Spring Security context
        User userSecurityContextHolder = (User) authentication.getPrincipal();
        String usernameFromSecurityContext = userSecurityContextHolder.getUsername();

        // Extract the username from the method arguments
        Object[] args = joinPoint.getArgs();
        String usernameFromRequest = null;
        String usernameFromRequestBody = null;
        for (Object arg : args) {
            if (arg instanceof PrincipalRequest<?> principalRequest) {
                AuthenticationPrincipal authPrincipal = principalRequest.getAuthenticationPrincipal();
                if (authPrincipal != null) {
                    usernameFromRequest = authPrincipal.getUsername();
                }

                PrincipalRequestBody principalRequestBody = (PrincipalRequestBody) principalRequest.getDto();

                if (principalRequestBody != null) {
                    usernameFromRequestBody = principalRequestBody.getUsername();
                } else {
                    throw new RuntimeException("Request body for principal is invalid.");
                }
            } else if (arg instanceof String) { // Request body contains only string
                usernameFromRequestBody = (String) arg;
            }
        }

        if (usernameFromRequest == null || usernameFromRequestBody == null) {
            throw new RuntimeException("Username not found in method arguments");
        }

        if (!usernameFromRequest.equals(usernameFromRequestBody)) {
            throw new RuntimeException("Usernames do not match. User cannot access other users data.");
        }

        System.out.println(usernameFromSecurityContext);
        System.out.println(usernameFromRequest);

        if (!usernameFromSecurityContext.equals(usernameFromRequest)) {
            throw new RuntimeException("User is not authorized to access this data");
        }
    }

}
