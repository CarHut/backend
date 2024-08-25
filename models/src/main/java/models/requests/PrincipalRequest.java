package models.requests;

import lombok.Getter;
import lombok.Setter;
import models.principals.AuthenticationPrincipal;

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
