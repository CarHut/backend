package com.carhut.requests.requestmodels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDetailsRequestBody implements PrincipalRequestBody {
    private String username;
}
