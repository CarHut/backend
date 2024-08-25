package ratingservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "seller_ratings")
@Getter
@Setter
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
}
