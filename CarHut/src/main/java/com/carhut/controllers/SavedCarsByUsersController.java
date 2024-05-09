package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.services.SavedCarsByUsersService;
import com.carhut.temputils.models.TempCarModel;
import com.carhut.util.exceptions.CarHutException;
import com.carhut.util.loggers.ControllerLogger;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/carhut/savedCars")
public class SavedCarsByUsersController {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @Deprecated
    @PostMapping("/getSavedTempCarsByUsername")
    public ResponseEntity<List<TempCarModel>> getSavedTempCarsByUsername(@RequestBody String username) {
        return ResponseEntity.ok(savedCarsByUsersService.getSavedTempCarsByUserUsername(username));
    }

    @PostMapping("/getSavedCarsByUsername")
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUsername(@RequestBody String username) {
        try {
            List<CarHutCar> savedCars = savedCarsByUsersService.getSavedCarsByUsername(username);
            if (savedCars != null) {
                controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /getSavedCarsByUsername - Successfully got saved cars from database.");
                return ResponseEntity.ok(savedCars);
            } else {
                controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /getSavedCarsByUsername - Couldn't retrieve saved cars from database.");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (CarHutException e) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][ERROR]: /getSavedCarsByUsername - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/addSavedCarByUsername")
    public ResponseEntity<String> addSavedCarByUser(@RequestBody SavedCarByUser savedCarByUser) {
        try {
            RequestStatusEntity requestStatusEntity = savedCarsByUsersService.addSavedCarByUser(savedCarByUser);
            if (requestStatusEntity == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /addSavedCarByUsername - Successfully added car to saved entities.");
                return ResponseEntity.ok("Car was saved to wishlist.");
            } else {
                controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - Couldn't save the car.");
                return ResponseEntity.internalServerError().body("Couldn't save the car.");
            }
        }
        catch (CarHutException e) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][ERROR]: /addSavedCarByUsername - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/removeSavedCarByUsername")
    public ResponseEntity<String> removeSavedCarByUsername(@RequestBody SavedCarByUser savedCarByUser) {
        try {
            RequestStatusEntity requestStatusEntity = savedCarsByUsersService.removeSavedCarByUsername(savedCarByUser);
            if (requestStatusEntity == RequestStatusEntity.SUCCESS) {
                controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /removeSavedCarByUsername - Successfully removed car from saved entities.");
                return ResponseEntity.ok("Car was saved to wishlist.");
            } else {
                controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - Couldn't remove the car.");
                return ResponseEntity.internalServerError().body("Couldn't remove the car.");
            }
        } catch (CarHutException e) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][ERROR]: /removeSavedCarByUsername - Internal error. Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
