package com.carhut.savedsearchesservice.requests;

import lombok.Getter;
import lombok.Setter;
import models.generic.SimpleUsernameRequestModel;

@Getter
@Setter
public class RemoveSavedSearchRequestModel extends SimpleUsernameRequestModel {
    private String searchId;
}
