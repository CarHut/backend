package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CarHutCarFilterModel {

    private String brand;
    private String model;
    private List<String> carTypes;
    private String priceFrom;
    private String priceTo;
    private String mileageFrom;
    private String mileageTo;
    private String registrationFrom;
    private String registrationTo;
    private String seatingConfig;
    private String doors;
    private String location;
    private String postalCode;
    private String fuelType;
    private String powerFrom;
    private String powerTo;
    private String displacementFrom;
    private String displacementTo;
    private String gearbox;
    private String powertrain;
    private List<ModelsPostModel> models;

    public CarHutCarFilterModel() {}

    public CarHutCarFilterModel(String brand, String model, List<String> carTypes, String priceFrom, String priceTo, String mileageFrom, String mileageTo, String registrationFrom, String registrationTo, String seatingConfig, String doors, String location, String postalCode, String fuelType, String powerFrom, String powerTo, String displacementFrom, String displacementTo, String gearbox, String powertrain, List<ModelsPostModel> models) {
        this.brand = brand;
        this.model = model;
        this.carTypes = carTypes;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.mileageFrom = mileageFrom;
        this.mileageTo = mileageTo;
        this.registrationFrom = registrationFrom;
        this.registrationTo = registrationTo;
        this.seatingConfig = seatingConfig;
        this.doors = doors;
        this.location = location;
        this.postalCode = postalCode;
        this.fuelType = fuelType;
        this.powerFrom = powerFrom;
        this.powerTo = powerTo;
        this.displacementFrom = displacementFrom;
        this.displacementTo = displacementTo;
        this.gearbox = gearbox;
        this.powertrain = powertrain;
        this.models = models;
    }

}
