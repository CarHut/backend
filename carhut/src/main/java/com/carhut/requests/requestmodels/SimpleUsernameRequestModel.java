package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleUsernameRequestModel implements PrincipalRequestBody {

    private String username;

    @Override
    public String getUsername() {
        return username;
    }
}
