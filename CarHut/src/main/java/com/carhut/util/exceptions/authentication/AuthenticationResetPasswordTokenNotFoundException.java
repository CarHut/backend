package com.carhut.util.exceptions.authentication;

public class AuthenticationResetPasswordTokenNotFoundException extends CarHutAuthenticationException {
    public AuthenticationResetPasswordTokenNotFoundException(String errMessage) {
        super(errMessage);
    }
}
