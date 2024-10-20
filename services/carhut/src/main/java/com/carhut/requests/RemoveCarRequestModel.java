package com.carhut.requests;

public class RemoveCarRequestModel {
    private String carId;
    private String userId;

    public RemoveCarRequestModel() {}

    public RemoveCarRequestModel(String carId, String userId) {
        this.carId = carId;
        this.userId = userId;
    }

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
