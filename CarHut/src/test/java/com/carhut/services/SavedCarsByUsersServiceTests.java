package com.carhut.services;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.savedcars.SavedCarsCanNotBeSavedException;
import com.carhut.util.exceptions.usercredentials.UserCredentialsNotFoundException;
import com.carhut.utils.AuthLogger;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SavedCarsByUsersServiceTests {

    @Autowired
    private SavedCarsByUsersService savedCarsByUsersService;
    private String bearerToken;

    @BeforeAll
    public void logIntoCarHutAPI() throws Exception {
        bearerToken = AuthLogger.loginToCarHutAPI();
        Thread.sleep(1000);
        if (bearerToken == null) {
            throw new Exception("Cannot run tests for CarHutAPIService because test unit couldn't login to server.");
        }
    }

    @AfterAll
    public void logout() {
        boolean state = AuthLogger.logout();
        if (!state) {
            throw new RuntimeException("Cannot logout from server. Tests automatically failed.");
        }
    }

}
