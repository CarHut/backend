package ratingservice.dtos;

public class SellerRatingDto {

    private String sellerId;
    private Double rating;
    private Integer numOfRatings;

    public SellerRatingDto() {}

    public SellerRatingDto(String sellerId, Double rating, Integer numOfRatings) {
        this.sellerId = sellerId;
        this.rating = rating;
        this.numOfRatings = numOfRatings;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(Integer numOfRatings) {
        this.numOfRatings = numOfRatings;
    }
}
