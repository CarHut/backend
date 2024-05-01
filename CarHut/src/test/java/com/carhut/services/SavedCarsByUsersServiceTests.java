package com.carhut.services;

import com.carhut.models.carhut.CarHutCar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
public class SavedCarsByUsersServiceTests {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;


    //Currently admin has 0 saved cars, if changed update this test
    @Test
    public void testGetSavedCarsByUsername_validUsername() {
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername("admin");

        Assertions.assertNotNull(cars, "List of saved cars for username admin is null");
        Assertions.assertEquals(0, cars.size(), "Size of saved cars for username admin is not 0");
    }

    @Test
    public void testGetSavedCarsByUsername_notValidUsername() {
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(UUID.randomUUID().toString());

        Assertions.assertNotNull(cars, "List of saved cars for nonexistant user is null");
        Assertions.assertEquals(0, cars.size(), "Size of saved cars for nonexistant user admin is not 0");
    }
    
}
