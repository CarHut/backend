package com.carhut.util.exceptions.rating;

public class CannotFindUserForRatingException extends RatingException{
    public CannotFindUserForRatingException(String errMessage) {
        super(errMessage);
    }
}
