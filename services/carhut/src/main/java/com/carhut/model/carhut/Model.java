package com.carhut.model.carhut;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "model")
public class Model {

    @Id
    private int id;

    private String model;

    private int brand_id;

    public Model() {}

    public Model(int id, String model, int brand_id) {
        this.id = id;
        this.model = model;
        this.brand_id = brand_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }
}
