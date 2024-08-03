package com.carhut.requests.requestmodels;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveCarRequestModel extends SimpleUsernameRequestModel {
    private String carId;
}
