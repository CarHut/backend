package ratingservice.services;

import com.carhut.commons.http.caller.RequestAuthenticationCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ratingservice.dtos.SellerRatingDto;
import ratingservice.models.SellerRating;
import ratingservice.dtos.GiveSellerRatingRequestModel;
import ratingservice.repository.SellerRatingRepository;
import ratingservice.repository.resourceprovider.SellerRatingDatabaseResourceManager;
import ratingservice.status.RatingServiceStatus;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.OptionalDouble;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
public class RatingService {

    @Autowired
    private SellerRatingRepository sellerRatingRepository;
    private SellerRatingDatabaseResourceManager sellerRatingDatabaseResourceManager =
            SellerRatingDatabaseResourceManager.getInstance();
    private final RequestAuthenticationCaller requestAuthenticationCaller =
            new RequestAuthenticationCaller();

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
        return sellerRatingDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public CompletableFuture<RatingServiceStatus> giveSellerRating(
            GiveSellerRatingRequestModel giveSellerRatingRequestModel, String bearerToken)
            throws ExecutionException, InterruptedException {

        CompletableFuture<RatingServiceStatus> cf = new CompletableFuture<>();

        if (bearerToken == null) {
            cf.complete(RatingServiceStatus.USER_NOT_AUTHORIZED);
            return cf;
        }

        if (giveSellerRatingRequestModel == null) {
            cf.complete(RatingServiceStatus.OBJECT_IS_NULL);
            return cf;
        }

        CompletableFuture<HttpResponse<String>> authorizationRequest = isRequestAuthenticatedAsync(
                giveSellerRatingRequestModel.getUserId(), bearerToken, cf);

        boolean ratingCheck = checkRating(giveSellerRatingRequestModel.getRating());
        boolean checkUsers = checkUsers(giveSellerRatingRequestModel);

        CompletableFuture ratingServiceRequest = null;

        if (ratingCheck && checkUsers) {
            Function<Void, RatingServiceStatus> function = (unused) -> {
                SellerRating existingRating = sellerRatingRepository
                        .getRatingBySellerIdAndUserId(giveSellerRatingRequestModel.getSellerId(),
                                giveSellerRatingRequestModel.getUserId());

                // Update rating
                if (existingRating != null) {
                    existingRating.setRating(giveSellerRatingRequestModel.getRating());
                    sellerRatingRepository.save(existingRating);
                    return RatingServiceStatus.SUCCESS;
                }

                SellerRating sellerRating = new SellerRating(giveSellerRatingRequestModel.getRating(),
                        giveSellerRatingRequestModel.getSellerId(), giveSellerRatingRequestModel.getUserId());
                try {
                    sellerRatingRepository.save(sellerRating);
                } catch (Exception e) {
                    return RatingServiceStatus.ERROR;
                }
                return RatingServiceStatus.SUCCESS;
            };

            ratingServiceRequest = authorizationRequest.thenCompose(
                    authResponse -> sellerRatingDatabaseResourceManager.acquireDatabaseResource(function));
        }

        if (ratingServiceRequest == null) {
            cf.complete(RatingServiceStatus.ERROR);
            return cf;
        }

        ratingServiceRequest.whenComplete((result, ex) -> {
            if (ex != null) {
                cf.complete(RatingServiceStatus.ERROR);
            } else {
                cf.complete(RatingServiceStatus.SUCCESS);
            }
        });

        return cf;
    }

    private boolean checkUsers(GiveSellerRatingRequestModel giveSellerRatingRequestModel) {
        return !(giveSellerRatingRequestModel.getSellerId().equals(giveSellerRatingRequestModel.getUserId()));
    }

    private boolean checkRating(int rating) {
        return rating >= 0 && rating <= 5;
    }

    private <T> CompletableFuture<HttpResponse<String>> isRequestAuthenticatedAsync(String userId, String bearerToken,
                                                                                    CompletableFuture<T> finalizable) {
        CompletableFuture<HttpResponse<String>> response = requestAuthenticationCaller
                .isRequestAuthenticatedAsync(userId, bearerToken);

        response.whenComplete((result, ex) -> {
            if (ex != null) {
                finalizable.complete(null);
            } else if (result.statusCode() < 200 || result.statusCode() > 299){
                finalizable.complete(null);
            }
        });

        return response;
    }
}
