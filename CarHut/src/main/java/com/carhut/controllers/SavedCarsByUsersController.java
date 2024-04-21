package com.carhut.controllers;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.SavedCarByUser;
import com.carhut.services.SavedCarsByUsersService;
import com.carhut.temputils.models.TempCarModel;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/carhut/savedCars")
public class SavedCarsByUsersController {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;

    @PostMapping("/getSavedCarsByUsername")
    public ResponseEntity<List<TempCarModel>> getSavedCarsByUsername(@RequestBody String username) {
        return ResponseEntity.ok(savedCarsByUsersService.getSavedCarsByUserUsername(username));
    }

    @PostMapping("/addSavedCarByUsername")
    public ResponseEntity<String> addSavedCarByUser(@RequestBody SavedCarByUser savedCarByUser) {
        return savedCarsByUsersService.addSavedCarByUser(savedCarByUser) == RequestStatusEntity.SUCCESS
                ? ResponseEntity.ok("Car was saved to wishlist.")
                : ResponseEntity.internalServerError().body("Something went wrong when saving car to wishlist.");
    }
}
