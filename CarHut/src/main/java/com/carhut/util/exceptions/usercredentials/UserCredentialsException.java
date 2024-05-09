package com.carhut.util.exceptions.usercredentials;

import com.carhut.util.exceptions.CarHutException;

public class UserCredentialsException extends CarHutException {
    public UserCredentialsException(String errMessage) {
        super(errMessage);
    }
}
