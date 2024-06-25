package com.carhut.services;

import com.carhut.database.repository.SellerRatingRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.dtos.SellerRatingDto;
import com.carhut.models.carhut.SellerRating;
import com.carhut.models.requestmodels.GiveSellerRatingRequestModel;
import com.carhut.models.security.User;
import com.carhut.util.exceptions.rating.CannotFindUserForRatingException;
import com.carhut.util.exceptions.rating.CannotGetAverageRatingException;
import com.carhut.util.exceptions.rating.CannotGetRatingsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;

@Service
public class SellerRatingService {

    @Autowired
    private SellerRatingRepository sellerRatingRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public SellerRatingDto getSellerRating(String sellerId) throws CannotGetRatingsException, CannotGetAverageRatingException {
        List<SellerRating> sellerRatings;
        try {
            sellerRatings = sellerRatingRepository.getSellerRatingsBySellerId(sellerId);
        } catch (Exception e) {
            throw new CannotGetRatingsException("Cannot get ratings from database for user with id: " + sellerId + ". Error message: " + e.getMessage());
        }

        OptionalDouble rating = sellerRatings
                .stream()
                .mapToInt(SellerRating::getRating)
                .average();

        if (rating.isEmpty()) {
            throw new CannotGetAverageRatingException("Cannot get average rating for user with id: " + sellerId);
        }

        int numOfRatings = sellerRatings.size();

        return new SellerRatingDto(sellerId, rating.getAsDouble(), numOfRatings);
    }

    public boolean giveSellerRating(GiveSellerRatingRequestModel giveSellerRatingRequestModel) throws CannotFindUserForRatingException {

        if (doesRatingAlreadyExist(giveSellerRatingRequestModel)) {
            return true;
        }

        boolean userCheck = checkUsers(giveSellerRatingRequestModel);
        boolean ratingCheck = checkRating(giveSellerRatingRequestModel.getRating());

        if (userCheck && ratingCheck) {
            SellerRating sellerRating = new SellerRating(giveSellerRatingRequestModel.getRating(), giveSellerRatingRequestModel.getSellerId(), giveSellerRatingRequestModel.getUserId());
            sellerRatingRepository.save(sellerRating);
            return true;
        }

        return false;
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

    private boolean checkUsers(GiveSellerRatingRequestModel giveSellerRatingRequestModel) throws CannotFindUserForRatingException {
        User seller;
        User user;

        try {
            seller = userCredentialsRepository.findUserById(giveSellerRatingRequestModel.getSellerId());
        } catch (Exception e) {
            throw new CannotFindUserForRatingException("Cannot find seller user model for id: " + giveSellerRatingRequestModel.getSellerId() + ". Error message: " + e.getMessage());
        }

        try {
            user = userCredentialsRepository.findUserById(giveSellerRatingRequestModel.getUserId());
        } catch (Exception e) {
            throw new CannotFindUserForRatingException("Cannot find user model for id: " + giveSellerRatingRequestModel.getUserId() + ". Error message: " + e.getMessage());
        }

        return (user != null && seller != null) && (!user.getId().equals(seller.getId()));
    }
}
