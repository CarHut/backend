package com.carhut.controllers;

import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveSavedSearchRequestModel extends SimpleUsernameRequestModel {
    private String searchId;
}
