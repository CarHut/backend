package com.carhut.util.exceptions.googleoauth2;

public class CannotBuildGoogleOAuth2UrlException extends GoogleOAuth2Exception {
    public CannotBuildGoogleOAuth2UrlException(String errMessage) {
        super(errMessage);
    }
}
