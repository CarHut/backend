package com.carhut.savedcarsservice.controllers;

import com.carhut.models.carhut.CarHutCar;
import com.carhut.savedcarsservice.services.SavedCarsByUsersService;
import com.carhut.savedcarsservice.status.SavedCarsServiceStatus;
import com.carhut.savedcarsservice.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.carhut.savedcarsservice.requests.GetSavedCarsByUserIdModel;
import com.carhut.savedcarsservice.requests.SaveCarRequestModel;

import java.util.List;

@Controller
@RequestMapping("/api/carhut/savedCars")
public class SavedCarsByUsersController {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @PostMapping("/getSavedCarsByUsername")
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUsername(@RequestBody GetSavedCarsByUserIdModel idModel) {
        List<CarHutCar> savedCars = savedCarsByUsersService.getSavedCarsByUsername(idModel);
        if (savedCars != null) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /getSavedCarsByUsername - Successfully got saved cars from database.");
            return ResponseEntity.ok(savedCars);
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /getSavedCarsByUsername - Couldn't retrieve saved cars from database.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/addSavedCarByUsername")
    public ResponseEntity<String> addSavedCarByUser(@RequestBody SaveCarRequestModel saveCarRequestModel) {
        SavedCarsServiceStatus savedCarsServiceStatus = savedCarsByUsersService.addSavedCarByUser(saveCarRequestModel);
        if (savedCarsServiceStatus == SavedCarsServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /addSavedCarByUsername - Successfully added car to saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - Couldn't save the car.");
            return ResponseEntity.internalServerError().body("Couldn't save the car.");
        }
    }

    @PostMapping("/removeSavedCarByUsername")
    public ResponseEntity<String> removeSavedCarByUsername(@RequestBody SaveCarRequestModel saveCarRequestModel) {
        SavedCarsServiceStatus savedCarsServiceStatus = savedCarsByUsersService.removeSavedCarByUsername(saveCarRequestModel);
        if (savedCarsServiceStatus == SavedCarsServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /removeSavedCarByUsername - Successfully removed car from saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - Couldn't remove the car.");
            return ResponseEntity.internalServerError().body("Couldn't remove the car.");
        }
    }
}
