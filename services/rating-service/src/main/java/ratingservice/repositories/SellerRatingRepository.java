package ratingservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ratingservice.models.SellerRating;

import java.util.List;

@Repository
public interface SellerRatingRepository extends JpaRepository<SellerRating, String> {

    @Query(value = "SELECT * FROM seller_ratings WHERE seller_id = :sellerId", nativeQuery = true)
    List<SellerRating> getSellerRatingsBySellerId(@Param("sellerId") String sellerId);

    @Query(value = "SELECT * FROM seller_ratings WHERE seller_id = :sellerId AND user_id = :userId", nativeQuery = true)
    SellerRating getRatingBySellerIdAndUserId(@Param("sellerId") String sellerId, @Param("userId") String userId);
}
