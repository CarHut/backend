package com.carhut.util.exceptions.authentication;

public class AuthenticationCanNotCreateTokenException extends CarHutAuthenticationException {
    public AuthenticationCanNotCreateTokenException(String errMessage) {
        super(errMessage);
    }
}
