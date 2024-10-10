package com.carhut.securityservice.security.model.request;

public class AuthRequest {

    private String userId;
    private String bearerToken;

    public AuthRequest(String userId, String bearerToken) {
        this.userId = userId;
        this.bearerToken = bearerToken;
    }

    public AuthRequest() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
