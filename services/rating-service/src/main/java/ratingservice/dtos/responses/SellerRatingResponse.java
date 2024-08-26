package ratingservice.dtos.responses;

import models.responses.GenericResponse;

public class SellerRatingResponse<T> extends GenericResponse<T> {
    public SellerRatingResponse(String status, int code, String message, T body) {
        super(status, code, message, body);
    }
}
