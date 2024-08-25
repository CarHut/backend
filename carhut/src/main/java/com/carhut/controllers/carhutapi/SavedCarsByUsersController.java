package com.carhut.controllers.carhutapi;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveCarRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.services.carhutapi.SavedCarsByUsersService;
import com.carhut.util.exceptions.CarHutException;
import com.carhut.util.loggers.ControllerLogger;
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

    @PostMapping("/getSavedCarsByUsername")
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUsername(@RequestBody PrincipalRequest<SimpleUsernameRequestModel> username) {
        List<CarHutCar> savedCars = savedCarsByUsersService.getSavedCarsByUsername(username);
        if (savedCars != null) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /getSavedCarsByUsername - Successfully got saved cars from database.");
            return ResponseEntity.ok(savedCars);
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /getSavedCarsByUsername - Couldn't retrieve saved cars from database.");
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/addSavedCarByUsername")
    public ResponseEntity<String> addSavedCarByUser(@RequestBody PrincipalRequest<SaveCarRequestModel> saveCarRequestModelPrincipalRequest) {
        ServiceStatusEntity serviceStatusEntity = savedCarsByUsersService.addSavedCarByUser(saveCarRequestModelPrincipalRequest);
        if (serviceStatusEntity == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /addSavedCarByUsername - Successfully added car to saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /addSavedCarByUsername - Couldn't save the car.");
            return ResponseEntity.internalServerError().body("Couldn't save the car.");
        }
    }

    @PostMapping("/removeSavedCarByUsername")
    public ResponseEntity<String> removeSavedCarByUsername(@RequestBody PrincipalRequest<SaveCarRequestModel> saveCarRequestModelPrincipalRequest) {
        ServiceStatusEntity serviceStatusEntity = savedCarsByUsersService.removeSavedCarByUsername(saveCarRequestModelPrincipalRequest);
        if (serviceStatusEntity == ServiceStatusEntity.SUCCESS) {
            controllerLogger.saveToFile("[SavedCarsByUsersController][OK]: /removeSavedCarByUsername - Successfully removed car from saved entities.");
            return ResponseEntity.ok("Car was saved to wishlist.");
        } else {
            controllerLogger.saveToFile("[SavedCarsByUsersController][WARN]: /removeSavedCarByUsername - Couldn't remove the car.");
            return ResponseEntity.internalServerError().body("Couldn't remove the car.");
        }
    }
}
