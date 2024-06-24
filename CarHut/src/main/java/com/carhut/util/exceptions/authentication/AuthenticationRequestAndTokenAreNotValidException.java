package com.carhut.util.exceptions.authentication;

public class AuthenticationRequestAndTokenAreNotValidException extends CarHutAuthenticationException {
    public AuthenticationRequestAndTokenAreNotValidException(String errMessage) {
        super(errMessage);
    }
}
