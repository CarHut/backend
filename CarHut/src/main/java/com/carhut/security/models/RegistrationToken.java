package com.carhut.security.models;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "registration_token")
public class RegistrationToken {

    @Id
    private String id;
    private String token;
    private Date expiryDate;
    private String userId;

    public RegistrationToken() {}

    public RegistrationToken(String userId) {
        this.token = generateToken();
        this.expiryDate = new Date(System.currentTimeMillis());
        this.id = generateRandomId();
        this.userId = userId;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
