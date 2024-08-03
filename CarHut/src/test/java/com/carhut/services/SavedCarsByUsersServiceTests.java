package com.carhut.services;

import com.carhut.models.carhut.CarHutCar;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SavedCarsByUsersServiceTests {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;
    @Autowired
    private CarHutAPIService carHutAPIService;
    private String bearerToken;
    private CarHutCar car;

}
