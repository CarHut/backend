package com.carhut.services.util;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.temputils.models.TempCarModel;

import java.util.List;

public class Filter {

    @Deprecated
    public void filterBrands(List<TempCarModel> cars, Integer brand) {
        cars.removeIf(car -> car.getBrandId() != brand);
    }

    @Deprecated
    public void filterModels(List<TempCarModel> cars, Integer model) {
        cars.removeIf(car -> car.getModelId() != model);
    }

    @Deprecated
    public void filterPriceFrom(List<TempCarModel> cars, String priceFrom) {
        int priceFromInt = Integer.parseInt(priceFrom);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) < priceFromInt);
    }

    @Deprecated
    public void filterPriceTo(List<TempCarModel> cars, String priceTo) {
        int priceToInt = Integer.parseInt(priceTo);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) > priceToInt);
    }

    @Deprecated
    public void filterMileageFrom(List<TempCarModel> cars, String mileageFrom) {
        int mileageFromInt = Integer.parseInt(mileageFrom);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) < mileageFromInt);
    }

    @Deprecated
    public void filterMileageTo(List<TempCarModel> cars, String mileageTo) {
        int mileageToInt = Integer.parseInt(mileageTo);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) > mileageToInt);
    }

    @Deprecated
    public void filterFuelType(List<TempCarModel> cars, String fuelType) {
        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    @Deprecated
    public void filterGearbox(List<TempCarModel> cars, String gearbox) {
        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    @Deprecated
    public void filterPowertrain(List<TempCarModel> cars, String powertrain) {
        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    @Deprecated
    public void filterPowerFrom(List<TempCarModel> cars, String powerFrom) {
        int powerFromToInt = Integer.parseInt(powerFrom);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) < powerFromToInt);
    }
    @Deprecated
    public void filterPowerTo(List<TempCarModel> cars, String powerTo) {
        int powerToToInt = Integer.parseInt(powerTo);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) > powerToToInt);
    }



    public void filterCarBrands(List<CarHutCar> cars, Integer brand) {
        cars.removeIf(car -> car.getBrandId() != brand);
    }

    public void filterCarModels(List<CarHutCar> cars, Integer model) {
        cars.removeIf(car -> car.getModelId() != model);
    }

    public void filterCarPriceFrom(List<CarHutCar> cars, String priceFrom) {
        int priceFromInt = Integer.parseInt(priceFrom);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) < priceFromInt);
    }

    public void filterCarPriceTo(List<CarHutCar> cars, String priceTo) {
        int priceToInt = Integer.parseInt(priceTo);
        cars.removeIf(car -> Integer.parseInt(car.getPrice().replaceAll("€", "")) > priceToInt);
    }

    public void filterCarMileageFrom(List<CarHutCar> cars, String mileageFrom) {
        int mileageFromInt = Integer.parseInt(mileageFrom);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) < mileageFromInt);
    }

    public void filterCarMileageTo(List<CarHutCar> cars, String mileageTo) {
        int mileageToInt = Integer.parseInt(mileageTo);
        cars.removeIf(car -> Integer.parseInt(car.getMileage().replaceAll("[\\s\\u00A0]|km|neuvedené", "").isEmpty() ? "0" : car.getMileage().replaceAll("[\\s\\u00A0]|km", "")) > mileageToInt);
    }

    public void filterCarFuelType(List<CarHutCar> cars, String fuelType) {
        cars.removeIf(car -> !car.getFuel().equals(fuelType));
    }

    public void filterCarGearbox(List<CarHutCar> cars, String gearbox) {
        cars.removeIf(car -> !car.getGearbox().equals(gearbox));
    }

    public void filterCarPowertrain(List<CarHutCar> cars, String powertrain) {
        cars.removeIf(car -> !car.getPowertrain().equals(powertrain));
    }

    public void filterCarPowerFrom(List<CarHutCar> cars, String powerFrom) {
        int powerFromToInt = Integer.parseInt(powerFrom);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) < powerFromToInt);
    }
    public void filterCarPowerTo(List<CarHutCar> cars, String powerTo) {
        int powerToToInt = Integer.parseInt(powerTo);
        cars.removeIf(car -> Double.valueOf(car.getEnginePower().replaceAll("kW|NotStated", "").isEmpty() ? "0" : car.getEnginePower().replaceAll("kW", "")) > powerToToInt);
    }

}
