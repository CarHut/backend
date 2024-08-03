package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiveSellerRatingRequestModel extends SimpleUsernameRequestModel implements PrincipalRequestBody {

    private String sellerId;
    private String userId;
    private int rating;

    @Override
    public String getUsername() {
        return userId;
    }
}
