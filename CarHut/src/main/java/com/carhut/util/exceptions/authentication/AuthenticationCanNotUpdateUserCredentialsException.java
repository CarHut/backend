package com.carhut.util.exceptions.authentication;

public class AuthenticationCanNotUpdateUserCredentialsException extends CarHutAuthenticationException {
    public AuthenticationCanNotUpdateUserCredentialsException(String errMessage) {
        super(errMessage);
    }
}
