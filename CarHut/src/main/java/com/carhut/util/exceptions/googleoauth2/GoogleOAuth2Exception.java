package com.carhut.util.exceptions.googleoauth2;

import com.carhut.services.GoogleOAuth2Service;

public class GoogleOAuth2Exception extends Exception {
    public GoogleOAuth2Exception(String errMessage) {
        super(errMessage);
    }
}
