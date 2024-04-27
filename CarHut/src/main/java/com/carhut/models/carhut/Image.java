package com.carhut.models.carhut;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "image")
public class Image {

    @Id
    private String id;
    private String path;

    @JoinColumn(name = "id", table = "users")
    private String userId;
    @JoinColumn(name = "id", table = "carhut_cars")
    private String carid;

    public Image() {}

    public Image(String path, String userId, String carid) {
        this.id = generateId(path, userId, carid);
        this.path = path;
        this.userId = userId;
        this.carid = carid;
    }

    public Image(String id, String path, String userId, String carid) {
        this.id = id;
        this.path = path;
        this.userId = userId;
        this.carid = carid;
    }

    private String generateId(String path, String userId, String carId) {
        String dataToHash = path + userId + carId;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            // Convert bytes to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }
}
