package com.carhut.controllers.carhutapi;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.*;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.CarHutCarFilterModel;
import com.carhut.requests.requestmodels.RemoveCarRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.services.carhutapi.CarHutAPIService;
import com.carhut.services.carhutapi.CarImageService;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        String userId = carHutAPIService.getUserIdByUsername(username);

        if (userId != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getUserIdByUsername - Successfully retrieved user id.");
            return ResponseEntity.ok().body(userId);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getUserIdByUsername - Something went wrong when retrieving user id.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/removeOffer")
    @ResponseBody
    public ResponseEntity<String> removeOffer(@RequestBody PrincipalRequest<RemoveCarRequestModel> removeCarRequestModelPrincipalRequest) {
        ServiceStatusEntity status = carHutAPIService.removeOffer(removeCarRequestModelPrincipalRequest);

        if (status == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /removeOffer - Successfully removed car offer.");
            return ResponseEntity.ok("Successfully removed car offer.");
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /removeOffer - Something went wrong when removing offer.");
            return ResponseEntity.internalServerError().body("Something went wrong when removing offer.");
        }
    }

    @PostMapping("/getMyListings")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getMyListings(@RequestBody PrincipalRequest<SimpleUsernameRequestModel> simpleUsernameRequestModelPrincipalRequest) {
        List<CarHutCar> cars = carHutAPIService.getMyListings(simpleUsernameRequestModelPrincipalRequest);

        if (cars != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getMyListings - Successfully retrieved listings for username: " + simpleUsernameRequestModelPrincipalRequest.getDto().getUsername());
            return ResponseEntity.ok(cars);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getMyListings - Something went wrong when retrieving listings for username: " + simpleUsernameRequestModelPrincipalRequest.getDto().getUsername());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getEmailByUserId")
    @ResponseBody
    public ResponseEntity<String> getEmailByUserId(@RequestParam String userId) {
        String email = carHutAPIService.getEmailByUserId(userId);
        if (email != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getEmailByUserId - Successfully retrieved email.");
            return ResponseEntity.ok(email);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getEmailByUserId - Couldn't retrieve email.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/getOffersNumByUserId")
    @ResponseBody
    public ResponseEntity<Integer> getOffersNumByUserId(@RequestParam String userId) {
        Integer offersNumByUserId = carHutAPIService.getOffersNumByUserId(userId);
        if (offersNumByUserId != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getOffersNumByUserId - Successfully retrieved offers num.");
            return ResponseEntity.ok(offersNumByUserId);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getOffersNumByUserId - Couldn't retrieve offers num.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/getFirstNameAndSurnameByUserId")
    @ResponseBody
    public ResponseEntity<String> getFirstNameAndSurnameByUserId(@RequestParam String userId) {
        String usernameAndSurname = carHutAPIService.getFirstNameAndSurnameByUserId(userId);
        if (usernameAndSurname != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFirstNameAndSurnameByUserId - Successfully retrieved username.");
            return ResponseEntity.ok(usernameAndSurname);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFirstNameAndSurnameByUserId - Couldn't retrieve username.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/getUsernameByUserId")
    @ResponseBody
    public ResponseEntity<String> getUsernameByUserId(@RequestParam String userId) {
        String username = carHutAPIService.getUsernameByUserId(userId);
        if (username != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getUsernameByUserId - Successfully retrieved username.");
            return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON_UTF8).body(username);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getUsernameByUserId - Couldn't retrieve username.");
            return ResponseEntity.status(404).body(null);
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
        String feature = carHutAPIService.getFeatureNameByFeatureId(featureId);
        if (feature != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getFeatureNameByFeatureId - Successfully retrieved data.");
            return ResponseEntity.ok(feature);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getFeatureNameByFeatureId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/getColorStringNameFromColorId")
    @ResponseBody
    public ResponseEntity<String> getColorStringNameFromColorId(@RequestParam String colorId) {
        String color = carHutAPIService.getColorStringNameFromColorId(colorId);

        if (color != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getColorStringNameFromColorId - Successfully retrieved data.");
            return ResponseEntity.ok(color);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getColorStringNameFromColorId - Couldn't retrieve data.");
            return ResponseEntity.status(404).body(null);
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
    public ResponseEntity<String> addCarToDatabase(@RequestPart("carHutCar") PrincipalRequest<CarHutCar> carHutCar,
                                                   @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) {

        String id = carHutAPIService.addCarToDatabase(carHutCar);
        ServiceStatusEntity imageStatus = carImageService.addImagesToDatabase(carHutCar, multipartFiles);
        if (imageStatus == ServiceStatusEntity.ERROR) {
            return ResponseEntity.internalServerError().body("Could not save images to server database.");
        }
        controllerLogger.saveToFile("[CarHutAPIController][OK]: /addCarToDatabase - Successfully executed.");
        return ResponseEntity.ok("Car was successfully added to database.");
    }

    @RequestMapping("/getFeatures")
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

    @RequestMapping("/getColors")
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
        List<Brand> brands = carHutAPIService.getAllBrands();

        if (brands != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getAllBrands - Successfully retrieved data.");
            return ResponseEntity.ok(brands);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getAllBrands - Couldn't retrieve data.");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getBrandIdFromBrandName")
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

    @RequestMapping("/getModelIdFromModelName")
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

    @GetMapping("/getModelsByBrand/{brand}")
    @ResponseBody
    public ResponseEntity<List<Model>> getModelsByBrand(@PathVariable String brand) {
        List<Model> models = carHutAPIService.getModelsByBrandName(brand);
        if (models != null) {
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getModelsByBrand - Successfully retrieved data.");
            return ResponseEntity.ok(models);
        } else {
            controllerLogger.saveToFile("[CarHutAPIController][WARN]: /getModelsByBrand - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getModelsByBrandName")
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

    @RequestMapping("/getCarWithId")
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

    @PostMapping("/getCarsWithFilters")
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

    @PostMapping("/getNumberOfFilteredCars")
    @ResponseBody
    public ResponseEntity<Integer> getNumberOfFilteredCars(@RequestBody CarHutCarFilterModel carHutCarFilterModel) {
        Integer size = carHutAPIService.getNumberOfFilteredCars(carHutCarFilterModel);
        controllerLogger.saveToFile("[CarHutAPIController][OK]: /getNumberOfFilteredCars - Successfully retrieved data.");
        return ResponseEntity.ok(size);
    }
    
}
