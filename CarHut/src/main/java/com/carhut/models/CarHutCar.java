package com.carhut.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "carhut_users")
public class CarHutCar {

    @Id
    private String id;
    @JoinColumn(table = "users", name = "id")
    private String userId;
    private String sellerAddress;
    @JoinColumn(table = "brand", name = "id")
    private int brandId;
    @JoinColumn(table = "model", name = "id")
    private int modelId;
    private String header;
    private String price;
    private String mileage;
    private String registration;
    private String enginePower;
    private String engineDisplacement;
    private String fuel;
    private String fuelConsumptionAvg;
    private String fuelConsumptionCity;
    private String fuelConsumptionHighway;
    private String gearbox;
    private String gearboxGears;
    private String bodyType;
    private String powertrain;
    private String description;
    private String baseImgPath;

    public CarHutCar() {}

    public CarHutCar(String id, String userId, String sellerAddress, int brandId, int modelId, String header,
                     String price, String mileage, String registration, String enginePower, String engineDisplacement,
                     String fuel, String fuelConsumptionAvg, String fuelConsumptionCity, String fuelConsumptionHighway,
                     String gearbox, String gearboxGears, String bodyType, String powertrain, String description,
                     String baseImgPath) {
        this.id = id;
        this.userId = userId;
        this.sellerAddress = sellerAddress;
        this.brandId = brandId;
        this.modelId = modelId;
        this.header = header;
        this.price = price;
        this.mileage = mileage;
        this.registration = registration;
        this.enginePower = enginePower;
        this.engineDisplacement = engineDisplacement;
        this.fuel = fuel;
        this.fuelConsumptionAvg = fuelConsumptionAvg;
        this.fuelConsumptionCity = fuelConsumptionCity;
        this.fuelConsumptionHighway = fuelConsumptionHighway;
        this.gearbox = gearbox;
        this.gearboxGears = gearboxGears;
        this.bodyType = bodyType;
        this.powertrain = powertrain;
        this.description = description;
        this.baseImgPath = baseImgPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getEngineDisplacement() {
        return engineDisplacement;
    }

    public void setEngineDisplacement(String engineDisplacement) {
        this.engineDisplacement = engineDisplacement;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getFuelConsumptionAvg() {
        return fuelConsumptionAvg;
    }

    public void setFuelConsumptionAvg(String fuelConsumptionAvg) {
        this.fuelConsumptionAvg = fuelConsumptionAvg;
    }

    public String getFuelConsumptionCity() {
        return fuelConsumptionCity;
    }

    public void setFuelConsumptionCity(String fuelConsumptionCity) {
        this.fuelConsumptionCity = fuelConsumptionCity;
    }

    public String getFuelConsumptionHighway() {
        return fuelConsumptionHighway;
    }

    public void setFuelConsumptionHighway(String fuelConsumptionHighway) {
        this.fuelConsumptionHighway = fuelConsumptionHighway;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getGearboxGears() {
        return gearboxGears;
    }

    public void setGearboxGears(String gearboxGears) {
        this.gearboxGears = gearboxGears;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getPowertrain() {
        return powertrain;
    }

    public void setPowertrain(String powertrain) {
        this.powertrain = powertrain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseImgPath() {
        return baseImgPath;
    }

    public void setBaseImgPath(String baseImgPath) {
        this.baseImgPath = baseImgPath;
    }
}
