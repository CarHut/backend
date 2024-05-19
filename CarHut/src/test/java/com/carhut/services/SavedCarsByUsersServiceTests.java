package com.carhut.services;
//
//import com.carhut.models.carhut.CarHutCar;
//import com.carhut.util.exceptions.CarHutException;
//import com.carhut.util.exceptions.carhutapi.CarHutAPICarNotFoundException;
//import com.carhut.util.exceptions.carhutapi.CarHutAPIException;
//import com.carhut.util.exceptions.savedcars.SavedCarsNotFoundException;
//import com.carhut.util.exceptions.usercredentials.UserCredentialsNotFoundException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.UUID;

@SpringBootTest
public class SavedCarsByUsersServiceTests {
//
//    @Autowired
//    private SavedCarsByUsersService savedCarsByUsersService;
//
//
//    //Currently admin has 0 saved cars, if changed update this test
//    @Test
//    public void testGetSavedCarsByUsername_validUsername() {
//
//        try {
//            List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername("admin");
//            Assertions.assertNotNull(cars, "List of saved cars for username admin is null");
//            Assertions.assertEquals(0, cars.size(), "Size of saved cars for username admin is not 0");
//        }
//        catch (CarHutException e) {
//
//        }
//    }
//
//    @Test
//    public void testGetSavedCarsByUsername_notValidUsername() {
//        try {
//            List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(UUID.randomUUID().toString());
//            Assertions.assertNotNull(cars, "List of saved cars for nonexistant user is null");
//            Assertions.assertEquals(0, cars.size(), "Size of saved cars for nonexistant user admin is not 0");
//        }
//        catch (CarHutException e) {
//
//        }
//    }
//
}
