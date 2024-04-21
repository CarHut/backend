package com.carhut.models;

import com.carhut.temputils.models.TempCarModel;
import jakarta.persistence.*;

@Entity
@Table(name = "saved_cars_by_users")
public class SavedCarByUser {

    @Id
    private int id;

    @JoinColumn(table = "users", name = "id")
    private String userId;

    @JoinColumn(table = "tmp_car_list", name = "id")
    private String carId;

    public SavedCarByUser() {}

    public SavedCarByUser(int id, String userId, String carId) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

