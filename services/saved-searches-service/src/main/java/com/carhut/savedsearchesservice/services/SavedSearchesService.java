package com.carhut.savedsearchesservice.services;

import com.carhut.savedsearchesservice.models.SavedSearch;
import com.carhut.savedsearchesservice.repositories.SavedSearchesRepository;
import com.carhut.savedsearchesservice.requests.RemoveSavedSearchRequestModel;
import com.carhut.savedsearchesservice.requests.SimpleUserIdRequestModel;
import com.carhut.savedsearchesservice.status.SavedSearchesServiceStatus;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedSearchesService {

    @Autowired
    private SavedSearchesRepository savedSearchesRepository;

    public SavedSearchesServiceStatus addNewSavedSearch(SavedSearch savedSearch) {
        if (savedSearch == null) {
            return SavedSearchesServiceStatus.OBJECT_IS_NULL;
        }

        try {
            SavedSearch newSavedSearch = new SavedSearch(savedSearch.getUserId(), savedSearch.getSortBy(), savedSearch.getOffersPerPage(),
                    savedSearch.getPriceFrom(), savedSearch.getPriceTo(), savedSearch.getMileageFrom(),
                    savedSearch.getMileageTo(), savedSearch.getFuelType(), savedSearch.getGearboxType(), savedSearch.getPowertrainType(),
                    savedSearch.getPowerFrom(), savedSearch.getPowerTo(), savedSearch.getBrandsAndModels());
            savedSearchesRepository.save(newSavedSearch);
            return SavedSearchesServiceStatus.SUCCESS;
        }
        catch (Exception e) {
            return SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_SAVED_EXCEPTIONALLY;
        }
    }

    public List<SavedSearch> getSavedSearchesByUsername(SimpleUserIdRequestModel idRequestModel) {
        if (idRequestModel == null) {
            return null;
        }

        return savedSearchesRepository.getSavedSearchesByUserId(idRequestModel.getUserId());
    }

    public SavedSearchesServiceStatus removeSavedSearch(RemoveSavedSearchRequestModel removeSavedSearchRequestModel) {

        if (removeSavedSearchRequestModel == null) {
            System.out.println("Request is null. Cannot remove saved search from database.");
            return SavedSearchesServiceStatus.ERROR;
        }

        try {
            SavedSearch savedSearch = savedSearchesRepository.getSavedSearchesBySavedSearchId(removeSavedSearchRequestModel.getSearchId());
            if (savedSearch == null) {
                return SavedSearchesServiceStatus.OBJECT_NOT_FOUND;
            }
            savedSearchesRepository.delete(savedSearch);
        }
        catch (Exception e) {
            return SavedSearchesServiceStatus.OBJECT_COULD_NOT_BE_DELETED_EXCEPTIONALLY;
        }
        return SavedSearchesServiceStatus.SUCCESS;
    }

    @VisibleForTesting
    public void flush() {
        savedSearchesRepository.deleteAll();
    }

}
