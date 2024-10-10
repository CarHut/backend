package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveCarRequestModel {
    private String carId;
    private String userId;
}
