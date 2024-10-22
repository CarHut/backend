package com.carhut.savedcarsservice.requests;

public class SaveCarRequestModel {
    private String carId;
    private String userId;

    public SaveCarRequestModel() {}

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
