package com.carhut.models.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDetailsRequestBody {
    private String username;
}
