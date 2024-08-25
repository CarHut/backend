package ratingservice.controllers;

import models.requests.PrincipalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.models.requests.GiveSellerRatingRequestModel;
import ratingservice.services.SellerRatingService;
import ratingservice.status.ServiceStatus;
import ratingservice.utils.loggers.ControllerLogger;

@RestController
@RequestMapping("/api/rating")
public class SellerRatingController {

    @Autowired
    private SellerRatingService sellerRatingService;
    private final ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping("/getSellerRating")
    public ResponseEntity<SellerRatingDto> getSellerRating(@RequestParam String sellerId) {
        SellerRatingDto sellerRatingDto = sellerRatingService.getSellerRating(sellerId);
        if (sellerRatingDto != null) {
            controllerLogger.saveToFile("[SellerRatingController][OK] - /getSellerRating - Successfully got seller rating.");
            return ResponseEntity.ok(sellerRatingDto);
        } else {
            controllerLogger.saveToFile("[SellerRatingController][WARN] - /getSellerRating - Cannot get seller rating for sellerId: " + sellerId);
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/giveSellerRating")
    public ResponseEntity<String> giveSellerRating(@RequestBody PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModel) {
        ServiceStatus serviceStatusEntity = sellerRatingService.giveSellerRating(giveSellerRatingRequestModel);

        if (serviceStatusEntity == ServiceStatus.SUCCESS) {
            controllerLogger.saveToFile("[SellerRatingController][OK] - /giveSellerRating - Successfully given seller rating.");
            return ResponseEntity.ok().body("Successfully added new rating for seller with id: " + giveSellerRatingRequestModel.getDto().getSellerId());
        } else {
            controllerLogger.saveToFile("[SellerRatingController][WARN] - /giveSellerRating - Something went wrong while adding new rating. Check post model. Also users might not exist.");
            return ResponseEntity.status(404).body("Something went wrong while adding new rating. Check post model. Also users might not exist.");
        }
    }
}
