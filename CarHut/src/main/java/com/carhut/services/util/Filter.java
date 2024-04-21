package com.carhut.services.util;

import com.carhut.temputils.models.TempCarModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Filter {

    public void filterBrands(List<TempCarModel> cars, Integer brand) {
        cars.removeIf(car -> car.getBrandId() != brand);
    }

    public void filterModels(List<TempCarModel> cars, Integer model) {
        cars.removeIf(car -> car.getModelId() != model);
    }

    public void filterPriceFrom(List<TempCarModel> cars, String priceFrom) {
        int priceFromInt = Integer.parseInt(priceFrom);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) < priceFromInt);
    }

    public void filterPriceTo(List<TempCarModel> cars, String priceTo) {
        int priceToInt = Integer.parseInt(priceTo);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) > priceToInt);
    }

    public void filterMileageFrom(List<TempCarModel> cars, String mileageFrom) {
        int mileageFromInt = Integer.parseInt(mileageFrom);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) < mileageFromInt);
    }

    public void filterMileageTo(List<TempCarModel> cars, String mileageTo) {
        int mileageToInt = Integer.parseInt(mileageTo);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) > mileageToInt);
    }

    public void filterFuelType(List<TempCarModel> cars, String fuelType) {
        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    public void filterGearbox(List<TempCarModel> cars, String gearbox) {
        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    public void filterPowertrain(List<TempCarModel> cars, String powertrain) {
        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    public void filterPowerFrom(List<TempCarModel> cars, String powerFrom) {
        int powerFromToInt = Integer.parseInt(powerFrom);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) < powerFromToInt);
    }
    public void filterPowerTo(List<TempCarModel> cars, String powerTo) {
        int powerToToInt = Integer.parseInt(powerTo);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) > powerToToInt);
    }

}
