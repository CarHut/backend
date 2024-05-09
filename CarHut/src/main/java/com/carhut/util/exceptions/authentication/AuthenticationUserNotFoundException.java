package com.carhut.util.exceptions.authentication;

public class AuthenticationUserNotFoundException extends CarHutAuthenticationException {

    public AuthenticationUserNotFoundException(String errMessage) {
        super(errMessage);
    }

}
