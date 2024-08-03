package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelsPostModel {

    private String brand;
    private String model;

    public ModelsPostModel(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public ModelsPostModel() {}
}
