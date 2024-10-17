package com.carhut.services.sr.carhutapisr.dto;

import com.carhut.commons.model.ModelsPostModel;

import java.util.List;

public class TypeCorrectCarHutCarFilterDto {

    private Integer brandId;
    private Integer modelId;
    private List<String> carTypes;
    private Integer priceFrom;
    private Integer priceTo;
    private Integer mileageFrom;
    private Integer mileageTo;
    private Integer registrationFrom;
    private Integer registrationTo;
    private String seatingConfig;
    private String doors;
    private String location;
    private String postalCode;
    private String fuelType;
    private Integer powerFrom;
    private Integer powerTo;
    private Integer displacementFrom;
    private Integer displacementTo;
    private String gearbox;
    private String powertrain;
    private List<ModelsPostModel> models;

    public TypeCorrectCarHutCarFilterDto() {}

    public TypeCorrectCarHutCarFilterDto(Integer brandId, Integer modelId, List<String> carTypes, Integer priceFrom,
                                         Integer priceTo, Integer mileageFrom, Integer mileageTo,
                                         Integer registrationFrom, Integer registrationTo, String seatingConfig,
                                         String doors, String location, String postalCode, String fuelType,
                                         Integer powerFrom, Integer powerTo, Integer displacementFrom,
                                         Integer displacementTo, String gearbox, String powertrain,
                                         List<ModelsPostModel> models) {
        this.brandId = brandId;
        this.modelId = modelId;
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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public List<String> getCarTypes() {
        return carTypes;
    }

    public void setCarTypes(List<String> carTypes) {
        this.carTypes = carTypes;
    }

    public Integer getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Integer priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Integer getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Integer priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getMileageFrom() {
        return mileageFrom;
    }

    public void setMileageFrom(Integer mileageFrom) {
        this.mileageFrom = mileageFrom;
    }

    public Integer getMileageTo() {
        return mileageTo;
    }

    public void setMileageTo(Integer mileageTo) {
        this.mileageTo = mileageTo;
    }

    public Integer getRegistrationFrom() {
        return registrationFrom;
    }

    public void setRegistrationFrom(Integer registrationFrom) {
        this.registrationFrom = registrationFrom;
    }

    public Integer getRegistrationTo() {
        return registrationTo;
    }

    public void setRegistrationTo(Integer registrationTo) {
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

    public Integer getPowerFrom() {
        return powerFrom;
    }

    public void setPowerFrom(Integer powerFrom) {
        this.powerFrom = powerFrom;
    }

    public Integer getPowerTo() {
        return powerTo;
    }

    public void setPowerTo(Integer powerTo) {
        this.powerTo = powerTo;
    }

    public Integer getDisplacementFrom() {
        return displacementFrom;
    }

    public void setDisplacementFrom(Integer displacementFrom) {
        this.displacementFrom = displacementFrom;
    }

    public Integer getDisplacementTo() {
        return displacementTo;
    }

    public void setDisplacementTo(Integer displacementTo) {
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
}
