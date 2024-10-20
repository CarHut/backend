package com.carhut.services.sr.carhutapisr.controller;

import com.carhut.commons.model.CarHutCar;
import com.carhut.commons.model.CarHutCarFilterModel;
import com.carhut.services.sr.carhutapisr.service.CarHutApiSRService;
import com.carhut.services.sr.carhutapisr.service.ServiceStatus;
import com.carhut.services.sr.carhutapisr.util.loggers.ControllerLogger;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/carhut-api-sr")
public class CarHutApiSRController {

    @Autowired
    private CarHutApiSRService carHutApiSRService;
    private static final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/remove-offer")
    @ResponseBody
    public ResponseEntity<String> removeOffer(@RequestParam("car-id") String carId)
            throws ExecutionException, InterruptedException {
        ServiceStatus status = carHutApiSRService.removeOffer(carId).get();

        if (status == ServiceStatus.OK) {
            controllerLogger.saveToFile("[CarHutApiSRController][OK]: /removeOffer - Successfully removed car offer.");
            return ResponseEntity.ok("Successfully removed car offer.");
        } else {
            controllerLogger.saveToFile("[CarHutApiSRController][WARN]: /removeOffer - Something went wrong when removing offer.");
            return ResponseEntity.internalServerError().body("Something went wrong when removing offer.");
        }
    }

    @GetMapping("/get-listings")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getListings(@RequestParam("user-id") String userId)
            throws InterruptedException, ExecutionException {
        List<CarHutCar> cars = carHutApiSRService.getListingsForUserId(userId).get();

        if (cars != null) {
            controllerLogger.saveToFile("[CarHutApiSRController][OK]: /getMyListings - Successfully retrieved listings for user id: " + userId);
            return ResponseEntity.ok(cars);
        } else {
            controllerLogger.saveToFile("[CarHutApiSRController][WARN]: /getMyListings - Something went wrong when retrieving listings for user id: " + userId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping(value = "/add-car")
    @ResponseBody
    public ResponseEntity<String> addCarToDatabase(@RequestBody CarHutCar carHutCar)
            throws ExecutionException, InterruptedException {

        String id = carHutApiSRService.addCarToDatabase(carHutCar).get();
        if (id != null) {
            controllerLogger.saveToFile("[CarHutApiSRController][OK]: /addCarToDatabase - Successfully executed.");
            return ResponseEntity.ok("Car was successfully added to database.");
        } else {
            controllerLogger.saveToFile("[CarHutApiSRController][ERROR]: /addCarToDatabase - Cannot save car to database.");
            return ResponseEntity.internalServerError().body("Cannot save car to database.");
        }
    }

    @GetMapping("/get-car-with-id")
    @ResponseBody
    public ResponseEntity<CarHutCar> getCarWithId(@RequestParam(name = "car-id") String carId)
            throws ExecutionException, InterruptedException {
        CarHutCar carHutCar = carHutApiSRService.getCarWithId(carId).get();
        if (carHutCar != null) {
            controllerLogger.saveToFile("[CarHutApiSRController][OK]: /getCarWithId - Successfully retrieved data.");
            return ResponseEntity.ok(carHutCar);
        } else {
            controllerLogger.saveToFile("[CarHutApiSRController][WARN]: /getCarWithId - Couldn't retrieve data");
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/get-cars-with-filters")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getCarsWithFilters(
            @RequestBody CarHutCarFilterModel carHutCarFilterModel,
            @RequestParam(name = "sort-by", required = false) String sortBy,
            @RequestParam(name = "sort-order", required = false) String sortOrder,
            @RequestParam(name = "offers-per-page", required = false) String offersPerPage,
            @RequestParam(name = "current-page", required = false) String currentPage
    ) throws ExecutionException, InterruptedException {
        List<CarHutCar> cars = carHutApiSRService.getCarsWithFilter(carHutCarFilterModel, sortBy, sortOrder,
                offersPerPage, currentPage).get();

        if (cars != null) {
            controllerLogger.saveToFile("[CarHutApiSRController][OK]: /getCarsWithFilters - Successfully retrieved data.");
            return ResponseEntity.ok(cars);
        } else {
            controllerLogger.saveToFile("[CarHutApiSRController][WARN]: /getCarsWithFilters - Couldn't retrieve data");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/get-number-of-filtered-cars")
    @ResponseBody
    public ResponseEntity<Integer> getNumberOfFilteredCars(@RequestBody CarHutCarFilterModel carHutCarFilterModel)
            throws ExecutionException, InterruptedException {
        Integer size = carHutApiSRService.getNumberOfFilteredCars(carHutCarFilterModel).get();
        controllerLogger.saveToFile("[CarHutApiSRController][OK]: /getNumberOfFilteredCars - Successfully retrieved data.");
        return ResponseEntity.ok(size);
    }

}
