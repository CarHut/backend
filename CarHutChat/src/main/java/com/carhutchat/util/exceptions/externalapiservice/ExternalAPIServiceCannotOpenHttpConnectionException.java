package com.carhutchat.util.exceptions.externalapiservice;

public class ExternalAPIServiceCannotOpenHttpConnectionException extends ExternalAPIServiceException {
    public ExternalAPIServiceCannotOpenHttpConnectionException(String errMessage) {
        super(errMessage);
    }
}
