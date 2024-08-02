package com.carhut.services;

import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.SavedCarByUser;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.savedcars.SavedCarsCanNotBeSavedException;
import com.carhut.util.exceptions.usercredentials.UserCredentialsNotFoundException;
import com.carhut.utils.AuthLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
    public void logIntoCarHutAPI() throws IOException, InterruptedException {
        AuthLogger authLogger = new AuthLogger();
        bearerToken = authLogger.loginToCarHutAPI();
        Assertions.assertNotNull(bearerToken);
    }

    @Test
    public void addSavedCarByUser_modelIsNull() throws UserCredentialsNotFoundException, SavedCarsCanNotBeSavedException, CarHutAuthenticationException {
        SavedCarByUser savedCarByUser = null;
        RequestStatusEntity requestStatusEntity = savedCarsByUsersService.addSavedCarByUser(savedCarByUser);
        Assertions.assertEquals(RequestStatusEntity.ERROR, requestStatusEntity);
    }

}
