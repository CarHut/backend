package ratingservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.models.SellerRating;
import ratingservice.dtos.requests.GiveSellerRatingRequestModel;
import ratingservice.repository.SellerRatingRepository;
import ratingservice.repository.resourceprovider.RatingDatabaseResourceManager;
import ratingservice.status.RatingServiceStatus;

import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
public class SellerRatingService {

    @Autowired
    private SellerRatingRepository sellerRatingRepository;
    private RatingDatabaseResourceManager ratingDatabaseResourceManager =
            RatingDatabaseResourceManager.getInstance();

    public CompletableFuture<SellerRatingDto> getSellerRating(String sellerId)
            throws ExecutionException, InterruptedException {

        CompletableFuture<SellerRatingDto> cf = new CompletableFuture<>();
        if (sellerId == null || sellerId.isEmpty()) {
            cf.complete(null);
            return cf;
        }

        List<SellerRating> sellerRatings = getSellerRatings(sellerId).get();
        if (sellerRatings == null) {
            cf.complete(null);
            return cf;
        }

        OptionalDouble rating = countAverageRating(sellerRatings);
        if (rating.isEmpty()) {
            cf.complete(null);
            return cf;
        }

        int numOfRatings = sellerRatings.size();
        cf.complete(new SellerRatingDto(sellerId, rating.getAsDouble(), numOfRatings));
        return cf;
    }

    private OptionalDouble countAverageRating(List<SellerRating> sellerRatings) {
        OptionalDouble rating = sellerRatings
                .stream()
                .mapToInt(SellerRating::getRating)
                .average();
        return rating;
    }

    private CompletableFuture<List<SellerRating>> getSellerRatings(String sellerId) {
        Function<Void, List<SellerRating>> function = (unused) -> {
            try {
                return sellerRatingRepository.getSellerRatingsBySellerId(sellerId);
            }
            catch (Exception e) {
                return null;
            }
        };
        return ratingDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<RatingServiceStatus> giveSellerRating(
            GiveSellerRatingRequestModel giveSellerRatingRequestModel)
            throws ExecutionException, InterruptedException {

        CompletableFuture<RatingServiceStatus> cf = new CompletableFuture<>();
        if (giveSellerRatingRequestModel == null) {
            cf.complete(RatingServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        if (doesRatingAlreadyExist(giveSellerRatingRequestModel).get()) {
            cf.complete(RatingServiceStatus.RATING_ALREADY_EXISTS);
            return cf;
        }

        boolean ratingCheck = checkRating(giveSellerRatingRequestModel.getRating());
        boolean checkUsers = checkUsers(giveSellerRatingRequestModel);

        if (ratingCheck && checkUsers) {
            Function<Void, RatingServiceStatus> function = (unused) -> {
                SellerRating sellerRating = new SellerRating(giveSellerRatingRequestModel.getRating(),
                        giveSellerRatingRequestModel.getSellerId(), giveSellerRatingRequestModel.getUserId());
                try {
                    sellerRatingRepository.save(sellerRating);
                } catch (Exception e) {
                    return RatingServiceStatus.ERROR;
                }
                return RatingServiceStatus.SUCCESS;
            };
            return ratingDatabaseResourceManager.acquireDatabaseResource(function);
        }

        cf.complete(RatingServiceStatus.ERROR);
        return cf;
    }

    private boolean checkUsers(GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        return !(giveSellerRatingRequestModel.getSellerId().equals(giveSellerRatingRequestModel.getUserId()));
    }

    private CompletableFuture<Boolean> doesRatingAlreadyExist(GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        Function<Void, Boolean> function = (unused) -> {
            SellerRating rating;

            try {
                rating = sellerRatingRepository.getRatingBySellerIdAndUserId(giveSellerRatingRequestModel.getSellerId(),
                        giveSellerRatingRequestModel.getUserId());
            } catch (Exception e) {
                return false;
            }

            if (rating != null) {
                rating.setRating(giveSellerRatingRequestModel.getRating());
                sellerRatingRepository.save(rating);
                return true;
            }

            return false;
        };

        return ratingDatabaseResourceManager.acquireDatabaseResource(function);
    }

    private boolean checkRating(int rating) {
        return rating >= 0 && rating <= 5;
    }

    public void flush() {
        sellerRatingRepository.deleteAll();
    }
}
