package com.carhut.controllers;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.requests.requestmodels.CarHutCarFilterModel;
import com.carhut.requests.requestmodels.RemoveCarRequestModel;
import com.carhut.services.CarHutAPIService;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/carhut-api")
public class CarHutAPIController {

    @Autowired
    private CarHutAPIService carHutAPIService;
    private ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/remove-offer")
    @ResponseBody
    public ResponseEntity<String> removeOffer(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody RemoveCarRequestModel removeCarRequestModel) throws IOException, InterruptedException {
        ServiceStatusEntity status = carHutAPIService.removeOffer(removeCarRequestModel, bearerToken);

        if (status == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /removeOffer - Successfully removed car offer.");
            return ResponseEntity.ok("Successfully removed car offer.");
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /removeOffer - Something went wrong when removing offer.");
            return ResponseEntity.internalServerError().body("Something went wrong when removing offer.");
        }
    }

    @PostMapping("/get-listings")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getListings(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody String userId) throws IOException, InterruptedException {
        List<CarHutCar> cars = carHutAPIService.getMyListings(userId, bearerToken);

        if (cars != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getMyListings - Successfully retrieved listings for user id: " + userId);
            return ResponseEntity.ok(cars);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getMyListings - Something went wrong when retrieving listings for user id: " + userId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-multiple-features-by-ids")
    @ResponseBody
    public ResponseEntity<List<String>> getMultipleFeaturesByIds(@RequestBody List<Integer> featureIds) {
        List<String> resultListOfFeatures = carHutAPIService.getMultipleFeaturesByIds(featureIds);
        if (resultListOfFeatures != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getMultipleFeaturesByIds - Successfully retrieved data.");
            return ResponseEntity.ok(resultListOfFeatures);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getMultipleFeaturesByIds - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-feature-name-by-feature-id")
    @ResponseBody
    public ResponseEntity<String> getFeatureNameByFeatureId(@RequestParam Integer featureId) {
        String feature = carHutAPIService.getFeatureNameByFeatureId(featureId);
        if (feature != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatureNameByFeatureId - Successfully retrieved data.");
            return ResponseEntity.ok(feature);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFeatureNameByFeatureId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-color-name-from-color-id")
    @ResponseBody
    public ResponseEntity<String> getColorNameFromColorId(@RequestParam String colorId) {
        String color = carHutAPIService.getColorStringNameFromColorId(colorId);

        if (color != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getColorStringNameFromColorId - Successfully retrieved data.");
            return ResponseEntity.ok(color);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getColorStringNameFromColorId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-feature-id-by-feature-name")
    @ResponseBody
    public ResponseEntity<Integer> getFeatureIdByFeatureName(@RequestParam String feature) {
        Integer id = carHutAPIService.getFeatureIdByFeatureName(feature);
        if (id != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatureIdByFeatureName - Successfully retrieved data.");
            return ResponseEntity.ok(id);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFeatureIdByFeatureName - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping(value = "/addCarToDatabase", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<String> addCarToDatabase(@RequestHeader("Authorization") String bearerToken,
                                                   @RequestPart("carHutCar") CarHutCar carHutCar,
                                                   @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {

        String id = carHutAPIService.addCarToDatabase(carHutCar, bearerToken);
//        ServiceStatusEntity imageStatus = carImageService.addImagesToDatabase(carHutCar, multipartFiles);
//        if (imageStatus == ServiceStatusEntity.ERROR) {
//            return ResponseEntity.internalServerError().body("Could not save images to server database.");
//        }
        controllerLogger.saveToFile("[CarHutAPIController][OK]: /addCarToDatabase - Successfully executed.");
        return ResponseEntity.ok("Car was successfully added to database.");
    }

    @GetMapping("/get-features")
    @ResponseBody
    public ResponseEntity<List<Feature>> getFeatures() {
        List<Feature> features = carHutAPIService.getFeatures();

        if (features != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatures successfully executed.");
            return ResponseEntity.ok(features);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFeatures - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-colors")
    @ResponseBody
    public ResponseEntity<List<Color>> getColors() {
        List<Color> colors = carHutAPIService.getColors();

        if (colors != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getColors successfully executed.");
            return ResponseEntity.ok(colors);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColors - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-body-types")
    @ResponseBody
    public ResponseEntity<List<String>> getBodyTypes() {
        List<String> bodyTypes = carHutAPIService.getBodyTypes();

        if (bodyTypes != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getBodyTypes successfully executed.");
            return ResponseEntity.ok(bodyTypes);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getBodyTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-gearbox-types")
    @ResponseBody
    public ResponseEntity<List<String>> getGearboxTypes() {
        List<String> gearboxTypes = carHutAPIService.getGearboxTypes();

        if (gearboxTypes != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getGearboxTypes successfully executed.");
            return ResponseEntity.ok(gearboxTypes);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getGearboxTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-fuel-types")
    @ResponseBody
    public ResponseEntity<List<String>> getFuelTypes() {
        List<String> fuelTypes = carHutAPIService.getFuelTypes();

        if (fuelTypes != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFuelTypes successfully executed.");
            return ResponseEntity.ok(fuelTypes);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFuelTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-powertrain-types")
    @ResponseBody
    public ResponseEntity<List<String>> getPowertrainTypes() {
        List<String> powertrainTypes = carHutAPIService.getPowertrainTypes();

        if (powertrainTypes != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getPowertrainTypes successfully executed.");
            return ResponseEntity.ok(powertrainTypes);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getPowertrainTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/get-all-brands")
    @ResponseBody
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = carHutAPIService.getAllBrands();

        if (brands != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getAllBrands - Successfully retrieved data.");
            return ResponseEntity.ok(brands);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getAllBrands - Couldn't retrieve data.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-brand-id-from-brand-name")
    @ResponseBody
    public ResponseEntity<Integer> getBrandIdFromModelName(@RequestParam String brand) {
            Integer brandId = carHutAPIService.getBrandIdFromBrandName(brand);
            if (brandId != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getBrandIdFromBrandName - Successfully retrieved data.");
                return ResponseEntity.ok(brandId);
            } else  {
                controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getBrandIdFromBrandName - Brand with name " + brand + " does not exist.");
                return ResponseEntity.internalServerError().body(null);
            }
    }

    @GetMapping("/get-model-id-from-model-name")
    @ResponseBody
    public ResponseEntity<Integer> getModelIdFromModelName(@RequestParam String model, @RequestParam int brandId) {
        Integer modelId = carHutAPIService.getModelIdFromModelName(model, brandId);

        if (modelId != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelIdFromModelName - Successfully retrieved data.");
            return ResponseEntity.ok(modelId);
        } else  {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getBrandIdFromBrandName - Could not retrieve data.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-models-by-brand-name")
    @ResponseBody
    public ResponseEntity<List<Model>> getModelsByBrandName(@RequestParam String brandName) {
        List<Model> models = carHutAPIService.getModelsByBrandName(brandName);
        if (models != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelsByBrandName - Successfully retrieved data.");
            return ResponseEntity.ok(models);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getModelsByBrandName - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-car-with-id")
    @ResponseBody
    public ResponseEntity<CarHutCar> getCarWithId(@RequestParam String carId) {
        CarHutCar carHutCar = carHutAPIService.getCarWithId(carId);
        if (carHutCar != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getCarWithId - Successfully retrieved data.");
            return ResponseEntity.ok(carHutCar);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getCarWithId - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-cars-with-filters")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getCarsWithFilters(
            @RequestBody CarHutCarFilterModel carHutCarFilterModel,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Integer offersPerPage,
            @RequestParam(required = false) Integer currentPage
    ) {
        List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, currentPage);

        if (cars != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getCarsWithFilters - Successfully retrieved data.");
            return ResponseEntity.ok(cars);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getCarsWithFilters - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/get-number-of-filtered-cars")
    @ResponseBody
    public ResponseEntity<Integer> getNumberOfFilteredCars(@RequestBody CarHutCarFilterModel carHutCarFilterModel) {
        Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel);
        controllerLogger.saveToFile("[CarHutAPIController][OK]: /getNumberOfFilteredCars - Successfully retrieved data.");
        return ResponseEntity.ok(size);
    }
    
}
