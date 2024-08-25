package com.carhut.models.carhut;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "carhut_cars")
public class CarHutCar {

    @Id
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
}
