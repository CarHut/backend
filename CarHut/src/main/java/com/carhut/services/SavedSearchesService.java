package com.carhut.services;

import com.carhut.controllers.RemoveSavedSearchRequestModel;
import com.carhut.database.repository.SavedSearchesRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.requests.PrincipalRequest;
import com.carhut.requests.requestmodels.SaveSearchRequestModel;
import com.carhut.requests.requestmodels.SimpleUsernameRequestModel;
import com.carhut.security.models.User;
import com.carhut.security.annotations.UserAccessCheck;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.savedsearches.SavedSearchesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedSearchesService {

    @Autowired
    private SavedSearchesRepository savedSearchesRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @UserAccessCheck
    public RequestStatusEntity addNewSavedSearch(PrincipalRequest<SaveSearchRequestModel> saveSearchRequestModelPrincipalRequest) throws SavedSearchesException, CarHutAuthenticationException {
        SaveSearchRequestModel savedSearch = saveSearchRequestModelPrincipalRequest.getDto();
        try {
            SavedSearch newSavedSearch = new SavedSearch(savedSearch.getUserId(), savedSearch.getSortBy(), savedSearch.getOffersPerPage(),
                    savedSearch.getPriceFrom(), savedSearch.getPriceTo(), savedSearch.getMileageFrom(),
                    savedSearch.getMileageTo(), savedSearch.getFuelType(), savedSearch.getGearboxType(), savedSearch.getPowertrainType(),
                    savedSearch.getPowerFrom(), savedSearch.getPowerTo(), savedSearch.getBrandsAndModels());
            savedSearchesRepository.save(newSavedSearch);
            return RequestStatusEntity.SUCCESS;
        }
        catch (Exception e) {
            throw new SavedSearchesException("Search couldn't be saved. Message: " + e.getMessage());
        }
    }


    @UserAccessCheck
    public List<SavedSearch> getSavedSearchesByUsername(PrincipalRequest<SimpleUsernameRequestModel> username) throws SavedSearchesException, CarHutAuthenticationException {
        try {
            User user = userCredentialsRepository.findUserByUsername(username.getDto().getUsername());
            return savedSearchesRepository.getSavedSearchesByUserId(user.getId());
        }
        catch (Exception e) {
            throw new SavedSearchesException("Couldn't retrieve saved searches from database. Message: " + e.getMessage());
        }

    }

    @UserAccessCheck
    public void removeSavedSearch(PrincipalRequest<RemoveSavedSearchRequestModel> removeSavedSearchRequestModelPrincipalRequest) throws SavedSearchesException {

        try {
            SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(removeSavedSearchRequestModelPrincipalRequest.getDto().getSearchId());
            savedSearchesRepository.delete(savedSearch);
        }
        catch (Exception e) {
            throw new SavedSearchesException("Cannot remove saved search from database. Message: " + e.getMessage());
        }
    }
}
