package com.carhut.util.exceptions.googleoauth2;

public class CannotCreateGoogleOAuth2TokenException extends GoogleOAuth2Exception {
    public CannotCreateGoogleOAuth2TokenException(String errMessage) {
        super(errMessage);
    }
}
