package com.carhut.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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

    public List<String> getCarTypes() {
        return carTypes;
    }

    public void setCarTypes(List<String> carTypes) {
        this.carTypes = carTypes;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getMileageFrom() {
        return mileageFrom;
    }

    public void setMileageFrom(String mileageFrom) {
        this.mileageFrom = mileageFrom;
    }

    public String getMileageTo() {
        return mileageTo;
    }

    public void setMileageTo(String mileageTo) {
        this.mileageTo = mileageTo;
    }

    public String getRegistrationFrom() {
        return registrationFrom;
    }

    public void setRegistrationFrom(String registrationFrom) {
        this.registrationFrom = registrationFrom;
    }

    public String getRegistrationTo() {
        return registrationTo;
    }

    public void setRegistrationTo(String registrationTo) {
        this.registrationTo = registrationTo;
    }

    public String getSeatingConfig() {
        return seatingConfig;
    }

    public void setSeatingConfig(String seatingConfig) {
        this.seatingConfig = seatingConfig;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getPowerFrom() {
        return powerFrom;
    }

    public void setPowerFrom(String powerFrom) {
        this.powerFrom = powerFrom;
    }

    public String getPowerTo() {
        return powerTo;
    }

    public void setPowerTo(String powerTo) {
        this.powerTo = powerTo;
    }

    public String getDisplacementFrom() {
        return displacementFrom;
    }

    public void setDisplacementFrom(String displacementFrom) {
        this.displacementFrom = displacementFrom;
    }

    public String getDisplacementTo() {
        return displacementTo;
    }

    public void setDisplacementTo(String displacementTo) {
        this.displacementTo = displacementTo;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getPowertrain() {
        return powertrain;
    }

    public void setPowertrain(String powertrain) {
        this.powertrain = powertrain;
    }

    public List<ModelsPostModel> getModels() {
        return models;
    }

    public void setModels(List<ModelsPostModel> models) {
        this.models = models;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
