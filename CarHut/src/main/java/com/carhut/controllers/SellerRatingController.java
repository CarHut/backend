package com.carhut.controllers;

import com.carhut.dtos.SellerRatingDto;
import com.carhut.models.requestmodels.GiveSellerRatingRequestModel;
import com.carhut.services.SellerRatingService;
import com.carhut.util.exceptions.rating.RatingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class SellerRatingController {

    @Autowired
    private SellerRatingService sellerRatingService;

    @GetMapping("/getSellerRating")
    public ResponseEntity<SellerRatingDto> getSellerRating(@RequestParam String sellerId) {
        try {
            SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(sellerId);
            return ResponseEntity.ok(sellerRatingDto);
        } catch (RatingException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/giveSellerRating")
    public ResponseEntity<String> giveSellerRating(@RequestBody GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        try {
            boolean status = sellerRatingService.giveSellerRating(giveSellerRatingRequestModel);

            if (status) {
                return ResponseEntity.ok().body("Successfully added new rating for seller with id: " + giveSellerRatingRequestModel.getSellerId());
            } else {
                return ResponseEntity.status(404).body("Something went wrong while adding new rating. Check post model. Also users might not exist.");
            }
        } catch (RatingException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
