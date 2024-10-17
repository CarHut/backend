package com.carhut.commons.model;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

@Entity
@Table(name = "carhut_cars")
public class CarHutCar {

    @Id
    @Column(name = "id")
    private String id;
    private String sellerId;
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
    private String previousOwners;
    private String energyEffClass;
    private String seats;
    private String doors;
    private String emissionClass;
    @JoinColumn(name = "id", table = "color")
    private String exteriorColorId;
    @JoinColumn(name = "id", table = "color")
    private String interiorColorId;
    private String damageStatus;
    private boolean parkingSensors;
    private boolean parkingCameras;
    private String countryOfOrigin;
    private Date technicalInspectionDate;
    private Date emissionInspectionDate;
    private int[] features;
    private Date dateAdded;
    private boolean isActive;

    public CarHutCar() {}

    public CarHutCar(String sellerId, String sellerAddress, int brandId, int modelId, String header, String price,
                     String mileage, String registration, String enginePower, String engineDisplacement, String fuel,
                     String fuelConsumptionAvg, String fuelConsumptionCity, String fuelConsumptionHighway,
                     String gearbox, String gearboxGears, String bodyType, String powertrain, String description,
                     String baseImgPath, String previousOwners, String energyEffClass, String seats, String doors,
                     String emissionClass, String exteriorColorId, String interiorColorId, String damageStatus,
                     boolean parkingSensors, boolean parkingCameras, String countryOfOrigin, Date technicalInspectionDate,
                     Date emissionInspectionDate, int[] features, Date dateAdded) {
        this.id = generateId(sellerId, sellerAddress, brandId, modelId, header, previousOwners, mileage, registration, enginePower,
                engineDisplacement, fuel, fuelConsumptionAvg, fuelConsumptionCity, fuelConsumptionHighway, gearbox,
                gearboxGears, bodyType, powertrain, description, baseImgPath, previousOwners, energyEffClass, seats,
                doors, emissionClass, exteriorColorId, interiorColorId, damageStatus, parkingSensors, parkingCameras,
                countryOfOrigin, technicalInspectionDate, emissionInspectionDate, features);
        this.sellerId = sellerId;
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
        this.baseImgPath = "car_" + id;
        this.previousOwners = previousOwners;
        this.energyEffClass = energyEffClass;
        this.seats = seats;
        this.doors = doors;
        this.emissionClass = emissionClass;
        this.exteriorColorId = exteriorColorId;
        this.interiorColorId = interiorColorId;
        this.damageStatus = damageStatus;
        this.parkingSensors = parkingSensors;
        this.parkingCameras = parkingCameras;
        this.countryOfOrigin = countryOfOrigin;
        this.technicalInspectionDate = technicalInspectionDate;
        this.emissionInspectionDate = emissionInspectionDate;
        this.features = features;
        this.dateAdded = dateAdded;
        this.isActive = true;
    }

    public CarHutCar(String id, String sellerId, String sellerAddress, int brandId, int modelId, String header,
                     String price, String mileage, String registration, String enginePower, String engineDisplacement,
                     String fuel, String fuelConsumptionAvg, String fuelConsumptionCity, String fuelConsumptionHighway,
                     String gearbox, String gearboxGears, String bodyType, String powertrain, String description,
                     String baseImgPath, String previousOwners, String energyEffClass, String seats, String doors,
                     String emissionClass, String exteriorColorId, String interiorColorId, String damageStatus,
                     boolean parkingSensors, boolean parkingCameras, String countryOfOrigin, Date technicalInspectionDate,
                     Date emissionInspectionDate, int[] features, Date dateAdded) {
        this.id = id;
        this.sellerId = sellerId;
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
        this.baseImgPath = "car_" + id;
        this.previousOwners = previousOwners;
        this.energyEffClass = energyEffClass;
        this.seats = seats;
        this.doors = doors;
        this.emissionClass = emissionClass;
        this.exteriorColorId = exteriorColorId;
        this.interiorColorId = interiorColorId;
        this.damageStatus = damageStatus;
        this.parkingSensors = parkingSensors;
        this.parkingCameras = parkingCameras;
        this.countryOfOrigin = countryOfOrigin;
        this.technicalInspectionDate = technicalInspectionDate;
        this.emissionInspectionDate = emissionInspectionDate;
        this.features = features;
        this.dateAdded = dateAdded;
        this.isActive = true;
    }

    private String generateId(String sellerId, String sellerAddress, int brandId, int modelId, String header,
                              String previousOwners, String mileage, String registration, String enginePower,
                              String engineDisplacement, String fuel, String fuelConsumptionAvg, String fuelConsumptionCity,
                              String fuelConsumptionHighway, String gearbox, String gearboxGears, String bodyType,
                              String powertrain, String description, String baseImgPath, String owners, String energyEffClass,
                              String seats, String doors, String emissionClass, String exteriorColorId, String interiorColorId,
                              String damageStatus, boolean parkingSensors, boolean parkingCameras, String countryOfOrigin,
                              Date technicalInspectionDate, Date emissionInspectionDate, int[] features) {
        String dataToHash = sellerId + sellerAddress + brandId + modelId + header + previousOwners + mileage + registration
                + enginePower + engineDisplacement + fuel + fuelConsumptionAvg + fuelConsumptionCity + fuelConsumptionHighway
                + gearbox + gearboxGears + bodyType + powertrain + description + baseImgPath + owners + energyEffClass + seats + doors + emissionClass
                + exteriorColorId + interiorColorId + damageStatus + parkingSensors + parkingCameras + countryOfOrigin
                + technicalInspectionDate + emissionInspectionDate;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());

            // Convert bytes to hexadecimal format
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
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

    public String getPreviousOwners() {
        return previousOwners;
    }

    public void setPreviousOwners(String previousOwners) {
        this.previousOwners = previousOwners;
    }

    public String getEnergyEffClass() {
        return energyEffClass;
    }

    public void setEnergyEffClass(String energyEffClass) {
        this.energyEffClass = energyEffClass;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getEmissionClass() {
        return emissionClass;
    }

    public void setEmissionClass(String emissionClass) {
        this.emissionClass = emissionClass;
    }

    public String getExteriorColorId() {
        return exteriorColorId;
    }

    public void setExteriorColorId(String exteriorColorId) {
        this.exteriorColorId = exteriorColorId;
    }

    public String getInteriorColorId() {
        return interiorColorId;
    }

    public void setInteriorColorId(String interiorColorId) {
        this.interiorColorId = interiorColorId;
    }

    public String getDamageStatus() {
        return damageStatus;
    }

    public void setDamageStatus(String damageStatus) {
        this.damageStatus = damageStatus;
    }

    public boolean isParkingSensors() {
        return parkingSensors;
    }

    public void setParkingSensors(boolean parkingSensors) {
        this.parkingSensors = parkingSensors;
    }

    public boolean isParkingCameras() {
        return parkingCameras;
    }

    public void setParkingCameras(boolean parkingCameras) {
        this.parkingCameras = parkingCameras;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public Date getTechnicalInspectionDate() {
        return technicalInspectionDate;
    }

    public void setTechnicalInspectionDate(Date technicalInspectionDate) {
        this.technicalInspectionDate = technicalInspectionDate;
    }

    public Date getEmissionInspectionDate() {
        return emissionInspectionDate;
    }

    public void setEmissionInspectionDate(Date emissionInspectionDate) {
        this.emissionInspectionDate = emissionInspectionDate;
    }

    public int[] getFeatures() {
        return features;
    }

    public void setFeatures(int[] features) {
        this.features = features;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
