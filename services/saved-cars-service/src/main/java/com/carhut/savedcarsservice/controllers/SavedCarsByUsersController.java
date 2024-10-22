package com.carhut.savedcarsservice.controllers;

import com.carhut.commons.model.CarHutCar;
import com.carhut.savedcarsservice.services.SavedCarsByUsersService;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.carhut.savedcarsservice.util.loggers.SavedCarsServiceLogger;
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
    private final SavedCarsServiceLogger logger = SavedCarsServiceLogger.getInstance();

    @GetMapping("/get-saved-cars-by-user-id")
    @ResponseBody
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUserId(@RequestHeader("Authorization") String bearerToken,
                                                                @RequestParam("user-id") String userId)
            throws ExecutionException, InterruptedException {

        List<CarHutCar> savedCars = savedCarsByUsersService.getSavedCarsByUserId(userId, bearerToken).get();
        if (savedCars != null) {
            logger.logInfo("[SavedCarsByUsersController][OK]: /getSavedCarsByUserId - Successfully got saved cars from database.");
            return ResponseEntity.ok(savedCars);
        } else {
            logger.logWarn("[SavedCarsByUsersController][WARN]: /getSavedCarsByUserId - Couldn't retrieve saved cars from database.");
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
            logger.logInfo("[SavedCarsByUsersController][OK]: /addSavedCarByUsername - Successfully added car to saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else if (savedCarsServiceStatus == SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            logger.logWarn("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - User is not authenticated.");
            return ResponseEntity.ok("User is not authenticated.");
        } else {
            logger.logWarn("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - Couldn't save the car.");
            return ResponseEntity.internalServerError().body("Couldn't save the car.");
        }
    }

    @PostMapping("/remove-saved-car-by-user-id")
    @ResponseBody
    public ResponseEntity<String> removeSavedCarByUserId(@RequestHeader("Authorization") String bearerToken,
                                                         @RequestBody SaveCarRequestModel saveCarRequestModel)
            throws ExecutionException, InterruptedException {

        SavedCarsServiceStatus savedCarsServiceStatus = savedCarsByUsersService
                .removeSavedCarByUsername(saveCarRequestModel, bearerToken).get();
        if (savedCarsServiceStatus == SavedCarsServiceStatus.SUCCESS) {
            logger.logInfo("[SavedCarsByUsersController][OK]: /removeSavedCarByUsername - Successfully removed car from saved entities.");
            return ResponseEntity.ok("Successfully removed car from saved entities.");
        } else if (savedCarsServiceStatus == SavedCarsServiceStatus.USER_IS_NOT_AUTHENTICATED) {
            logger.logWarn("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - User is not authenticated.");
            return ResponseEntity.ok("User is not authenticated.");
        } else {
            logger.logWarn("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - Couldn't remove the car.");
            return ResponseEntity.internalServerError().body("Couldn't remove the car.");
        }
    }
}
