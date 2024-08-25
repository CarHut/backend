package ratingservice;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ratingservice.services.SellerRatingService;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SellerRatingServiceTests {

    @Autowired
    private SellerRatingService sellerRatingService;

//    @BeforeAll
//    public void generateUser() {
//        String id = UUID.randomUUID().toString();
//        String username = "user";
//        String password = "password";
//        String email = username + "@example.com";
//        boolean isActive = true;
//        String firstName = "FirstName";
//        String surname = "Surname";
//        Date dateRegistered = new Date(System.currentTimeMillis());
//        Integer numOfOfferedCars = 0;
//        String registrationType = "standard";
//
//        user = new User(id, username, password, email, firstName, surname, dateRegistered, numOfOfferedCars, isActive, new Authority(2, "ROLE_USER"), registrationType);
//        userCredentialsRepository.save(user);
//    }
//
//    @AfterAll
//    public void removeUser() throws InterruptedException {
//        sellerRatingService.flush();
//        Thread.sleep(1000);
//        userCredentialsRepository.delete(user);
//    }
//
//    @AfterEach
//    public void flushRatings() {
//        sellerRatingService.flush();
//    }
//
//    @Test
//    public void giveSellerRating_requestIsNull() {
//        ServiceStatusEntity status = sellerRatingService.giveSellerRating(null);
//        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
//    }
//
//    @Test
//    public void giveSellerRating_ratingRequestIsInvalid() {
//        GiveSellerRatingRequestModel giveSellerRatingRequestModel = new GiveSellerRatingRequestModel();
//        giveSellerRatingRequestModel.setRating(100);
//        giveSellerRatingRequestModel.setUsername("ivalid_username");
//        giveSellerRatingRequestModel.setSellerId("random");
//        giveSellerRatingRequestModel.setUserId("random");
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModelPrincipalRequest = new PrincipalRequest<>();
//        giveSellerRatingRequestModelPrincipalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        giveSellerRatingRequestModelPrincipalRequest.setDto(giveSellerRatingRequestModel);
//
//        ServiceStatusEntity status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModelPrincipalRequest);
//        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
//    }
//
//    @Test
//    public void giveSellerRating_validScenario() {
//        GiveSellerRatingRequestModel giveSellerRatingRequestModel = new GiveSellerRatingRequestModel();
//        giveSellerRatingRequestModel.setRating(4);
//        giveSellerRatingRequestModel.setUsername("admin");
//        giveSellerRatingRequestModel.setSellerId(user.getId());
//        giveSellerRatingRequestModel.setUserId("user0");
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModelPrincipalRequest = new PrincipalRequest<>();
//        giveSellerRatingRequestModelPrincipalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        giveSellerRatingRequestModelPrincipalRequest.setDto(giveSellerRatingRequestModel);
//
//        ServiceStatusEntity status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModelPrincipalRequest);
//        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, status);
//    }
//
//    @Test
//    public void getSellerRating_idIsNull() {
//        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(null);
//        Assertions.assertNull(sellerRatingDto);
//    }
//
//    @Test
//    public void getSellerRating_idIsInvalid() {
//        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating("random_id");
//        Assertions.assertNull(sellerRatingDto);
//    }
//
//    @Test
//    public void getSellerRating_validScenario() throws InterruptedException {
//        giveSellerRating_validScenario();
//
//        Thread.sleep(1000);
//
//        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(user.getId());
//        Assertions.assertNotNull(sellerRatingDto);
//        Assertions.assertEquals(4.0, sellerRatingDto.rating());
//        Assertions.assertEquals(1, sellerRatingDto.numOfRatings());
//    }

}
