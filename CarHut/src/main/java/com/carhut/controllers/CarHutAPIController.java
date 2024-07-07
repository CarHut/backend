package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.models.deprecated.AutobazarEUCarObject;
import com.carhut.models.requestmodels.CarHutCarFilterModel;
import com.carhut.models.requestmodels.ModelsPostModel;
import com.carhut.services.CarHutAPIService;
import com.carhut.services.CarImageService;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.util.exceptions.CarHutException;
import com.carhut.util.exceptions.carhutapi.*;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(path = "/api/carhut")
public class CarHutAPIController {

    @Autowired
    private CarHutAPIService carHutAPIService;
    @Autowired
    private CarImageService carImageService;
    private ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getUserIdByUsername")
    @ResponseBody
    public ResponseEntity<String> getUserIdByUsername(@RequestParam String username) {
        try {
            String userId = carHutAPIService.getUserIdByUsername(username);

            if (userId != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getUserIdByUsername - Successfully retrieved user id.");
                return ResponseEntity.ok().body(userId);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getUserIdByUsername - Something went wrong when retrieving user id.");
                return ResponseEntity.internalServerError().body(null);
            }
        }
        catch (CarHutException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getUserIdByUsername - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/removeOffer")
    @ResponseBody
    public ResponseEntity<String> removeOffer(@RequestParam String carId) {
        try {
            RequestStatusEntity status = carHutAPIService.removeOffer(carId);

            if (status == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /removeOffer - Successfully removed car offer.");
                return ResponseEntity.ok("Successfully removed car offer.");
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /removeOffer - Something went wrong when removing offer.");
                return ResponseEntity.internalServerError().body("Something went wrong when removing offer.");
            }
        }
        catch (CarHutException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /removeOffer - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getMyListings")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getMyListings(@RequestParam String username) {
        try {
            List<CarHutCar> cars = carHutAPIService.getMyListings(username);

            if (cars != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getMyListings - Successfully retrieved listings for username: " + username);
                return ResponseEntity.ok(cars);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getMyListings - Something went wrong when retrieving listings for username: " + username);
                return ResponseEntity.internalServerError().body(null);
            }
        }
        catch (CarHutException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getMyListings - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getEmailByUserId")
    @ResponseBody
    public ResponseEntity<String> getEmailByUserId(@RequestParam String userId) {
        try {
            String email = carHutAPIService.getEmailByUserId(userId);
            if (email != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getEmailByUserId - Successfully retrieved email.");
                return ResponseEntity.ok(email);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getEmailByUserId - Couldn't retrieve email.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getEmailByUserId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getOffersNumByUserId")
    @ResponseBody
    public ResponseEntity<Integer> getOffersNumByUserId(@RequestParam String userId) {
        try {
            Integer offersNumByUserId = carHutAPIService.getOffersNumByUserId(userId);
            if (offersNumByUserId != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getOffersNumByUserId - Successfully retrieved offers num.");
                return ResponseEntity.ok(offersNumByUserId);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getOffersNumByUserId - Couldn't retrieve offers num.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getOffersNumByUserId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getFirstNameAndSurnameByUserId")
    @ResponseBody
    public ResponseEntity<String> getFirstNameAndSurnameByUserId(@RequestParam String userId) {
        try {
            String usernameAndSurname = carHutAPIService.getFirstNameAndSurnameByUserId(userId);
            if (usernameAndSurname != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFirstNameAndSurnameByUserId - Successfully retrieved username.");
                return ResponseEntity.ok(usernameAndSurname);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFirstNameAndSurnameByUserId - Couldn't retrieve username.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFirstNameAndSurnameByUserId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getUsernameByUserId")
    @ResponseBody
    public ResponseEntity<String> getUsernameByUserId(@RequestParam String userId) {
        try {
            String username = carHutAPIService.getUsernameByUserId(userId);
            if (username != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getUsernameByUserId - Successfully retrieved username.");
                return ResponseEntity.ok(username);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getUsernameByUserId - Couldn't retrieve username.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getUsernameByUserId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getMultipleFeaturesByIds")
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

    @GetMapping("/getFeatureNameByFeatureId")
    @ResponseBody
    public ResponseEntity<String> getFeatureNameByFeatureId(@RequestParam Integer featureId) {
        try {
            String feature = carHutAPIService.getFeatureNameByFeatureId(featureId);
            if (feature != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatureNameByFeatureId - Successfully retrieved data.");
                return ResponseEntity.ok(feature);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFeatureNameByFeatureId - Couldn't retrieve data.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPICanNotGetFeaturesException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFeatureNameByFeatureId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getColorStringNameFromColorId")
    @ResponseBody
    public ResponseEntity<String> getColorStringNameFromColorId(@RequestParam String colorId) {
        try {
            String color = carHutAPIService.getColorStringNameFromColorId(colorId);

            if (color != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getColorStringNameFromColorId - Successfully retrieved data.");
                return ResponseEntity.ok(color);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getColorStringNameFromColorId - Couldn't retrieve data.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPICanNotGetColorsException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColorStringNameFromColorId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @GetMapping(value = "/getImages")
    @ResponseBody
    public ResponseEntity<List<byte[]>> getImages(@RequestParam String carId) {
        List<byte[]> images = null;
        try {
            images = carImageService.getImagesWithCarId(carId);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getImages successfully executed.");
        } catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColorStringNameFromColorId something went wrong. Message: " + e.getMessage());
        }
        return images != null ? ResponseEntity.status(HttpStatus.OK).body(images) : ResponseEntity.internalServerError().body(null);
    }


    @RequestMapping("/getFeatureIdByFeatureName")
    @ResponseBody
    public ResponseEntity<Integer> getFeatureIdByFeatureName(@RequestParam String feature) {
        try {
            Integer id = carHutAPIService.getFeatureIdByFeatureName(feature);
            if (id != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatureIdByFeatureName - Successfully retrieved data.");
                return ResponseEntity.ok(id);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFeatureIdByFeatureName - Couldn't retrieve data.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPICanNotGetFeaturesException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFeatureIdByFeatureName - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping(value = "/addCarToDatabase", consumes = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<String> addCarToDatabase(@RequestPart("carHutCar") CarHutCar carHutCar,
                                                   @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {
        try {
            String id = carHutAPIService.addCarToDatabase(carHutCar);
            carImageService.addImagesToDatabase(carHutCar, multipartFiles);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /addCarToDatabase - Successfully executed.");
            return ResponseEntity.ok("Car was successfully added to database.");
        } catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /addCarToDatabase - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Something went wrong while trying to add car to database.");
        }
    }

    @RequestMapping("/getFeatures")
    @ResponseBody
    public ResponseEntity<List<Feature>> getFeatures() {
        try {
            List<Feature> features = carHutAPIService.getFeatures();

            if (features != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatures successfully executed.");
                return ResponseEntity.ok(features);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getFeatures - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPICanNotGetFeaturesException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /uploadImage - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping("/getColors")
    @ResponseBody
    public ResponseEntity<List<Color>> getColors() {
        try {
            List<Color> colors = carHutAPIService.getColors();

            if (colors != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getColors successfully executed.");
                return ResponseEntity.ok(colors);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColors - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPICanNotGetColorsException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColors - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping("/getBodyTypes")
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

    @RequestMapping("/getGearboxTypes")
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

    @RequestMapping("/getFuelTypes")
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

    @RequestMapping("/getPowertrainTypes")
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
    
    @RequestMapping("/getAllBrands")
    @ResponseBody
    public ResponseEntity<List<Brand>> getAllBrands() {
        try {
            List<Brand> brands = carHutAPIService.getAllBrands();

            if (brands != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getAllBrands - Successfully retrieved data.");
                return ResponseEntity.ok(brands);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getAllBrands - Couldn't retrieve data.");
                return ResponseEntity.internalServerError().body(null);
            }
        }
        catch (CarHutAPIBrandsNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getAllBrands - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @GetMapping("/getBrandIdFromBrandName")
    @ResponseBody
    public ResponseEntity<Integer> getBrandIdFromModelName(@RequestParam String brand) {
        try {
            Integer brandId = carHutAPIService.getBrandIdFromBrandName(brand);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getBrandIdFromBrandName - Successfully retrieved data.");
            return ResponseEntity.ok(brandId);
        } catch (CarHutAPIBrandNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getBrandIdFromBrandName - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping("/getModelIdFromModelName")
    @ResponseBody
    public ResponseEntity<Integer> getModelIdFromModelName(@RequestParam String model, @RequestParam int brandId) {
        try {
            Integer modelId = carHutAPIService.getModelIdFromModelName(model, brandId);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelIdFromModelName - Successfully retrieved data.");
            return ResponseEntity.ok(modelId);
        } catch (CarHutAPIModelNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getModelIdFromModelName - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getModelsByBrand/{brand}")
    @ResponseBody
    public ResponseEntity<List<Model>> getModelsByBrand(@PathVariable String brand) {
        try {
            List<Model> models = carHutAPIService.getModelsByBrandName(brand);
            if (models != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelsByBrand - Successfully retrieved data.");
                return ResponseEntity.ok(models);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getModelsByBrand - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPIModelsNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getModelsByBrand - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getModelsByBrandName")
    @ResponseBody
    public ResponseEntity<List<Model>> getModelsByBrandName(@RequestParam String brandName) {
        try {
            List<Model> models = carHutAPIService.getModelsByBrandName(brandName);
            if (models != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelsByBrandName - Successfully retrieved data.");
                return ResponseEntity.ok(models);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getModelsByBrandName - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPIModelsNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getModelsByBrandName - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @RequestMapping("/getCarWithId")
    @ResponseBody
    public ResponseEntity<CarHutCar> getCarWithId(@RequestParam String carId) {
        try {
            CarHutCar carHutCar = carHutAPIService.getCarWithId(carId);
            if (carHutCar != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getCarWithId - Successfully retrieved data.");
                return ResponseEntity.ok(carHutCar);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getCarWithId - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        } catch (CarHutAPICarNotFoundException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getCarWithId - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getCarsWithFilters")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getCarsWithFilters(
            @RequestBody CarHutCarFilterModel carHutCarFilterModel,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(required = false) Integer offersPerPage,
            @RequestParam(required = false) Integer currentPage
    ) {
        try {
            List<CarHutCar> cars = carHutAPIService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder, offersPerPage, currentPage);

            if (cars != null) {
                controllerLogger.saveToFile("[CarHutAPIController][OK]: /getCarsWithFilters - Successfully retrieved data.");
                return ResponseEntity.ok(cars);
            } else {
                controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getCarsWithFilters - Couldn't retrieve data");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutAPIException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getCarsWithFilters - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getNumberOfFilteredCars")
    @ResponseBody
    public ResponseEntity<Integer> getNumberOfFilteredCars(@RequestBody CarHutCarFilterModel carHutCarFilterModel) {
        try {
            Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getNumberOfFilteredCars - Successfully retrieved data.");
            return ResponseEntity.ok(size);
        }
        catch (CarHutAPIException e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getNumberOfFilteredCars - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
}
