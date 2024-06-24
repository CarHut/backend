package com.carhut.models.requestmodels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDetailsRequestBody {
    private String username;
}
