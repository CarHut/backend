package com.carhut.services;

import com.carhut.requests.requestmodels.RemoveSavedSearchRequestModel;
import com.carhut.enums.ServiceStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveSearchRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.AuthenticationPrincipal;
import com.carhut.services.carhutapi.SavedSearchesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class SavedSearchesServiceTests {

    @Autowired
    private SavedSearchesService savedSearchesService;
    private String searchId;

    @AfterEach
    public void flushRepository() {
        savedSearchesService.flush();
    }

    @Test
    public void addNewSavedSearch_requestIsNull() {
        ServiceStatusEntity status = savedSearchesService.addNewSavedSearch(null);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
    }

    @Test
    public void addNewSavedSearch_requestIsInvalid() {
        SaveSearchRequestModel saveSearchRequestModel = new SaveSearchRequestModel();
        saveSearchRequestModel.setUsername("INVALID_USERNAME");
        saveSearchRequestModel.setUserId("INVALID_USER_ID");

        PrincipalRequest<SaveSearchRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(saveSearchRequestModel);

        ServiceStatusEntity serviceStatusEntity = savedSearchesService.addNewSavedSearch(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, serviceStatusEntity);
    }

    @Test
    public void addNewSavedSearch_validScenario() {
        SaveSearchRequestModel saveSearchRequestModel = new SaveSearchRequestModel();
        saveSearchRequestModel.setUsername("admin");
        saveSearchRequestModel.setUserId("user0");
        saveSearchRequestModel.setBrandsAndModels(Collections.emptyList());

        PrincipalRequest<SaveSearchRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(saveSearchRequestModel);

        ServiceStatusEntity serviceStatusEntity = savedSearchesService.addNewSavedSearch(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, serviceStatusEntity);
    }

    @Test
    public void getSavedSearchesByUsername_requestIsNull() {
        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(null);
        Assertions.assertNull(savedSearches);
    }

    @Test
    public void getSavedSearchesByUsername_requestIsInvalid() {
        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
        simpleUsernameRequestModel.setUsername("INVALID_USERNAME");

        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(simpleUsernameRequestModel);

        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(principalRequest);
        Assertions.assertNull(savedSearches);
    }

    @Test
    public void getSavedSearchesByUsername_validScenario() throws InterruptedException {
        addNewSavedSearch_validScenario();

        Thread.sleep(1000);

        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
        simpleUsernameRequestModel.setUsername("admin");

        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(simpleUsernameRequestModel);

        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(principalRequest);
        Assertions.assertNotNull(savedSearches);
        Assertions.assertFalse(savedSearches.isEmpty());
        searchId = savedSearches.get(0).getId();
    }

    @Test
    public void removeSavedSearch_requestIsNull() {
        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(null);
        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
    }

    @Test
    public void removeSavedSearch_requestIsInvalid() {
        RemoveSavedSearchRequestModel removeSavedSearchRequestModel = new RemoveSavedSearchRequestModel();
        removeSavedSearchRequestModel.setUsername("INVALID_USERNAME");
        removeSavedSearchRequestModel.setSearchId("RANDOM_ID");

        PrincipalRequest<RemoveSavedSearchRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(removeSavedSearchRequestModel);

        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.OBJECT_NOT_FOUND, status);
    }

    @Test
    public void removeSavedSearch_validScenario() throws InterruptedException {
        addNewSavedSearch_validScenario();

        Thread.sleep(1000);

        getSavedSearchesByUsername_validScenario();

        Thread.sleep(1000);

        RemoveSavedSearchRequestModel removeSavedSearchRequestModel = new RemoveSavedSearchRequestModel();
        removeSavedSearchRequestModel.setUsername("admin");
        removeSavedSearchRequestModel.setSearchId(searchId);

        PrincipalRequest<RemoveSavedSearchRequestModel> principalRequest = new PrincipalRequest<>();
        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
        authenticationPrincipal.setUsername("admin");
        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
        principalRequest.setDto(removeSavedSearchRequestModel);

        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(principalRequest);
        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, status);
    }

}
