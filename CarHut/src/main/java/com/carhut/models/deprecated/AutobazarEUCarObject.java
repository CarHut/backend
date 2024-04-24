package com.carhut.models.deprecated;

import com.carhut.datatransfer.util.CarIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Deprecated
@Entity
@Table(name = "autobazar_eu")
public class AutobazarEUCarObject {

    @Id
    private String id;
    private String header;
    private String price;
    private String dateAdded;
    private String link;
    private String location;
    private String fuel;
    private String bodywork;
    private String modelYear;
    private String transmission;
    private String engineVolume;
    private String enginePower;
    private String mileage;
    private String powerTrain;
    private String vin;
    private String note;
    private String stateOfTheVehicle;

    public AutobazarEUCarObject(String header, String price, String dateAdded, String link, String location, String fuel, String bodywork, String modelYear, String transmission, String engineVolume, String enginePower, String mileage, String powerTrain, String vin, String note, String stateOfTheVehicle) {
        this.header = header;
        this.price = price;
        this.dateAdded = dateAdded;
        this.link = link;
        this.location = location;
        this.fuel = fuel;
        this.bodywork = bodywork;
        this.modelYear = modelYear;
        this.transmission = transmission;
        this.engineVolume = engineVolume;
        this.enginePower = enginePower;
        this.mileage = mileage;
        this.powerTrain = powerTrain;
        this.vin = vin;
        this.note = note;
        this.stateOfTheVehicle = stateOfTheVehicle;
        this.id = CarIdGenerator.generateId(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AutobazarEUCarObject() {}

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

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getBodywork() {
        return bodywork;
    }

    public void setBodywork(String bodywork) {
        this.bodywork = bodywork;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(String engineVolume) {
        this.engineVolume = engineVolume;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getPowerTrain() {
        return powerTrain;
    }

    public void setPowerTrain(String powerTrain) {
        this.powerTrain = powerTrain;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStateOfTheVehicle() {
        return stateOfTheVehicle;
    }

    public void setStateOfTheVehicle(String stateOfTheVehicle) {
        this.stateOfTheVehicle = stateOfTheVehicle;
    }
}
