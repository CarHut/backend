package com.carhut.requests;

import com.carhut.security.models.AuthenticationPrincipal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalRequest<T> {

    private AuthenticationPrincipal authenticationPrincipal;
    private T dto;

    public PrincipalRequest() {}

    public PrincipalRequest(AuthenticationPrincipal authenticationPrincipal, T dto) {
        this.authenticationPrincipal = authenticationPrincipal;
        this.dto = dto;
    }
}
