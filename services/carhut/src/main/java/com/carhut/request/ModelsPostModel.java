package com.carhut.request;

public class ModelsPostModel {

    private String brand;
    private String model;

    public ModelsPostModel(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public ModelsPostModel() {}

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
