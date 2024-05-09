package com.carhut.util.exceptions.authentication;

public class AuthenticationCanNotDeleteTokenException extends CarHutAuthenticationException {
    public AuthenticationCanNotDeleteTokenException(String errMessage) {
        super(errMessage);
    }
}
