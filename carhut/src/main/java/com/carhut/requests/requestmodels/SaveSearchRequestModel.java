package com.carhut.requests.requestmodels;

import com.carhut.models.carhut.SavedSearch;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveSearchRequestModel extends SavedSearch implements PrincipalRequestBody {
    private String username;
}
