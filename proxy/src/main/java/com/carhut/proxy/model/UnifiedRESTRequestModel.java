package com.carhut.proxy.model;

import jakarta.servlet.http.HttpServletRequest;

public final class UnifiedRESTRequestModel implements RequestModel {

    private final HttpServletRequest request;
    private final String body;
    private final String bearerToken;
    private final String contentType;

    public UnifiedRESTRequestModel(HttpServletRequest request, String body, String bearerToken, String contentType) {
        this.request = request;
        this.body = body;
        this.bearerToken = bearerToken;
        this.contentType = contentType;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getBody() {
        return body;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getContentType() {
        return contentType;
    }
}
