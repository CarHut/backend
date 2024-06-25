package com.carhut.services;

import com.carhut.database.repository.SavedSearchesRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.enums.RequestStatusEntity;
import com.carhut.models.carhut.SavedSearch;
import com.carhut.models.security.User;
import com.carhut.util.exceptions.authentication.CarHutAuthenticationException;
import com.carhut.util.exceptions.savedsearches.SavedSearchesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedSearchesService {

    @Autowired
    private SavedSearchesRepository savedSearchesRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    public RequestStatusEntity addNewSavedSearch(SavedSearch savedSearch) throws SavedSearchesException, CarHutAuthenticationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurityContextHolder = ((User)authentication.getPrincipal());

        if (!userSecurityContextHolder.getUsername().equals(userCredentialsRepository.getUsernameByUserId(savedSearch.getUserId()))) {
            throw new CarHutAuthenticationException("Unauthorized access add new saved search.");
        }

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


    public List<SavedSearch> getSavedSearchesByUsername(String username) throws SavedSearchesException, CarHutAuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userSecurityContextHolder = ((User)authentication.getPrincipal());

        if (!userSecurityContextHolder.getUsername().equals(username)) {
            throw new CarHutAuthenticationException("Unauthorized access to get saved searches.");
        }

        try {
            User user = userCredentialsRepository.findUserByUsername(username);
            return savedSearchesRepository.getSavedSearchesByUserId(user.getId());
        }
        catch (Exception e) {
            throw new SavedSearchesException("Couldn't retrieve saved searches from database. Message: " + e.getMessage());
        }

    }

    public void removeSavedSearch(String id) throws SavedSearchesException {

        try {
            SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(id);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userSecurityContextHolder = ((User)authentication.getPrincipal());

            if (!userSecurityContextHolder.getUsername().equals(userCredentialsRepository.getUsernameByUserId(savedSearch.getUserId()))) {
                throw new CarHutAuthenticationException("Unauthorized access to remove saved searches.");
            }

            savedSearchesRepository.delete(savedSearch);
        }
        catch (Exception e) {
            throw new SavedSearchesException("Cannot remove saved search from database. Message: " + e.getMessage());
        }
    }
}
