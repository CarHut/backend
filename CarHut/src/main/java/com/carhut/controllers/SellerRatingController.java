package com.carhut.controllers;

import com.carhut.dtos.SellerRatingDto;
import com.carhut.models.requestmodels.GiveSellerRatingRequestModel;
import com.carhut.services.SellerRatingService;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.rating.RatingException;
import com.carhut.util.loggers.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class SellerRatingController {

    @Autowired
    private SellerRatingService sellerRatingService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getSellerRating")
    public ResponseEntity<SellerRatingDto> getSellerRating(@RequestParam String sellerId) {
        try {
            SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(sellerId);
            if (sellerRatingDto != null) {
                controllerLogger.saveToFile("[SellerRatingController][OK] - /giveSellerRating - Successfully got seller rating.");
                return ResponseEntity.ok(sellerRatingDto);
            } else {
                controllerLogger.saveToFile("[SellerRatingController][WARN] - /giveSellerRating - Cannot get seller rating for sellerId: " + sellerId);
                return ResponseEntity.status(404).body(null);
            }
        } catch (RatingException e) {
            controllerLogger.saveToFile("[SellerRatingController][ERROR] - /giveSellerRating - Something went wrong while getting seller rating sellerId=" + sellerId +". Check post model. Also users might not exist. Error message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/giveSellerRating")
    public ResponseEntity<String> giveSellerRating(@RequestBody GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        try {
            boolean status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModel);

            if (status) {
                controllerLogger.saveToFile("[SellerRatingController][OK] - /giveSellerRating - Successfully given seller rating.");
                return ResponseEntity.ok().body("Successfully added new rating for seller with id: " + giveSellerRatingRequestModel.getSellerId());
            } else {
                controllerLogger.saveToFile("[SellerRatingController][WARN] - /giveSellerRating - Something went wrong while adding new rating. Check post model. Also users might not exist.");
                return ResponseEntity.status(404).body("Something went wrong while adding new rating. Check post model. Also users might not exist.");
            }
        } catch (RatingException e) {
            controllerLogger.saveToFile("[SellerRatingController][ERROR] - /giveSellerRating - Internal error. Error message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        } catch (CarHutAuthenticationException e) {
            controllerLogger.saveToFile("[SellerRatingController][WARN] - /giveSellerRating - Unauthorized access. Error message: " + e.getMessage());
            return ResponseEntity.status(403).body(null);
        }
    }
}
