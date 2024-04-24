package com.carhut.datatransfer.util;

import com.carhut.models.deprecated.AutobazarEUCarObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Deprecated
public class CarIdGenerator {

    @Deprecated
    public static String generateId(AutobazarEUCarObject carObject) {
        String dataToHash = carObject.getHeader() +
                carObject.getPrice() +
                carObject.getDateAdded() +
                carObject.getLink() +
                carObject.getLocation() +
                carObject.getFuel() +
                carObject.getBodywork() +
                carObject.getModelYear() +
                carObject.getTransmission() +
                carObject.getEngineVolume() +
                carObject.getEnginePower() +
                carObject.getMileage() +
                carObject.getPowerTrain() +
                carObject.getVin() +
                carObject.getNote() +
                carObject.getStateOfTheVehicle();

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
