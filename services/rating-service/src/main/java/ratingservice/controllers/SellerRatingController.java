package ratingservice.controllers;

import models.requests.PrincipalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.dtos.requests.GiveSellerRatingRequestModel;
import ratingservice.dtos.responses.SellerRatingResponse;
import ratingservice.services.SellerRatingService;
import ratingservice.status.RatingServiceStatus;
import ratingservice.utils.loggers.ControllerLogger;

@RestController
@RequestMapping("/api/rating")
public class SellerRatingController {

    @Autowired
    private SellerRatingService sellerRatingService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getSellerRating")
    public SellerRatingResponse<SellerRatingDto> getSellerRating(@RequestParam String sellerId) {
        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(sellerId);
        if (sellerRatingDto != null) {
            controllerLogger.saveToFile("[SellerRatingController][OK] - /getSellerRating - Successfully got seller rating.");
            return new SellerRatingResponse<>("SUCCESS",
                    200,
                    "Successfully fetched seller's (" + sellerId + ") rating.",
                    sellerRatingDto);
        } else {
            controllerLogger.saveToFile("[SellerRatingController][ERROR] - /getSellerRating - Cannot get seller rating for sellerId: " + sellerId);
            return new SellerRatingResponse<>("ERROR",
                    500,
                    "Cannot fetch seller's (" + sellerId + ") rating.",
                    null);
        }
    }

    @PostMapping("/giveSellerRating")
    public SellerRatingResponse<Object> giveSellerRating(@RequestBody PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModel) {
        RatingServiceStatus ratingServiceStatusEntity = sellerRatingService.giveSellerRating(giveSellerRatingRequestModel);

        if (ratingServiceStatusEntity == RatingServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SellerRatingController][OK] - /giveSellerRating - Successfully given seller rating.");
            return new SellerRatingResponse<>("SUCCESS",
                    201,
                    "Successfully gave rating to seller.",
                    null);
        } else {
            controllerLogger.saveToFile("[SellerRatingController][ERROR] - /giveSellerRating - Something went wrong while adding new rating. Check post model. Also users might not exist.");
            return new SellerRatingResponse<>("ERROR",
                    500,
                    "Cannot give rating to seller.",
                    null);
        }
    }
}