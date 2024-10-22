package com.carhut.controller;

import com.carhut.commons.model.CarHutCar;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.model.carhut.*;
import com.carhut.request.CarHutCarFilterModel;
import com.carhut.request.RemoveCarRequestModel;
import com.carhut.service.CarHutAPIService;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.carhut.util.logger.CarHutApiLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/carhut-api")
public class CarHutAPIController {

    @Autowired
    private CarHutAPIService carHutAPIService;
    private CarHutApiLogger logger = CarHutApiLogger.getInstance();

    @PostMapping("/remove-offer")
    @ResponseBody
    public ResponseEntity<String> removeOffer(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody RemoveCarRequestModel removeCarRequestModel)
            throws InterruptedException, ExecutionException {
        ServiceStatusEntity status = carHutAPIService.removeOffer(removeCarRequestModel, bearerToken).get();

        if (status == ServiceStatusEntity.SUCCESS) {
            logger.logInfo("[CarHutAPIController][OK]: /removeOffer - Successfully removed car offer.");
            return ResponseEntity.ok("Successfully removed car offer.");
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /removeOffer - Something went wrong when removing offer.");
            return ResponseEntity.internalServerError().body("Something went wrong when removing offer.");
        }
    }

    @GetMapping("/get-listings")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getListings(
            @RequestHeader("Authorization") String bearerToken,
            @RequestParam("user-id") String userId) throws InterruptedException, ExecutionException {
        List<CarHutCar> cars = carHutAPIService.getMyListings(userId, bearerToken).get();

        if (cars != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getMyListings - Successfully retrieved listings for user id: " + userId);
            return ResponseEntity.ok(cars);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getMyListings - Something went wrong when retrieving listings for user id: " + userId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-multiple-features-by-ids")
    @ResponseBody
    public ResponseEntity<List<String>> getMultipleFeaturesByIds(@RequestBody List<Integer> featureIds) {
        List<String> resultListOfFeatures = carHutAPIService.getMultipleFeaturesByIds(featureIds);
        if (resultListOfFeatures != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getMultipleFeaturesByIds - Successfully retrieved data.");
            return ResponseEntity.ok(resultListOfFeatures);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getMultipleFeaturesByIds - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-feature-name-by-feature-id")
    @ResponseBody
    public ResponseEntity<String> getFeatureNameByFeatureId(@RequestParam(name = "feature-id") Integer featureId) {
        String feature = carHutAPIService.getFeatureNameByFeatureId(featureId);
        if (feature != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getFeatureNameByFeatureId - Successfully retrieved data.");
            return ResponseEntity.ok(feature);
        } else {
            logger.logError("[CarHutAPIController][WARN]: /getFeatureNameByFeatureId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-color-name-from-color-id")
    @ResponseBody
    public ResponseEntity<String> getColorNameFromColorId(@RequestParam(name = "color-id") String colorId) {
        String color = carHutAPIService.getColorStringNameFromColorId(colorId);

        if (color != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getColorStringNameFromColorId - Successfully retrieved data.");
            return ResponseEntity.ok(color);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getColorStringNameFromColorId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-feature-id-by-feature-name")
    @ResponseBody
    public ResponseEntity<Integer> getFeatureIdByFeatureName(@RequestParam(name = "feature") String feature) {
        Integer id = carHutAPIService.getFeatureIdByFeatureName(feature);
        if (id != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getFeatureIdByFeatureName - Successfully retrieved data.");
            return ResponseEntity.ok(id);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getFeatureIdByFeatureName - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping(value = "/add-car", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> addCarToDatabase(@RequestHeader("Authorization") String bearerToken,
                                                   @RequestPart(value = "json") CarHutCar carHutCar,
                                                   @RequestPart(value = "files", required = false)
                                                       List<MultipartFile> multipartFiles)
            throws ExecutionException, InterruptedException {

        String id = carHutAPIService.addCarToDatabase(carHutCar, multipartFiles, bearerToken).get();

        if (id != null) {
            logger.logInfo("[CarHutAPIController][OK]: /addCarToDatabase - Successfully executed.");
            return ResponseEntity.ok("Car was successfully added to database.");
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /addCarToDatabase - Cannot save car to database.");
            return ResponseEntity.internalServerError().body("Cannot save car to database.");
        }
    }

    @GetMapping("/get-features")
    @ResponseBody
    public ResponseEntity<List<Feature>> getFeatures() {
        List<Feature> features = carHutAPIService.getFeatures();

        if (features != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getFeatures successfully executed.");
            return ResponseEntity.ok(features);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getFeatures - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-colors")
    @ResponseBody
    public ResponseEntity<List<Color>> getColors() {
        List<Color> colors = carHutAPIService.getColors();

        if (colors != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getColors successfully executed.");
            return ResponseEntity.ok(colors);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getColors - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/get-body-types")
    @ResponseBody
    public ResponseEntity<List<String>> getBodyTypes() {
        List<String> bodyTypes = carHutAPIService.getBodyTypes();

        if (bodyTypes != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getBodyTypes successfully executed.");
            return ResponseEntity.ok(bodyTypes);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getBodyTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-gearbox-types")
    @ResponseBody
    public ResponseEntity<List<String>> getGearboxTypes() {
        List<String> gearboxTypes = carHutAPIService.getGearboxTypes();

        if (gearboxTypes != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getGearboxTypes successfully executed.");
            return ResponseEntity.ok(gearboxTypes);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getGearboxTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-fuel-types")
    @ResponseBody
    public ResponseEntity<List<String>> getFuelTypes() {
        List<String> fuelTypes = carHutAPIService.getFuelTypes();

        if (fuelTypes != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getFuelTypes successfully executed.");
            return ResponseEntity.ok(fuelTypes);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getFuelTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-powertrain-types")
    @ResponseBody
    public ResponseEntity<List<String>> getPowertrainTypes() {
        List<String> powertrainTypes = carHutAPIService.getPowertrainTypes();

        if (powertrainTypes != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getPowertrainTypes successfully executed.");
            return ResponseEntity.ok(powertrainTypes);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getPowertrainTypes something went wrong. Service returned 'null'");
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
    @GetMapping("/get-all-brands")
    @ResponseBody
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = carHutAPIService.getAllBrands();

        if (brands != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getAllBrands - Successfully retrieved data.");
            return ResponseEntity.ok(brands);
        } else {
            logger.logError("[CarHutAPIController][ERROR]: /getAllBrands - Couldn't retrieve data.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-brand-id-from-brand-name")
    @ResponseBody
    public ResponseEntity<Integer> getBrandIdFromModelName(@RequestParam(name = "brand") String brand) {
            Integer brandId = carHutAPIService.getBrandIdFromBrandName(brand);
            if (brandId != null) {
                logger.logInfo("[CarHutAPIController][OK]: /getBrandIdFromBrandName - Successfully retrieved data.");
                return ResponseEntity.ok(brandId);
            } else  {
                logger.logError("[CarHutAPIController][ERROR]: /getBrandIdFromBrandName - Brand with name " + brand + " does not exist.");
                return ResponseEntity.internalServerError().body(null);
            }
    }

    @GetMapping("/get-model-id-from-model-name")
    @ResponseBody
    public ResponseEntity<Integer> getModelIdFromModelName(@RequestParam(name = "model") String model,
                                                           @RequestParam(name = "brand-id") int brandId) {
        Integer modelId = carHutAPIService.getModelIdFromModelName(model, brandId);

        if (modelId != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getModelIdFromModelName - Successfully retrieved data.");
            return ResponseEntity.ok(modelId);
        } else  {
            logger.logError("[CarHutAPIController][ERROR]: /getBrandIdFromBrandName - Could not retrieve data.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-models-by-brand-name")
    @ResponseBody
    public ResponseEntity<List<Model>> getModelsByBrandName(@RequestParam(name = "brand-name") String brandName) {
        List<Model> models = carHutAPIService.getModelsByBrandName(brandName);
        if (models != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getModelsByBrandName - Successfully retrieved data.");
            return ResponseEntity.ok(models);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getModelsByBrandName - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/get-car-with-id")
    @ResponseBody
    public ResponseEntity<CarHutCar> getCarWithId(@RequestParam("car-id") String carId)
            throws URISyntaxException, ExecutionException, InterruptedException {
        CarHutCar carHutCar = carHutAPIService.getCarWithId(carId).get();
        if (carHutCar != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getCarWithId - Successfully retrieved data.");
            return ResponseEntity.ok(carHutCar);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getCarWithId - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-cars-with-filters")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getCarsWithFilters(
            @RequestBody CarHutCarFilterModel carHutCarFilterModel,
            @RequestParam(name = "sort-by", required = false) String sortBy,
            @RequestParam(name = "sort-order", required = false) String sortOrder,
            @RequestParam(name = "offers-per-page", required = false) Integer offersPerPage,
            @RequestParam(name = "current-page", required = false) Integer currentPage
    ) throws ExecutionException, InterruptedException, URISyntaxException {
        List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder,
                offersPerPage, currentPage).get();

        if (cars != null) {
            logger.logInfo("[CarHutAPIController][OK]: /getCarsWithFilters - Successfully retrieved data.");
            return ResponseEntity.ok(cars);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /getCarsWithFilters - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-number-of-filtered-cars")
    @ResponseBody
    public ResponseEntity<Integer> getNumberOfFilteredCars(@RequestBody CarHutCarFilterModel carHutCarFilterModel)
            throws URISyntaxException, ExecutionException, InterruptedException {
        Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel).get();
        logger.logInfo("[CarHutAPIController][OK]: /getNumberOfFilteredCars - Successfully retrieved data.");
        return ResponseEntity.ok(size);
    }

    @PostMapping("/get-cars-by-car-ids")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getCarsByCarIds(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody List<String> ids,
            @RequestParam("user-id") String userId) throws ExecutionException, InterruptedException {
        List<CarHutCar> cars = carHutAPIService.getCarsByCarIds(ids, bearerToken, userId).get();

        if (cars != null) {
            logger.logInfo("[CarHutAPIController][OK]: /get-cars-by-car-ids - Successfully retrieved data.");
            return ResponseEntity.ok(cars);
        } else {
            logger.logWarn("[CarHutAPIController][WARN]: /get-cars-by-car-ids - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
