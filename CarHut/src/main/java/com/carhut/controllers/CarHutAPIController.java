package com.carhut.controllers;

import com.carhut.models.carhut.*;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.services.CarHutAPIService;
import com.carhut.services.ImageService;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.util.loggers.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping(path = "/api/carhut")
public class CarHutAPIController {

    @Autowired
    private CarHutAPIService carHutAPIService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Logger logger;

    @PostMapping("/uploadImage")
    @ResponseBody
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image,  @RequestParam("username") String username) {
        try {
            imageService.uploadImageToFileSystem(image, username);
            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (Exception e) {
            // If there's an exception, return a failure response
            return ResponseEntity.internalServerError().body("Failed to upload image.");
        }
    }

    @RequestMapping("/getImageById")
    @ResponseBody
    public Image getImageById(@RequestParam String id) {
        return imageService.getImageById(id);
    }

    @RequestMapping("/getImagesByCarId/{carId}")
    @ResponseBody
    public List<Image> getImagesByCarId(@PathVariable String carId) {
        return imageService.getImagesByCarId(carId);
    }

    @RequestMapping("/getFeatures")
    @ResponseBody
    public List<Feature> getFeatures() {
        return carHutAPIService.getFeatures();
    }

    @RequestMapping("/getColors")
    @ResponseBody
    public List<Color> getColors() {
        return carHutAPIService.getColors();
    }

    @RequestMapping("/getBodyTypes")
    @ResponseBody
    public List<String> getBodyTypes() {
        return carHutAPIService.getBodyTypes();
    }

    @RequestMapping("/getGearboxTypes")
    @ResponseBody
    public List<String> getGearboxTypes() {
        return carHutAPIService.getGearboxTypes();
    }

    @RequestMapping("/getFuelTypes")
    @ResponseBody
    public List<String> getFuelTypes() {
        return carHutAPIService.getFuelTypes();
    }

    @RequestMapping("/getPowertrainTypes")
    @ResponseBody
    public List<String> getPowertrainTypes() {
        return carHutAPIService.getPowertrainTypes();
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

    @RequestMapping("/updateBrand")
    public void updateBrand(@RequestParam String brand, @RequestParam String id) {
        carHutAPIService.updateBrand(brand, id);
    }

    @RequestMapping("/updateModel")
    public void updateModel(@RequestParam String model, @RequestParam String id) {
        carHutAPIService.updateModel(model, id);
    }

    @RequestMapping("/getCarWithId")
    @ResponseBody
    public CarHutCar getCarWithId(@RequestParam String carId) {
        return carHutAPIService.getCarWithId(carId);
    }

    @Deprecated
    @GetMapping("/getAllCars")
    @ResponseBody
    public List<CarHutCar> getAllCars(@RequestParam(required = false) String sortBy,
                                      @RequestParam(required = false, defaultValue = "ASC") String sortOrder) {
        if (sortBy != null && !sortBy.isEmpty()) {
            // If sortBy parameter is provided, apply sorting
            return carHutAPIService.getAllCarsSorted(sortBy, sortOrder);
        } else {
            return carHutAPIService.getAllCars();
        }
    }

    @PostMapping("/getCarsWithFilters")
    @ResponseBody
    public List<CarHutCar> getCarsWithFilters(
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
        return carHutAPIService.getCarsWithFilter(brand, model, carType, priceFrom, priceTo, mileageFrom, mileageTo, registration, seatingConfig, doors, location, postalCode, fuelType, powerFrom, powerTo, displacement, gearbox, powertrain, sortBy, sortOrder, models);
    }

    @GetMapping("/getNumberOfFilteredCars")
    @ResponseBody
    public int getNumberOfFilteredCars(
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
        return carHutAPIService.getNumberOfFilteredCars(brand, model, carType, priceFrom, priceTo, mileageFrom, mileageTo, registration, seatingConfig, doors, location, postalCode, fuelType, powerFrom, powerTo, displacement, gearbox, powertrain);
    }

    @Deprecated
    @RequestMapping("/getTempCarWithId")
    @ResponseBody
    public TempCarModel getTempCarWithId(@RequestParam String carId) {
        return carHutAPIService.getTempCarWithId(carId);
    }

    @Deprecated
    @RequestMapping("/getAllAutobazarCars")
    public void getAllAutobazarCars() {
        List<AutobazarEUCarObject> cars = carHutAPIService.getAllAutobazarCars();
        logger.saveLogToFile("[LOG][CarHutAPIController]: API call /api/getAllCars executed successfully.");
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

}
