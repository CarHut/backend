package com.carhut.savedcarsservice.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCarRequestModel {
    private String carId;
    private String userId;
}
