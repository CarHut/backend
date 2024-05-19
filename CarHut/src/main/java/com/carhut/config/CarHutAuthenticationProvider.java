package com.carhut.config;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CarHutAuthenticationProvider extends DaoAuthenticationProvider {

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (!userDetails.isEnabled()) {
            throw new DisabledException("User is not active");
        }
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
