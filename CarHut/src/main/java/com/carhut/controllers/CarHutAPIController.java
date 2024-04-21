package com.carhut.controllers;

import com.carhut.models.AutobazarEUCarObject;
import com.carhut.models.Brand;
import com.carhut.models.Model;
import com.carhut.services.CarHutAPIService;
import com.carhut.temputils.models.ModelsPostModel;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.util.loggers.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/carhut")
public class CarHutAPIController {

    @Autowired
    private CarHutAPIService carHutAPIService;
    @Autowired
    private Logger logger;


    @RequestMapping("/getAllCars")
    public void getAllCars() {
        List<AutobazarEUCarObject> cars = carHutAPIService.getAllCars();
        logger.saveLogToFile("[LOG][CarHutAPIController]: API call /api/getAllCars executed successfully.");
    }

    @RequestMapping("/getAllBrands")
    @ResponseBody
    public List<Brand> getAllBrands() {
        List<Brand> brands = carHutAPIService.getAllBrands();
        logger.saveLogToFile("[LOG][CarHutAPIController]: API call /api/getAllBrands executed successfully.");
        return brands;
    }

    @GetMapping("/getModelsByBrand/{brand}")
    @ResponseBody
    public List<Model> getModelsByBrand(@PathVariable String brand) {
        List<Model> models = carHutAPIService.getModelsByBrand(brand);
        logger.saveLogToFile("[LOG][CarHutAPIController]: API call /api/getModelsByBrand executed successfully.");
        return models;
    }

    @GetMapping("/getModelsByBrandName")
    @ResponseBody
    public List<Model> getModelsByBrandName(@RequestParam String brandName) {
        List<Model> models = carHutAPIService.getModelsByBrandName(brandName);
        logger.saveLogToFile("[LOG][CarHutAPIController]: API call /api/getModelsByBrandName executed successfully.");
        return models;
    }

    @GetMapping("/getAllTempCars")
    @ResponseBody
    public List<TempCarModel> getAllTempCars(@RequestParam(required = false) String sortBy,
                                             @RequestParam(required = false, defaultValue = "ASC") String sortOrder) {
        if (sortBy != null && !sortBy.isEmpty()) {
            // If sortBy parameter is provided, apply sorting
            return carHutAPIService.getAllTempCarsSorted(sortBy, sortOrder);
        } else {
            // If sortBy parameter is not provided, return unsorted list
            return carHutAPIService.getAllTempCars();
        }
    }

    @PostMapping("/getTempCarsWithFilters")
    @ResponseBody
    public List<TempCarModel> getTempCarsWithFilters(
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) String model,
        @RequestParam(required = false) String carType,
        @RequestParam(required = false) String priceFrom,
        @RequestParam(required = false) String priceTo,
        @RequestParam(required = false) String mileageFrom,
        @RequestParam(required = false) String mileageTo,
        @RequestParam(required = false) String registration,
        @RequestParam(required = false) String seatingConfig,
        @RequestParam(required = false) String doors,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String postalCode,
        @RequestParam(required = false) String fuelType,
        @RequestParam(required = false) String powerFrom,
        @RequestParam(required = false) String powerTo,
        @RequestParam(required = false) String displacement,
        @RequestParam(required = false) String gearbox,
        @RequestParam(required = false) String powertrain,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortOrder,
        @RequestBody(required = false) List<ModelsPostModel> models
    ) {
        return carHutAPIService.getTempCarsWithFilter(brand, model, carType, priceFrom, priceTo, mileageFrom, mileageTo, registration, seatingConfig, doors, location, postalCode, fuelType, powerFrom, powerTo, displacement, gearbox, powertrain, sortBy, sortOrder, models);
    }

    @GetMapping("/getNumberOfTempFilteredCars")
    @ResponseBody
    public int getNumberOfTempFilteredCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String carType,
            @RequestParam(required = false) String priceFrom,
            @RequestParam(required = false) String priceTo,
            @RequestParam(required = false) String mileageFrom,
            @RequestParam(required = false) String mileageTo,
            @RequestParam(required = false) String registration,
            @RequestParam(required = false) String seatingConfig,
            @RequestParam(required = false) String doors,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String powerFrom,
            @RequestParam(required = false) String powerTo,
            @RequestParam(required = false) String displacement,
            @RequestParam(required = false) String gearbox,
            @RequestParam(required = false) String powertrain
    ) {
        return carHutAPIService.getNumberOfTempFilteredCars(brand, model, carType, priceFrom, priceTo, mileageFrom, mileageTo, registration, seatingConfig, doors, location, postalCode, fuelType, powerFrom, powerTo, displacement, gearbox, powertrain);
    }


    @RequestMapping("/updateBrand")
    public void updateBrand(@RequestParam String brand, @RequestParam String id) {
        carHutAPIService.updateBrand(brand, id);
    }

    @RequestMapping("/updateModel")
    public void updateModel(@RequestParam String model, @RequestParam String id) {
        carHutAPIService.updateModel(model, id);
    }

    @RequestMapping("/getTempCarWithId")
    @ResponseBody
    public TempCarModel getTempCarWithId(@RequestParam String carId) {
        return carHutAPIService.getTempCarWithId(carId);
    }

}
