package com.carhut.models;

import com.carhut.temputils.models.TempCarModel;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "saved_cars_by_users")
public class SavedCarByUser {

    @Id
    private String id;

    @JoinColumn(table = "users", name = "id")
    private String userId;

    @JoinColumn(table = "tmp_car_list", name = "id")
    private String carId;

    public SavedCarByUser() {}

    public SavedCarByUser(String id, String userId, String carId) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public static String generateId(String userId, String carId) {
        String combinedId = userId + carId;
        return hashString(combinedId);
    }

    private static String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}

