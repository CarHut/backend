package ratingservice.models.requests;

import lombok.Getter;
import lombok.Setter;
import models.requests.PrincipalRequestBody;

@Getter
@Setter
public class GiveSellerRatingRequestModel implements PrincipalRequestBody {

    private String sellerId;
    private String userId;
    private int rating;

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public void setUsername(String username) {

    }
}
