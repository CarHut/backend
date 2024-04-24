package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.services.SavedCarsByUsersService;
import com.carhut.temputils.models.TempCarModel;
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

    @Deprecated
    @PostMapping("/getSavedTempCarsByUsername")
    public ResponseEntity<List<TempCarModel>> getSavedTempCarsByUsername(@RequestBody String username) {
        return ResponseEntity.ok(savedCarsByUsersService.getSavedTempCarsByUserUsername(username));
    }

    @PostMapping("/getSavedCarsByUsername")
    public ResponseEntity<List<CarHutCar>> getSavedCarsByUsername(@RequestBody String username) {
        return ResponseEntity.ok(savedCarsByUsersService.getSavedCarsByUsername(username));
    }

    @PostMapping("/addSavedCarByUsername")
    public ResponseEntity<String> addSavedCarByUser(@RequestBody SavedCarByUser savedCarByUser) {
        return savedCarsByUsersService.addSavedCarByUser(savedCarByUser) == RequestStatusEntity.SUCCESS
                ? ResponseEntity.ok("Car was saved to wishlist.")
                : ResponseEntity.internalServerError().body("Something went wrong when saving car to wishlist.");
    }

    @PostMapping("/removeSavedCarByUsername")
    public ResponseEntity<String> removeSavedCarByUsername(@RequestBody SavedCarByUser savedCarByUser) {
        return savedCarsByUsersService.removeSavedCarByUsername(savedCarByUser) == RequestStatusEntity.SUCCESS
                ? ResponseEntity.ok("Car was removed from wishlist.")
                : ResponseEntity.internalServerError().body("Something went wrong while removing car from wishlist.");
    }
}
