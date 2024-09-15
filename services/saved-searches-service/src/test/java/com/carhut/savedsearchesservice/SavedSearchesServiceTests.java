package com.carhut.savedsearchesservice;

import com.carhut.savedsearchesservice.models.SavedSearch;
import com.carhut.savedsearchesservice.services.SavedSearchesService;
import com.carhut.savedsearchesservice.status.SavedSearchesServiceStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

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
        SavedSearchesServiceStatus status = savedSearchesService.addNewSavedSearch(null);
        Assertions.assertEquals(SavedSearchesServiceStatus.ERROR, status);
    }

    @Test
    public void addNewSavedSearch_requestIsInvalid() {
        SavedSearch savedSearch = new SavedSearch();

        SavedSearchesServiceStatus status = savedSearchesService.addNewSavedSearch(savedSearch);
        Assertions.assertEquals(SavedSearchesServiceStatus.ERROR, status);
    }

//    @Test
//    public void addNewSavedSearch_validScenario() {
//        SaveSearchRequestModel saveSearchRequestModel = new SaveSearchRequestModel();
//        saveSearchRequestModel.setUsername("admin");
//        saveSearchRequestModel.setUserId("user0");
//        saveSearchRequestModel.setBrandsAndModels(Collections.emptyList());
//
//        PrincipalRequest<SaveSearchRequestModel> principalRequest = new PrincipalRequest<>();
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        principalRequest.setDto(saveSearchRequestModel);
//
//        ServiceStatusEntity serviceStatusEntity = savedSearchesService.addNewSavedSearch(principalRequest);
//        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, serviceStatusEntity);
//    }
//
//    @Test
//    public void getSavedSearchesByUsername_requestIsNull() {
//        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(null);
//        Assertions.assertNull(savedSearches);
//    }
//
//    @Test
//    public void getSavedSearchesByUsername_requestIsInvalid() {
//        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
//        simpleUsernameRequestModel.setUsername("INVALID_USERNAME");
//
//        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        principalRequest.setDto(simpleUsernameRequestModel);
//
//        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(principalRequest);
//        Assertions.assertNull(savedSearches);
//    }
//
//    @Test
//    public void getSavedSearchesByUsername_validScenario() throws InterruptedException {
//        addNewSavedSearch_validScenario();
//
//        Thread.sleep(1000);
//
//        SimpleUsernameRequestModel simpleUsernameRequestModel = new SimpleUsernameRequestModel();
//        simpleUsernameRequestModel.setUsername("admin");
//
//        PrincipalRequest<SimpleUsernameRequestModel> principalRequest = new PrincipalRequest<>();
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        principalRequest.setDto(simpleUsernameRequestModel);
//
//        List<SavedSearch> savedSearches = savedSearchesService.getSavedSearchesByUsername(principalRequest);
//        Assertions.assertNotNull(savedSearches);
//        Assertions.assertFalse(savedSearches.isEmpty());
//        searchId = savedSearches.get(0).getId();
//    }
//
//    @Test
//    public void removeSavedSearch_requestIsNull() {
//        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(null);
//        Assertions.assertEquals(ServiceStatusEntity.ERROR, status);
//    }
//
//    @Test
//    public void removeSavedSearch_requestIsInvalid() {
//        RemoveSavedSearchRequestModel removeSavedSearchRequestModel = new RemoveSavedSearchRequestModel();
//        removeSavedSearchRequestModel.setUsername("INVALID_USERNAME");
//        removeSavedSearchRequestModel.setSearchId("RANDOM_ID");
//
//        PrincipalRequest<RemoveSavedSearchRequestModel> principalRequest = new PrincipalRequest<>();
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        principalRequest.setDto(removeSavedSearchRequestModel);
//
//        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(principalRequest);
//        Assertions.assertEquals(ServiceStatusEntity.OBJECT_NOT_FOUND, status);
//    }
//
//    @Test
//    public void removeSavedSearch_validScenario() throws InterruptedException {
//        addNewSavedSearch_validScenario();
//
//        Thread.sleep(1000);
//
//        getSavedSearchesByUsername_validScenario();
//
//        Thread.sleep(1000);
//
//        RemoveSavedSearchRequestModel removeSavedSearchRequestModel = new RemoveSavedSearchRequestModel();
//        removeSavedSearchRequestModel.setUsername("admin");
//        removeSavedSearchRequestModel.setSearchId(searchId);
//
//        PrincipalRequest<RemoveSavedSearchRequestModel> principalRequest = new PrincipalRequest<>();
//        AuthenticationPrincipal authenticationPrincipal = new AuthenticationPrincipal();
//        authenticationPrincipal.setUsername("admin");
//        principalRequest.setAuthenticationPrincipal(authenticationPrincipal);
//        principalRequest.setDto(removeSavedSearchRequestModel);
//
//        ServiceStatusEntity status = savedSearchesService.removeSavedSearch(principalRequest);
//        Assertions.assertEquals(ServiceStatusEntity.SUCCESS, status);
//    }

}
