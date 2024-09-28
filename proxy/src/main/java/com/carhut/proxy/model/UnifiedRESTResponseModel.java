package com.carhut.proxy.model;

public final class UnifiedRESTResponseModel implements ResponseModel {

    private final String responseBody;
    private final Integer statusCode;
    private final String message;

    public UnifiedRESTResponseModel(String responseBody, Integer statusCode, String message) {
        this.responseBody = responseBody;
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
