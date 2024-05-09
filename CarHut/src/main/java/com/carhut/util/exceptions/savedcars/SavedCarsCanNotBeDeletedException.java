package com.carhut.util.exceptions.savedcars;

public class SavedCarsCanNotBeDeletedException extends SavedCarsException {
    public SavedCarsCanNotBeDeletedException(String errMessage) {
        super(errMessage);
    }
}
