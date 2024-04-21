package com.carhut.temputils.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tmp_car_list")
public class TempCarModel {

    @Id
    private String id;
    private String mileage;
    private String registration;
    private String enginePower;
    private String fuel;
    private String bodyType;
    private String gearbox;
    private String powertrain;
    private String fuelConsumption;
    private String sellerName;
    private String sellerAddress;
    private String price;
    private String imagePath;
    private int brandId;
    private int modelId;
    private String header;

    public TempCarModel(String id, String mileage, String registration, String enginePower, String fuel, String bodyType, String gearbox, String powertrain, String fuelConsumption, String sellerName, String sellerAddress, String price, String imagePath, int brandId, int modelId, String header) {
        this.id = id;
        this.mileage = mileage;
        this.registration = registration;
        this.enginePower = enginePower;
        this.fuel = fuel;
        this.bodyType = bodyType;
        this.gearbox = gearbox;
        this.powertrain = powertrain;
        this.fuelConsumption = fuelConsumption;
        this.sellerName = sellerName;
        this.sellerAddress = sellerAddress;
        this.price = price;
        this.imagePath = imagePath;
        this.brandId = brandId;
        this.modelId = modelId;
        this.header = header;
    }

    public TempCarModel() {}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
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

    public String getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(String fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
}
