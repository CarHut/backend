package com.carhut.util.exceptions.savedcars;

public class SavedCarsCanNotBeSavedException extends SavedCarsException {
    public SavedCarsCanNotBeSavedException(String errMessage) {
        super(errMessage);
    }
}
