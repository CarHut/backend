package com.carhut.util.exceptions.authentication;

import com.carhut.util.exceptions.CarHutException;

public class CarHutAuthenticationException extends CarHutException {
    public CarHutAuthenticationException(String errMessage) {
        super(errMessage);
    }
}
