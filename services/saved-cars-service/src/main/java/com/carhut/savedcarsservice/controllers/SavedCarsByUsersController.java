package com.carhut.savedcarsservice.controllers;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.savedcarsservice.services.SavedCarsByUsersService;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.carhut.savedcarsservice.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.carhut.savedcarsservice.requests.SaveCarRequestModel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/saved-cars-service")
public class SavedCarsByUsersController {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/get-saved-cars-by-user-id")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUserId(@RequestHeader("Authorization") String bearerToken,
                                                                @RequestBody String userId)
            throws ExecutionException, InterruptedException, IOException {

        List<CarHutCar> savedCars = savedCarsByUsersService.getSavedCarsByUsername(userId, bearerToken).get();
        if (savedCars != null) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /getSavedCarsByUsername - Successfully got saved cars from database.");
            return ResponseEntity.ok(savedCars);
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /getSavedCarsByUsername - Couldn't retrieve saved cars from database.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/add-saved-car-by-user-id")
    @ResponseBody
    public ResponseEntity<String> addSavedCarByUserId(@RequestHeader("Authorization") String bearerToken,
                                                      @RequestBody SaveCarRequestModel saveCarRequestModel)
            throws ExecutionException, InterruptedException, IOException {

        SavedCarsServiceStatus savedCarsServiceStatus = savedCarsByUsersService
                .addSavedCarByUser(saveCarRequestModel, bearerToken).get();
        if (savedCarsServiceStatus == SavedCarsServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /addSavedCarByUsername - Successfully added car to saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else if (savedCarsServiceStatus == SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - User is not authenticated.");
            return ResponseEntity.ok("User is not authenticated.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - Couldn't save the car.");
            return ResponseEntity.internalServerError().body("Couldn't save the car.");
        }
    }

    @PostMapping("/remove-saved-car-by-user-id")
    @ResponseBody
    public ResponseEntity<String> removeSavedCarByUserId(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestBody SaveCarRequestModel saveCarRequestModel)
            throws ExecutionException, InterruptedException, IOException {

        SavedCarsServiceStatus savedCarsServiceStatus = savedCarsByUsersService
                .removeSavedCarByUsername(saveCarRequestModel, bearerToken).get();
        if (savedCarsServiceStatus == SavedCarsServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /removeSavedCarByUsername - Successfully removed car from saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else if (savedCarsServiceStatus == SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - User is not authenticated.");
            return ResponseEntity.ok("User is not authenticated.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - Couldn't remove the car.");
            return ResponseEntity.internalServerError().body("Couldn't remove the car.");
        }
    }
}
