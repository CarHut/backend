package com.carhut.securityservice.security.model.request;

public class AuthRequest {

    private String username;
    private String bearerToken;

    public AuthRequest(String username, String bearerToken) {
        this.username = username;
        this.bearerToken = bearerToken;
    }

    public AuthRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
