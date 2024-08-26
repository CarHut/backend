package ratingservice.services;

import models.requests.PrincipalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.models.SellerRating;
import ratingservice.models.requests.GiveSellerRatingRequestModel;
import ratingservice.repositories.SellerRatingRepository;
import ratingservice.status.ServiceStatus;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class SellerRatingService {

    @Autowired
    private SellerRatingRepository sellerRatingRepository;
    public SellerRatingDto getSellerRating(String sellerId) {
        if (sellerId == null || sellerId.isEmpty()) {
            return null;
        }

        List<SellerRating> sellerRatings;
        try {
            sellerRatings = sellerRatingRepository.getSellerRatingsBySellerId(sellerId);
        } catch (Exception e) {
            return null;
        }

        OptionalDouble rating = sellerRatings
                .stream()
                .mapToInt(SellerRating::getRating)
                .average();

        if (rating.isEmpty()) {
            return null;
        }

        int numOfRatings = sellerRatings.size();

        return new SellerRatingDto(sellerId, rating.getAsDouble(), numOfRatings);
    }

    public ServiceStatus giveSellerRating(PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModel) {
        if (giveSellerRatingRequestModel == null) {
            return ServiceStatus.ERROR;
        }

        if (doesRatingAlreadyExist(giveSellerRatingRequestModel.getDto())) {
            return ServiceStatus.ERROR;
        }

        boolean ratingCheck = checkRating(giveSellerRatingRequestModel.getDto().getRating());
        boolean checkUsers = checkUsers(giveSellerRatingRequestModel);

        if (ratingCheck) {
            SellerRating sellerRating = new SellerRating(giveSellerRatingRequestModel.getDto().getRating(), giveSellerRatingRequestModel.getDto().getSellerId(), giveSellerRatingRequestModel.getDto().getUserId());
            sellerRatingRepository.save(sellerRating);
            return ServiceStatus.SUCCESS;
        }

        return ServiceStatus.ERROR;
    }

    private boolean checkUsers(PrincipalRequest<GiveSellerRatingRequestModel> giveSellerRatingRequestModel) {
        return !(giveSellerRatingRequestModel.getDto().getSellerId().equals(giveSellerRatingRequestModel.getDto().getUserId()));
    }

    private boolean doesRatingAlreadyExist(GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        SellerRating rating;

        try {
            rating = sellerRatingRepository.getRatingBySellerIdAndUserId(giveSellerRatingRequestModel.getSellerId(), giveSellerRatingRequestModel.getUserId());
        } catch (Exception e) {
            return false;
        }

        if (rating != null) {
            rating.setRating(giveSellerRatingRequestModel.getRating());
            sellerRatingRepository.save(rating);
            return true;
        }

        return false;
    }

    private boolean checkRating(int rating) {
        return rating >= 0 && rating <= 5;
    }

    public void flush() {
        sellerRatingRepository.deleteAll();
    }
}
