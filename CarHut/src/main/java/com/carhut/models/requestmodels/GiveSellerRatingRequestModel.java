package com.carhut.models.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiveSellerRatingRequestModel {

    private String sellerId;
    private String userId;
    private int rating;

}
