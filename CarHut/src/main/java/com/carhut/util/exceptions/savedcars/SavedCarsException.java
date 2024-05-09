package com.carhut.util.exceptions.savedcars;

import com.carhut.util.exceptions.CarHutException;

public class SavedCarsException extends CarHutException {
    public SavedCarsException(String errMessage) {
        super(errMessage);
    }
}
