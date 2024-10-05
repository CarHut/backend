package com.carhut.services.utils;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.services.util.Filter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class FilterTests {

    private Filter filter = new Filter();

    @Test // Checking whether the program will crash
    void filterCarBrands_ListIsNull() {
        filter.filterCarBrands(null, 3);
    }

    @Test // Checking whether the program will crash
    void filterCarBrands_BrandIntegerIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarBrands(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarBrands_ListIsEmptyButNotNull_IntegerIsValid_ArrayListShouldBeEmpty() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer brandId = 1;

        filter.filterCarBrands(cars, brandId);

        Assertions.assertEquals(0, cars.size());
    }

    @Test
    void filterCarBrands_ListIsNotEmpty_IntegerIsValid_ArrayShouldBeReduced() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer brandId = 2;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setBrandId(2);
        carHutCar2.setBrandId(2);
        carHutCar3.setBrandId(1);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarBrands(cars, brandId);

        Assertions.assertEquals(2, cars.size());

        for (CarHutCar carHutCar : cars) {
            Assertions.assertEquals(brandId, carHutCar.getBrandId());
        }
    }

    @Test
    void filterCarBrands_ListIsNotEmpty_IntegerIsValid_NoCarsShouldBeRemoved() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer brandId = 2;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setBrandId(2);
        carHutCar2.setBrandId(2);
        carHutCar3.setBrandId(2);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarBrands(cars, brandId);

        Assertions.assertEquals(3, cars.size());

        for (CarHutCar carHutCar : cars) {
            Assertions.assertEquals(brandId, carHutCar.getBrandId());
        }
    }

    @Test
    void filterCarBrands_ListIsNotEmpty_IntegerIsValid_NoCarsShouldRemain() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer brandId = 3;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setBrandId(2);
        carHutCar2.setBrandId(2);
        carHutCar3.setBrandId(2);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarBrands(cars, brandId);

        Assertions.assertEquals(0, cars.size());
    }

    @Test // Checking whether the program will crash
    void filterCarModels_ListIsNull() {
        filter.filterCarModels(null, 3);
    }

    @Test
    void filterCarModels_ModelIntegerIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarModels(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarModels_ListIsEmptyButNotNull_IntegerIsValid_ArrayListShouldBeEmpty() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer modelId = 1;
        filter.filterCarModels(cars, modelId);
        Assertions.assertEquals(0, cars.size());
    }

    @Test
    void filterCarModels_ListIsNotEmpty_IntegerIsValid_ArrayShouldBeReduced() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer modelId = 2;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setModelId(2);
        carHutCar2.setModelId(2);
        carHutCar3.setModelId(1);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarModels(cars, modelId);

        Assertions.assertEquals(2, cars.size());

        for (CarHutCar carHutCar : cars) {
            Assertions.assertEquals(modelId, carHutCar.getModelId());
        }
    }

    @Test
    void filterCarModels_ListIsNotEmpty_IntegerIsValid_NoCarsShouldBeRemoved() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer modelId = 2;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setModelId(2);
        carHutCar2.setModelId(2);
        carHutCar3.setModelId(2);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarModels(cars, modelId);

        Assertions.assertEquals(3, cars.size());

        for (CarHutCar carHutCar : cars) {
            Assertions.assertEquals(modelId, carHutCar.getModelId());
        }
    }

    @Test
    void filterCarModels_ListIsNotEmpty_IntegerIsValid_NoCarsShouldRemain() {
        List<CarHutCar> cars = new ArrayList<>();
        Integer modelId = 3;

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setModelId(2);
        carHutCar2.setModelId(2);
        carHutCar3.setModelId(2);

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarModels(cars, modelId);

        Assertions.assertEquals(0, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNull() {
        filter.filterCarPriceTo(null, "1000");
    }

    @Test
    void filterCarPriceTo_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarPriceTo(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNotNull_StringValueIsValidWithoutEuroSign_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999");
        carHutCar2.setPrice("2001");
        carHutCar3.setPrice("100000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNotNull_StringValueIsValidWithEuroSign_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("2001€");
        carHutCar3.setPrice("100000€");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNotNullAndContainsDifferentTypesOfPriceValues_StringValueIsValidWithEuroSign_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("Not stated");
        carHutCar3.setPrice("10000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNotNull_StringValueIsPriceMore_ArrayShouldBeTheSameSize() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "priceMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("Not stated");
        carHutCar3.setPrice("199999");
        carHutCar4.setPrice("1000000€");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarPriceTo(cars, value);

        Assertions.assertEquals(4, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNull() {
        filter.filterCarPriceFrom(null, "1000");
    }

    @Test
    void filterCarPriceFrom_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarPriceFrom(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNotNull_StringValueIsValidWithoutEuroSign_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999");
        carHutCar2.setPrice("2001");
        carHutCar3.setPrice("100000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNotNull_StringValueIsValidWithEuroSign_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("2001€");
        carHutCar3.setPrice("100000€");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNotNullAndContainsDifferentTypesOfPriceValues_StringValueIsValidWithEuroSign_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("Not stated");
        carHutCar3.setPrice("10000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPriceFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNotNull_StringValueIsPriceMore_ShouldReturnOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "priceMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setPrice("1999€");
        carHutCar2.setPrice("Not stated");
        carHutCar3.setPrice("199999");
        carHutCar4.setPrice("1000000€");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarPriceFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageFrom_ListIsNull() {
        filter.filterCarMileageFrom(null, "1000");
    }

    @Test
    void filterCarMileageFrom_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarMileageFrom(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageFrom_ListIsNotNull_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999");
        carHutCar2.setMileage("2001");
        carHutCar3.setMileage("100000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarPriceFrom_ListIsNotNullWithKilometerPostfix_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("2001km");
        carHutCar3.setMileage("100000km");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarMileageFrom_ListIsNotNullAndContainsDifferentTypesOfMileageValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("Not stated");
        carHutCar3.setMileage("10000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageFrom_ListIsNotNull_StringValueIsMileageMore_ShouldReturnOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "mileageMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("Not stated");
        carHutCar3.setMileage("199999");
        carHutCar4.setMileage("1000000km");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarMileageFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageTo_ListIsNull() {
        filter.filterCarMileageTo(null, "1000");
    }

    @Test
    void filterCarMileageTo_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarMileageTo(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageTo_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999");
        carHutCar2.setMileage("2001");
        carHutCar3.setMileage("100000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPriceTo_ListIsNotNullWithKilometerPostfix_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("2001km");
        carHutCar3.setMileage("100000km");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMileageTo_ListIsNotNullAndContainsDifferentTypesOfMileageValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("Not stated");
        carHutCar3.setMileage("10000");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarMileageTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarMilaegeTo_ListIsNotNull_StringValueIsMileageMore_ShouldReturnFourCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "mileageMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setMileage("1999km");
        carHutCar2.setMileage("Not stated");
        carHutCar3.setMileage("199999");
        carHutCar4.setMileage("1000000km");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarMileageTo(cars, value);

        Assertions.assertEquals(4, cars.size());
    }

    @Test
    void filterCarFuelType_ListIsNull() {
        filter.filterCarFuelType(null, "Petrol");
    }

    @Test
    void filterCarFuelType_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarFuelType(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarFuelType_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "Petrol";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setFuel("Petrol");
        carHutCar2.setFuel("Diesel");
        carHutCar3.setFuel("LPG");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarFuelType(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarGearbox_ListIsNull() {
        filter.filterCarGearbox(null, "Automatic");
    }

    @Test
    void filterCarGearbox_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarGearbox(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarGearbox_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "Automatic";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setGearbox("Manual");
        carHutCar2.setGearbox("Sequential");
        carHutCar3.setGearbox("Automatic");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarGearbox(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowertrain_ListIsNull() {
        filter.filterCarPowertrain(null, "All-Wheel");
    }

    @Test
    void filterCarPowertrain_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarPowertrain(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowertrain_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "Front-Wheel";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setPowertrain("All-wheel");
        carHutCar2.setPowertrain("Front-Wheel");
        carHutCar3.setPowertrain("Rear-Wheel");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowertrain(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerFrom_ListIsNull() {
        filter.filterCarPowerFrom(null, "100");
    }

    @Test
    void filterCarPowerFrom_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarPowerFrom(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerFrom_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "100";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("99");
        carHutCar2.setEnginePower("150");
        carHutCar3.setEnginePower("0");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerFrom_ListIsNotNullWithKwPostfix_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "200";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("150kW");
        carHutCar2.setEnginePower("95kW");
        carHutCar3.setEnginePower("300kW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerFrom_ListIsNotNullAndContainsDifferentTypesOfPowerValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "100";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("101kW");
        carHutCar2.setEnginePower("Something random");
        carHutCar3.setEnginePower("99");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerFrom_ListIsNotNull_StringValueIsPowerMore_ShouldReturnOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "powerMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setEnginePower("301kW");
        carHutCar2.setEnginePower("Not stated");
        carHutCar3.setEnginePower("150");
        carHutCar4.setEnginePower("86kW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarPowerFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerTo_ListIsNull() {
        filter.filterCarPowerTo(null, "100");
    }

    @Test
    void filterCarPowerTo_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarPowerTo(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerTo_ListIsNotNull_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "100";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("99");
        carHutCar2.setEnginePower("150");
        carHutCar3.setEnginePower("0");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerTo(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarPowerTo_ListIsNotNullWithKwPostfix_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "100";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("150kW");
        carHutCar2.setEnginePower("95kW");
        carHutCar3.setEnginePower("300kW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerTo_ListIsNotNullAndContainsDifferentTypesOfPowerValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "100";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEnginePower("101kW");
        carHutCar2.setEnginePower("Something random");
        carHutCar3.setEnginePower("99");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarPowerTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarPowerTo_ListIsNotNull_StringValueIsPowerMore_ShouldReturnFourCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "powerMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setEnginePower("301kW");
        carHutCar2.setEnginePower("Not stated");
        carHutCar3.setEnginePower("150");
        carHutCar4.setEnginePower("86kW");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarPowerTo(cars, value);

        Assertions.assertEquals(4, cars.size());
    }

    @Test
    void filterCarTypes_ListIsNull() {
        filter.filterCarTypes(null, new ArrayList<>());
    }

    @Test
    void filterCarTypes_ListWithValuesIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarTypes(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarTypes_ListIsNotNull_ValidValuesList_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        List<String> types = new ArrayList<>();

        types.add("SUV");
        types.add("Combi");

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setBodyType("Combi");
        carHutCar2.setBodyType("Sedan");
        carHutCar3.setBodyType("SUV");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarTypes(cars, types);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarRegistrationFrom_ListIsNull() {
        filter.filterCarRegistrationFrom(null, "2000");
    }

    @Test
    void filterCarRegistrationFrom_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarRegistrationFrom(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarRegistrationFrom_ListIsNotNull_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setRegistration("1999");
        carHutCar2.setRegistration("2000");
        carHutCar3.setRegistration("2009");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarRegistrationFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarRegistrationFrom_ListIsNotNullAndContainsDifferentTypesOfRegistrationValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setRegistration("1999");
        carHutCar2.setRegistration("Something random");
        carHutCar3.setRegistration("2024");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarRegistrationFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarRegistrationFrom_ListIsNotNull_StringValueIsRegistrationOlder_ShouldReturnFourCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "registrationOlder";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setRegistration("2000");
        carHutCar2.setRegistration("1990");
        carHutCar3.setRegistration("1950");
        carHutCar4.setRegistration("2021");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarRegistrationFrom(cars, value);

        Assertions.assertEquals(4, cars.size());
    }

    @Test
    void filterCarRegistrationTo_ListIsNull() {
        filter.filterCarRegistrationTo(null, "2000");
    }

    @Test
    void filterCarRegistrationTo_StringValueIsNull() {
         List<CarHutCar> cars = new ArrayList<>();
         cars.add(new CarHutCar());
         filter.filterCarRegistrationTo(cars, null);
         Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarRegistrationTo_ListIsNotNull_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setRegistration("1999");
        carHutCar2.setRegistration("2000");
        carHutCar3.setRegistration("2009");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarRegistrationTo(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarRegistrationTo_ListIsNotNullAndContainsDifferentTypesOfRegistrationValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setRegistration("1999");
        carHutCar2.setRegistration("Something random");
        carHutCar3.setRegistration("2024");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarRegistrationTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarRegistrationTo_ListIsNotNull_StringValueIsRegistrationOlder_ShouldReturnOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "registrationOlder";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setRegistration("2000");
        carHutCar2.setRegistration("1990");
        carHutCar3.setRegistration("1950");
        carHutCar4.setRegistration("2021");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarRegistrationTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementFrom_ListIsNull() {
        filter.filterCarDisplacementFrom(null, "2000");
    }

    @Test
    void filterCarDisplacementFrom_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarDisplacementFrom(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementFrom_ListIsNotNull_ValidString_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEngineDisplacement("1999");
        carHutCar2.setEngineDisplacement("2000");
        carHutCar3.setEngineDisplacement("2009");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarDisplacementFrom(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarDisplacementFrom_ListIsNotNullAndContainsDifferentTypesOfDisplacementValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEngineDisplacement("5000");
        carHutCar2.setEngineDisplacement("Something random");
        carHutCar3.setEngineDisplacement("1999");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarDisplacementFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementFrom_ListIsNotNull_StringValueIsDisplacementMore_ShouldReturnOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "displacementMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setEngineDisplacement("6000");
        carHutCar2.setEngineDisplacement("5009");
        carHutCar3.setEngineDisplacement("0");
        carHutCar4.setEngineDisplacement("Not stated");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarDisplacementFrom(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementTo_ListIsNull() {
        filter.filterCarDisplacementTo(null, "2000");
    }

    @Test
    void filterCarDisplacementTo_StringValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarDisplacementTo(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementTo_ListIsNotNull_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEngineDisplacement("1999");
        carHutCar2.setEngineDisplacement("10000");
        carHutCar3.setEngineDisplacement("2009");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarDisplacementTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementTo_ListIsNotNullAndContainsDifferentTypesOfDisplacementValues_ValidString_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "2000";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setEngineDisplacement("5000");
        carHutCar2.setEngineDisplacement("Something random");
        carHutCar3.setEngineDisplacement("1999");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarDisplacementTo(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDisplacementTo_ListIsNotNull_StringValueIsDisplacementMore_ShouldReturnFourCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "displacementMore";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();
        CarHutCar carHutCar4 = new CarHutCar();

        carHutCar1.setEngineDisplacement("6000");
        carHutCar2.setEngineDisplacement("5009");
        carHutCar3.setEngineDisplacement("0");
        carHutCar4.setEngineDisplacement("Not stated");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);
        cars.add(carHutCar4);

        filter.filterCarDisplacementTo(cars, value);

        Assertions.assertEquals(4, cars.size());
    }

    @Test
    void filterCarSeatingConfig_ListIsNull() {
        filter.filterCarSeatingConfig(null, "7");
    }

    @Test
    void filterCarSeatingConfig_ValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarSeatingConfig(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarSeatingConfig_ListIsNotNull_ValidValue_ShouldRemoveOneCar() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "7";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setSeats("7");
        carHutCar2.setSeats("5");
        carHutCar3.setSeats("7");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarSeatingConfig(cars, value);

        Assertions.assertEquals(2, cars.size());
    }

    @Test
    void filterCarDoors_ListIsNull() {
        filter.filterCarDoors(null, "5");
    }

    @Test
    void filterCarDoors_ValueIsNull() {
        List<CarHutCar> cars = new ArrayList<>();
        cars.add(new CarHutCar());
        filter.filterCarDoors(cars, null);
        Assertions.assertEquals(1, cars.size());
    }

    @Test
    void filterCarDoors_ListIsNotNull_ValidValue_ShouldRemoveTwoCars() {
        List<CarHutCar> cars = new ArrayList<>();
        String value = "5";

        CarHutCar carHutCar1 = new CarHutCar();
        CarHutCar carHutCar2 = new CarHutCar();
        CarHutCar carHutCar3 = new CarHutCar();

        carHutCar1.setDoors("7");
        carHutCar2.setDoors("5");
        carHutCar3.setDoors("7");

        cars.add(carHutCar1);
        cars.add(carHutCar2);
        cars.add(carHutCar3);

        filter.filterCarDoors(cars, value);

        Assertions.assertEquals(1, cars.size());
    }

}
