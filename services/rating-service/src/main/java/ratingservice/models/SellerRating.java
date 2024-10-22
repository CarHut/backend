package ratingservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "seller_ratings")
public class SellerRating {

    @Id
    private String id;
    private Integer rating;
    private String sellerId;
    private String userId;

    public SellerRating() {}

    public SellerRating(String id, Integer rating, String sellerId, String userId) {
        this.id = id;
        this.rating = rating;
        this.sellerId = sellerId;
        this.userId = userId;
    }

    public SellerRating(Integer rating, String sellerId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.rating = rating;
        this.sellerId = sellerId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

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
}
