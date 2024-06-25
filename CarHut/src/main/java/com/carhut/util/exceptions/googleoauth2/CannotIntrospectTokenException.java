package com.carhut.util.exceptions.googleoauth2;

public class CannotIntrospectTokenException extends GoogleOAuth2Exception {
    public CannotIntrospectTokenException(String errMessage) {
        super(errMessage);
    }
}
