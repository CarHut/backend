package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCarRequestModel extends SimpleUsernameRequestModel {
    private String carId;
}
