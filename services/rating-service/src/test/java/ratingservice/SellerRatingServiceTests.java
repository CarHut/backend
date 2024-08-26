package ratingservice;

import models.principals.AuthenticationPrincipal;
import models.requests.PrincipalRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.models.requests.GiveSellerRatingRequestModel;
import ratingservice.services.SellerRatingService;
import ratingservice.status.ServiceStatus;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SellerRatingServiceTests {

    @Autowired
    private SellerRatingService sellerRatingService;

    @AfterEach
    public void removeRating() {
        sellerRatingService.flush();
    }

    @Test
    public void giveSellerRating_requestIsNull() {
        ServiceStatus status = sellerRatingService.giveSellerRating(null);
        Assertions.assertEquals(ServiceStatus.ERROR, status);
    }

    @Test
    public void giveSellerRating_ratingRequestIsInvalid() {
        GiveSellerRatingRequestModel giveSellerRatingRequestModel = new GiveSellerRatingRequestModel();
        giveSellerRatingRequestModel.setRating(100);
        giveSellerRatingRequestModel.setUsername("ivalid_username");
        giveSellerRatingRequestModel.setSellerId("random");
        giveSellerRatingRequestModel.setUserId("random");
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModelPrincipalRequest = new PrincipalRequest<>();
        giveSellerRatingRequestModelPrincipalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        giveSellerRatingRequestModelPrincipalRequest.setDto(giveSellerRatingRequestModel);

        ServiceStatus status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModelPrincipalRequest);
        Assertions.assertEquals(ServiceStatus.ERROR, status);
    }

    @Test
    public void giveSellerRating_validScenario() {
        GiveSellerRatingRequestModel giveSellerRatingRequestModel = new GiveSellerRatingRequestModel();
        giveSellerRatingRequestModel.setRating(4);
        giveSellerRatingRequestModel.setUsername("admin");
        giveSellerRatingRequestModel.setSellerId("user1");
        giveSellerRatingRequestModel.setUserId("user0");
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModelPrincipalRequest = new PrincipalRequest<>();
        giveSellerRatingRequestModelPrincipalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        giveSellerRatingRequestModelPrincipalRequest.setDto(giveSellerRatingRequestModel);

        ServiceStatus status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModelPrincipalRequest);
        Assertions.assertEquals(ServiceStatus.SUCCESS, status);
    }

    @Test
    public void getSellerRating_idIsNull() {
        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(null);
        Assertions.assertNull(sellerRatingDto);
    }

    @Test
    public void getSellerRating_idIsInvalid() {
        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating("random_id");
        Assertions.assertNull(sellerRatingDto);
    }

    @Test
    public void getSellerRating_validScenario() throws InterruptedException {
        giveSellerRating_validScenario();

        Thread.sleep(1000);

        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating("user1");
        Assertions.assertNotNull(sellerRatingDto);
        Assertions.assertEquals(4.0, sellerRatingDto.rating());
        Assertions.assertEquals(1, sellerRatingDto.numOfRatings());
    }

}
