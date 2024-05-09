package com.carhut.util.exceptions.savedcars;

public class SavedCarsNotFoundException extends SavedCarsException {
    public SavedCarsNotFoundException(String errMessage) {
        super(errMessage);
    }
}
