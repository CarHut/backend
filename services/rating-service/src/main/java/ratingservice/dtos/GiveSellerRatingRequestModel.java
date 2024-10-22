package ratingservice.dtos;

public class GiveSellerRatingRequestModel {

    private String sellerId;
    private String userId;
    private int rating;

    public GiveSellerRatingRequestModel(String sellerId, String userId, int rating) {
        this.sellerId = sellerId;
        this.userId = userId;
        this.rating = rating;
    }

    public GiveSellerRatingRequestModel() {}

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
