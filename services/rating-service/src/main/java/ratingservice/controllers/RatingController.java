package ratingservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.dtos.GiveSellerRatingRequestModel;
import ratingservice.services.RatingService;
import ratingservice.status.RatingServiceStatus;
import ratingservice.utils.loggers.RatingServiceLogger;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rating-service")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    private final RatingServiceLogger logger = RatingServiceLogger.getInstance();

    @GetMapping("/get-seller-rating")
    @ResponseBody
    public ResponseEntity<SellerRatingDto> getSellerRating(@RequestParam("seller-id") String sellerId)
            throws ExecutionException, InterruptedException {

        SellerRatingDto sellerRatingDto = ratingService.getSellerRating(sellerId).get();
        if (sellerRatingDto != null) {
            logger.logInfo("[SellerRatingController][OK] - /getSellerRating - Successfully got seller rating.");
            return ResponseEntity.ok(sellerRatingDto);
        } else {
            logger.logError("[SellerRatingController][ERROR] - /getSellerRating - Cannot get seller rating for sellerId: " + sellerId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/give-seller-rating")
    public ResponseEntity<String> giveSellerRating(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody GiveSellerRatingRequestModel giveSellerRatingRequestModel)
            throws ExecutionException, InterruptedException {

        RatingServiceStatus ratingServiceStatusEntity = ratingService
                .giveSellerRating(giveSellerRatingRequestModel, bearerToken).get();

        if (ratingServiceStatusEntity == RatingServiceStatus.SUCCESS) {
            logger.logInfo("[SellerRatingController][OK] - /giveSellerRating - Successfully given seller rating.");
            return ResponseEntity.status(201).body("Successfully gave rating to seller.");
        } else {
            logger.logError("[SellerRatingController][ERROR] - /giveSellerRating - Something went wrong while adding new rating. Check post model. Also users might not exist.");
            return ResponseEntity.internalServerError().body("Cannot give rating to seller.");
        }
    }
}
