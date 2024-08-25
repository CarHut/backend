package com.carhut.services;

import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveCarRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.AuthenticationPrincipal;
import com.carhut.services.carhutapi.CarHutAPIService;
import com.carhut.services.carhutapi.SavedCarsByUsersService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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
    private CarHutCar car = new CarHutCar(
            "user0", // Please add seller name due to logic of adding car to database (id=user0,name=admin)
            "Test address",
            1,
            1,
            "Test header",
            "Test price",
            null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null,
            null, null, null, "color0", "color0", null, false, false,
            null, null, null, null, null
    );

    @BeforeEach
    public void addCarToDatabase() throws InterruptedException {
        carHutAPIService.save(car);
    }

    @AfterEach
    public void removeCarFromDatabase() throws InterruptedException {
        savedCarsByUsersService.flush();
        Thread.sleep(1000);
        carHutAPIService.delete(car);
    }

    @Test
    public void addSavedCarByUser_requestIsNull() {
        ServiceStatusEntity status = savedCarsByUsersService.addSavedCarByUser(null);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
    }

    @Test
    public void addSavedCarByUser_userDoesNotExist() {
        PrincipalRequest<SaveCarRequestModel> principalRequest = new PrincipalRequest<>();
        SaveCarRequestModel saveCarRequestModel = new SaveCarRequestModel();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("INVALID_USER_ID");
        saveCarRequestModel.setCarId("RANDOM_ID");
        saveCarRequestModel.setUsername("INVALID_USER_ID");
        principalRequest.setDto(saveCarRequestModel);
        ServiceStatusEntity status = savedCarsByUsersService.addSavedCarByUser(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.OBJECT_NOT_FOUND, status);
    }

    @Test
    public void addSavedCarByUser_validScenario() {
        PrincipalRequest<SaveCarRequestModel> principalRequest = new PrincipalRequest<>();
        SaveCarRequestModel saveCarRequestModel = new SaveCarRequestModel();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        saveCarRequestModel.setCarId(car.getId());
        saveCarRequestModel.setUsername("admin");
        principalRequest.setDto(saveCarRequestModel);
        ServiceStatusEntity status = savedCarsByUsersService.addSavedCarByUser(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, status);
    }

    @Test
    public void getSavedCarsByUsername_requestIsNull() {
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(null);
        Assertions.assertNull(cars);
    }

    @Test
    public void getSavedCarsByUsername_userNameIsNull() {
        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
        simpleUsernameRequestModel.setUsername(null);
        authenticationPrincipal.setUsername(null);
        principalRequest.setDto(simpleUsernameRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(principalRequest);
        Assertions.assertNull(cars);
    }

    @Test
    public void getSavedCarsByUsername_usernameDoesNotExist() {
        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
        simpleUsernameRequestModel.setUsername("INVALID_USERNAME");
        authenticationPrincipal.setUsername("INVALID_USERNAME");
        principalRequest.setDto(simpleUsernameRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(principalRequest);
        Assertions.assertNotNull(cars);
        Assertions.assertTrue(cars.isEmpty());
    }

    @Test
    public void getSavedCarsByUsername_validScenario() throws InterruptedException {
        addSavedCarByUser_validScenario();

        Thread.sleep(1000);

        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
        simpleUsernameRequestModel.setUsername("admin");
        authenticationPrincipal.setUsername("admin");
        principalRequest.setDto(simpleUsernameRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        List<CarHutCar> cars = savedCarsByUsersService.getSavedCarsByUsername(principalRequest);
        Assertions.assertNotNull(cars);
        Assertions.assertFalse(cars.isEmpty());
    }

    @Test
    public void removeSavedCarByUsername_requestIsNull() {
        ServiceStatusEntity status = savedCarsByUsersService.removeSavedCarByUsername(null);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
    }

    @Test
    public void removeSavedCarByUsername_usernameIsNull() {
        PrincipalRequest<SaveCarRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SaveCarRequestModel saveCarRequestModel = new SaveCarRequestModel();
        saveCarRequestModel.setUsername(null);
        authenticationPrincipal.setUsername(null);
        principalRequest.setDto(saveCarRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        ServiceStatusEntity serviceStatusEntity = savedCarsByUsersService.removeSavedCarByUsername(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, serviceStatusEntity);
    }

    @Test
    public void removeSavedCarByUsername_usernameDoesNotExist() {
        PrincipalRequest<SaveCarRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SaveCarRequestModel saveCarRequestModel = new SaveCarRequestModel();
        saveCarRequestModel.setUsername("INVALID_USERNAME");
        authenticationPrincipal.setUsername("INVALID_USERNAME");
        principalRequest.setDto(saveCarRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        ServiceStatusEntity serviceStatusEntity = savedCarsByUsersService.removeSavedCarByUsername(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.OBJECT_NOT_FOUND, serviceStatusEntity);
    }

    @Test
    public void removeSavedCarByUsername_validScenario() {
        PrincipalRequest<SaveCarRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        SaveCarRequestModel saveCarRequestModel = new SaveCarRequestModel();
        saveCarRequestModel.setCarId(car.getId());
        saveCarRequestModel.setUsername("admin");
        authenticationPrincipal.setUsername("admin");
        principalRequest.setDto(saveCarRequestModel);
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        ServiceStatusEntity serviceStatusEntity = savedCarsByUsersService.removeSavedCarByUsername(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, serviceStatusEntity);
    }

}
